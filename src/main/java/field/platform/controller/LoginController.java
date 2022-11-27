package field.platform.controller;

import field.platform.domain.Member;
import field.platform.dto.login.LoginResultDto;
import field.platform.security.kakao.KakaoOauth2;
import field.platform.security.kakao.KakaoUserInfo;
import field.platform.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final KakaoOauth2 kakaoOauth2;

    @GetMapping("/members/signup")
    public LoginResultDto kakaoLogin(@RequestHeader(name = "code") String code) {
        Member member = loginService.kakaoLogin(code);
        LoginResultDto loginResultDto = new LoginResultDto();
        loginResultDto.setData(member);
        return loginResultDto;
    }

    @GetMapping("/members/authorization")
    public void getAuthorizedCode() {
        kakaoOauth2.getAuthorizedCode();
    }

    @GetMapping("/members/login")
    public LoginResultDto login(@RequestHeader(name = "access_token") String token) {
        log.debug("Token:" + token);
        LoginResultDto loginResultDto = new LoginResultDto();
        KakaoUserInfo userInfoByToken = kakaoOauth2.getUserInfoByToken(token);
        if (userInfoByToken == null) {
            loginResultDto.setResultCode("403");
            loginResultDto.setResultMessage("there's no member");
            return loginResultDto;
        }
        loginResultDto.setData(userInfoByToken);
        return loginResultDto;
    }
}
