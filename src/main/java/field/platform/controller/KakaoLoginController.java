package field.platform.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class KakaoLoginController {
    @GetMapping("/user/kakao/callback")
        public String kakaoLogin(String code) {
            userService.kakaoLogin(code);

            return "redirect:/";
    }
}
