package field.platform.security.kakao;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.json.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Component
public class KakaoOauth2 {

    public KakaoUserInfo getUserInfo(String authorizedCode) {
        //인가코드 -> 엑세스 토큰
        String accessToken = getAccessToken(authorizedCode);
        //엑세스 토큰 -> 카카오 사용자 정보
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);
        return userInfo;
    }

    //return 카카오 로그인 페이지
    public void getAuthorizedCode() {
        String REST_API_KEY = "ae3d3854486ace63b03a73e0e1881b8b";
        String REDIRECT_URI = "http://localhost:8080/members/signup"; //프론트 redirect uri 인듯?

        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code")
                .encode()
                .build()
                .expand(REST_API_KEY, REDIRECT_URI)
                .toUri();

        HttpHeaders headers = new HttpHeaders();

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                uri.toString(),
                HttpMethod.GET,
                request,
                String.class
        );

    }

    private String getAccessToken(String authorizedCode) {
        //httpheader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //httpbody 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "ae3d3854486ace63b03a73e0e1881b8b");
        params.add("redirect_uri", "http://localhost:8080/members/signup");
        params.add("code", authorizedCode);

        //httpheader와 httpbody 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //http 요청
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth.token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );


        //json => 액세스 토큰 파싱
        String tokenJson = response.getBody();
        JSONObject rjson = new JSONObject(tokenJson);
        String accessToken = rjson.getString("access_token");

        return accessToken;
    }


    public KakaoUserInfo getUserInfoByToken(String accessToken) {
        //HttpHeader 오브젝트 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //httpheader와 httpbody 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        //http 요청
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class
        );

        JSONObject body = new JSONObject(response.getBody());
        Long id = body.getLong("id");
        String email = body.getJSONObject("kakao_account").getString("email");
        String username = body.getJSONObject("properties").getString("nickname");
        String profile = body.getJSONObject("properties").getString("profile_image");
        return new KakaoUserInfo(id, email, username, profile);
    }
}
