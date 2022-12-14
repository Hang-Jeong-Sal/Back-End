package field.platform.controller;

import field.platform.domain.Member;

import field.platform.dto.jwt.TokenDto;
import field.platform.dto.login.KakaoLoginRequestDto;


import field.platform.dto.login.LoginResultDto;
import field.platform.dto.login.LoginRequestDto;
import field.platform.repository.MemberRepository;
import field.platform.service.AuthService;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final MemberRepository memberRepository;

    @PostMapping("/kakao/login")
    public TokenDto kakaoLogin(@RequestBody KakaoLoginRequestDto kakaoLoginRequestDto) {
        return authService.createToken(authService.kakaoLogin(kakaoLoginRequestDto));

    }

    @PostMapping("/kakao/logout")
    public ResponseEntity kakaoLogout(@RequestHeader(name = "Authorization") String bearerToken) {
        System.out.println("bearerToken = " + bearerToken);
        return authService.logout(bearerToken);
    }

    @GetMapping("")
    public Map getByAccessToken(@RequestHeader(name = "Authorization") String bearerToken) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", memberRepository.findByAccessToken(bearerToken).get().getId());
        return map;
    }
}

//    @PostMapping("/signup")
//    public ResponseEntity signup(@RequestBody LoginRequestDto loginRequestDto) {
//        return authService.signup(loginRequestDto);
//
//    }

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

