package field.platform.controller;

import field.platform.domain.Member;
import field.platform.dto.login.LoginResultDto;
import field.platform.dto.login.LoginRequestDto;
import field.platform.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/members/kakao/login")
    public LoginResultDto kakaoLogin(@RequestBody String code) {
        Member member = loginService.kakaoLogin(code);
        LoginResultDto loginResultDto = new LoginResultDto();
        loginResultDto.setData(member);
        return loginResultDto;
    }

    @PostMapping("/members/signup")
    public ResponseEntity signup(@RequestBody LoginRequestDto loginRequestDto) {
        return loginService.signup(loginRequestDto);
    }

//    @GetMapping("/members/authorization")
//    public void getAuthorizedCode() {
//        kakaoOauth2.getAuthorizedCode();
//    } -> front에서 할 거

//    @GetMapping("/members/login")
//    public LoginResultDto login(@RequestHeader(name = "access_token") String token) {
//        //log.debug("Token:" + token);
//        LoginResultDto loginResultDto = new LoginResultDto();
//        KakaoUserInfo userInfoByToken = kakaoOauth2.getUserInfoByToken(token);
//        if (userInfoByToken == null) {
//            loginResultDto.setResultCode("403");
//            loginResultDto.setResultMessage("there's no member");
//            return loginResultDto;
//        }
//        loginResultDto.setData(userInfoByToken);
//        return loginResultDto;
//    }
}
