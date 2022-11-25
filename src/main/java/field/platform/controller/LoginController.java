package field.platform.controller;

import field.platform.domain.Member;
import field.platform.dto.login.LoginResultDto;
import field.platform.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/members/signup")
    public LoginResultDto kakaoLogin(@RequestHeader(name = "code") String code) {
        Member member = loginService.kakaoLogin(code);
        LoginResultDto loginResultDto = new LoginResultDto();
        loginResultDto.setData(member);
        return loginResultDto;
    }
}
