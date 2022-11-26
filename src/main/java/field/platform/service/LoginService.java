package field.platform.service;

import field.platform.domain.Member;
import field.platform.domain.UserRole;
import field.platform.repository.MemberRepository;
import field.platform.security.kakao.KakaoOauth2;
import field.platform.security.kakao.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final KakaoOauth2 kakaoOauth2;
    //private final AuthenticationManager authenticationManager;

    public Member kakaoLogin(String authorizedCode) {
        //kakakOAuth2를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOauth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String username = userInfo.getUsername();
        String password = username;
        String email = userInfo.getEmail();
        String profile = userInfo.getProfile();

        Member member = new Member(email, username, password, profile);

        Member kakaoMember = memberRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoMember == null) {
            UserRole role = UserRole.USER;
            kakaoMember = new Member(email, username, password, profile, role);
            memberRepository.save(kakaoMember);
        }

//        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(username, password);
//        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        return member;
    }
}
