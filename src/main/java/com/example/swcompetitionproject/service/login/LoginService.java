package com.example.swcompetitionproject.service.login;

import com.example.swcompetitionproject.dto.response.request.LoginDto;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {

    private final RestTemplate restTemplate;

    private static final String USER_CHECK_URL = "https://sso1.mju.ac.kr/mju/userCheck.do";
    private static final String LOGIN_URL = "https://sso1.mju.ac.kr/login/ajaxActionLogin2.do";
    private static final String TOKEN_URL = "https://sso1.mju.ac.kr/oauth2/token2.do";
    private static final String LOGIN_BANDI_URL = "https://lms.mju.ac.kr/ilos/lo/login_bandi_sso.acl";
    private final StringHttpMessageConverter stringHttpMessageConverter;

    public void login(LoginDto loginDto) {
        log.info("Starting login process for user: {}", loginDto.getId());

        //요청할 데이터 requestEntity만들기
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> userCheckParams = new LinkedMultiValueMap<>();
        userCheckParams.add("id", loginDto.getId());
        userCheckParams.add("passwrd", loginDto.getPasswrd());
        userCheckParams.add("redirect_uri", "http://lms.mju.ac.kr/ilos/bandi/sso/index.jsp");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(userCheckParams, headers);

        //유저 존재 확인
        String userCheckResponse = restTemplate.postForObject(USER_CHECK_URL, requestEntity, String.class);
        log.info("User check response: {}", userCheckResponse);

        if (userCheckResponse.contains("VL-2100")) {
            log.error("User check failed: {}", userCheckResponse);
            throw new RuntimeException("User check failed: " + userCheckResponse);
        }

        //로그인
        String loginResponse = restTemplate.postForObject(LOGIN_URL, requestEntity, String.class);
        log.info("login response: {}", loginResponse);

        ResponseEntity<String> loginResponseEntity = restTemplate.exchange(LOGIN_URL, HttpMethod.POST, requestEntity, String.class);
        HttpStatus statusCode = (HttpStatus) loginResponseEntity.getStatusCode();
        String loginRespons = loginResponseEntity.getBody();

        log.info("login response: {}", loginRespons);
        log.info("HTTP Status Code: {}", statusCode);
        log.info("login response: {}", loginResponseEntity);

        HttpHeaders headersss = loginResponseEntity.getHeaders();
        log.info("Response headers: {}", headersss);

        String tokenResponse = restTemplate.postForObject(TOKEN_URL, requestEntity, String.class);
        log.info("tokenResponse: {}", tokenResponse);


        //String loginBandiResponse = restTemplate.getForObject(LOGIN_BANDI_URL,String.class);
        //log.info("loginBandiResponse: {}", loginBandiResponse);

/*
        // 상태 코드가 307 (리디렉션)인 경우
        if (statusCode == HttpStatus.TEMPORARY_REDIRECT) {
            String redirectUrl = headers.getLocation().toString();
            log.info("Redirecting to: {}", redirectUrl);

            // 리디렉션 URL로 새로운 요청을 보냄
            ResponseEntity<String> redirectedResponse = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, String.class);
            HttpHeaders redirectedHeaders = redirectedResponse.getHeaders();
            log.info("Redirected response headers: {}", redirectedHeaders);

            // 쿠키 추출
            List<String> setCookieHeaders = redirectedHeaders.get(HttpHeaders.SET_COOKIE);
            if (setCookieHeaders != null) {
                for (String cookieHeader : setCookieHeaders) {
                    log.info("Set-Cookie Header: {}", cookieHeader);
                }
            } else {
                log.info("redirected response에서 Set-Cookie를 찾을수 없습니다");
            }
        }


 */

        //// Obtain token->이 코드의 존재의 의미를 몰라 주석 처리
        //String obtainToken = restTemplate.postForObject(TOKEN_URL, requestEntity, String.class);
        //log.info("Obtain token: {}", obtainToken);


        // Check login->무조건 페이지 부르는 거라서 이 코드의 존재의 의미를 몰라 주석 처리
        //String loginBandiResponse = restTemplate.getForObject(LOGIN_BANDI_URL, String.class);
        //log.info("loginBandi: {}",loginBandiResponse);
//
        //if (loginBandiResponse.contains("로그인 정보가 일치하지 않습니다.")) {
        //    throw new RuntimeException("Login check failed.");
        //}
    }
}
