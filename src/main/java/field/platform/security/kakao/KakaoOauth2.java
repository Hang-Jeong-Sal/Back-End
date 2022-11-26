package field.platform.security.kakao;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.json.*;


@Component
public class KakaoOauth2 {

    public KakaoUserInfo getUserInfo(String authorizedCode) {
        //인가코드 -> 엑세스 토큰
        String accessToken = getAccessToken(authorizedCode);
        //엑세스 토큰 -> 카카오 사용자 정보
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);
        return userInfo;
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
