package field.platform.service;

import field.platform.domain.User;
import field.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final AuthenticationManager authenticationManager;
    private static final String ADMIN_TOKEN = "";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, kakaoOAuth2 kakaoOAuth2, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
        this.authenticationManager = authenticationManager;
    }

    // 카카오 OAuth2를 통해 카카오 사용자 정보 조회
    // input으로 프론트에서 인가코드가 넘어옵니다
    public void kakaoLogin(String authorizedCode){
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        String username = nickname;
        String password = kakaoId + ADMIN_TOKEN;

        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        // 카카오 정보로 회원가입
        if(kakaoUser == null){
            String encodedPassword = passwordEncoder.encode(password);
            UserRole role = userRole.USER;

            kakaoUser = new User(nickname, encodedPassword, email, role, kakaoId);
            userRepository.save(kakaoUser);
        }

        // 로그인 처리
        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
