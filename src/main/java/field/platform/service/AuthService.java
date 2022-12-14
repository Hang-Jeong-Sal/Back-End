package field.platform.service;

import field.platform.domain.Authority;
import field.platform.domain.Member;
import field.platform.domain.RefreshToken;
import field.platform.domain.UserRole;
import field.platform.dto.jwt.TokenDto;
import field.platform.dto.jwt.TokenRequestDto;
import field.platform.dto.login.KakaoLoginRequestDto;
import field.platform.dto.login.LoginRequestDto;
import field.platform.exception.AuthorityException;
import field.platform.exception.BizException;
import field.platform.exception.JwtException;
import field.platform.exception.MemberException;
import field.platform.jwt.CustomKakaoIdAuthToken;
import field.platform.jwt.TokenProvider;
import field.platform.repository.AuthorityRepository;
import field.platform.repository.MemberRepository;
import field.platform.repository.RefreshTokenRepository;
import field.platform.security.kakao.KakaoOauth2;
import field.platform.security.kakao.KakaoUserInfo;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final EntityManager em;
    private final AuthenticationManager authenticationManager;
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;
    private final AuthorityRepository authorityRepository;
    private final MemberRepository memberRepository;
    private final KakaoOauth2 kakaoOauth2;
    private final CustomKakaoAuthService customKakaoAuthService;
    private final RefreshTokenRepository refreshTokenRepository;
    private String resolveToken(String bearerToken) {
        // bearer : 123123123123123 -> return 123123123123123123
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
    private static String getRandomString(int length)
    {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();

        String chars[] =
                "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,0,1,2,3,4,5,6,7,8,9".split(",");

        for (int i=0 ; i<length ; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }
    @Transactional
    public KakaoUserInfo kakaoLogin(KakaoLoginRequestDto kakaoLoginRequestDto) {
        String authorizedCode = kakaoLoginRequestDto.getAuthorizedCode();
        //kakakOAuth2??? ?????? ????????? ????????? ?????? ??????
//        ??????????????? member?????? ????????? ?????????????????????

        KakaoUserInfo userInfo = kakaoOauth2.getUserInfo(authorizedCode);

        Long kakaoId = userInfo.getId();
        String username = userInfo.getUsername();
        String email = userInfo.getEmail();
        String profile = userInfo.getProfile();

        Optional<Member> byKakaoIdMember = memberRepository.findByKakaoId(kakaoId);
        if (byKakaoIdMember.isEmpty()) {
            Optional<Authority> byAuthorityName = authorityRepository.findByAuthorityName(UserRole.ROLE_KAKAO);
            if (byAuthorityName.isEmpty()) {
                throw new BizException(AuthorityException.NOT_FOUND_AUTHORITY);
            }
            Set<Authority> authorities = new HashSet<>();
            authorities.add(byAuthorityName.get());
            Member newMember = new Member(kakaoId, username, email, profile, authorities);
            memberRepository.save(newMember);
        }
        return userInfo;
    }

    @Transactional
    public TokenDto createToken (KakaoUserInfo kakaoUserInfo){
        Long kakaoId = kakaoUserInfo.getId();
        CustomKakaoIdAuthToken customKakaoIdAuthToken = new CustomKakaoIdAuthToken(String.valueOf(kakaoId), "");
        Authentication authentication = authenticationManager.authenticate(customKakaoIdAuthToken);
        log.info("authentication");
        Member findMember = customKakaoAuthService.getMember(kakaoId);

        String accessToken = tokenProvider.createAccessTokenByKakaoId(kakaoId, findMember.getAuthorities());
        if (refreshTokenRepository.existsByKey(String.valueOf(kakaoId))) {
            refreshTokenRepository.deleteRefreshToken(refreshTokenRepository.findByKey(String.valueOf(kakaoId))
                    .orElseThrow(() -> new BizException(MemberException.NOT_FOUND_USER)));
        }
        String newRefreshToken = tokenProvider.createRefreshToken(kakaoId, findMember.getAuthorities());

        refreshTokenRepository.saveRefreshToken(
                RefreshToken.builder()
                        .key(String.valueOf(kakaoId))
                        .value(newRefreshToken)
                        .build()
        );
        findMember.setAccessToken(accessToken);
        return tokenProvider.createTokenDTO(accessToken, newRefreshToken);
    }

    public ResponseEntity logout(String bearerToken){
        String accessToken = resolveToken(bearerToken);

        Long kakaoIdByToken = tokenProvider.getMemberKakaoIdByToken(accessToken);
        RefreshToken refreshToken = refreshTokenRepository.findByKey(String.valueOf(kakaoIdByToken))
                .orElseThrow(() -> new BizException(MemberException.LOGOUT_MEMBER));

        refreshTokenRepository.deleteRefreshToken(refreshToken);
        return new ResponseEntity(HttpStatus.OK);
    }


//    @Transactional
//    public ResponseEntity signup(LoginRequestDto requestDto) {
//        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
//            throw new BizException(MemberException.DUPLICATE_USER);
//        }
//
//
//
//        Member member = requestDto.toMember();
//        memberRepository.save(member);
//
//        return new ResponseEntity("??????????????????", HttpStatus.OK);
//    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        /*
         *  accessToken ??? JWT Filter ?????? ???????????? ???
         * */
        String originAccessToken = resolveToken(tokenRequestDto.getAccessToken());
        String originRefreshToken = tokenRequestDto.getRefreshToken();

        // refreshToken ??????
        int refreshTokenFlag = tokenProvider.validateToken(originRefreshToken);



        //refreshToken ???????????? ????????? ?????? ????????? ????????????.
        if (refreshTokenFlag == -1) {
            throw new BizException(JwtException.BAD_TOKEN); // ????????? ???????????? ??????
        } else if (refreshTokenFlag == 2) {
            throw new BizException(JwtException.REFRESH_TOKEN_EXPIRED); // ???????????? ?????? ??????
        }

        // 2. Access Token ?????? Member Email ????????????
        Authentication authentication = tokenProvider.getAuthenticationByKakaoId(originAccessToken);

//        log.debug("Authentication = {}",authentication);

        // 3. ??????????????? Member Email ??? ???????????? Refresh Token ??? ?????????
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new BizException(MemberException.LOGOUT_MEMBER)); // ?????? ????????? ?????????


        // 4. Refresh Token ??????????????? ??????
        if (!refreshToken.getValue().equals(originRefreshToken)) {
            throw new BizException(JwtException.BAD_TOKEN); // ????????? ???????????? ????????????.
        }

        // 5. ????????? ?????? ??????
        Long kakaoIdByToken = tokenProvider.getMemberKakaoIdByToken(originAccessToken);
        Member member = customKakaoAuthService.getMember(kakaoIdByToken);

        String newAccessToken = tokenProvider.createAccessTokenByKakaoId(kakaoIdByToken, member.getAuthorities());
        String newRefreshToken = tokenProvider.createRefreshToken(kakaoIdByToken, member.getAuthorities());
        TokenDto tokenDto = tokenProvider.createTokenDTO(newAccessToken, newRefreshToken);

        // 6. ????????? ?????? ???????????? (dirtyChecking?????? ????????????)
        refreshToken.updateValue(newRefreshToken);

        // ?????? ??????
        return tokenDto;
    }



}
