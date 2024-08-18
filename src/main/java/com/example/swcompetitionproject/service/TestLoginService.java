package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.auth.LoginDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.HttpCookie;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

@AllArgsConstructor
@Service
@Slf4j
public class TestLoginService {

    public static final String USERCHECKURL = "https://sso1.mju.ac.kr/mju/userCheck.do";
    public static final String mmjumate="https://mju-mate.com/main/auth/login";
    private final RestTemplate restTemplate;

    public String login(LoginDto loginDto) {

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //입력 데이터
        MultiValueMap<String, String> userData = new LinkedMultiValueMap<>();
        userData.add("id", loginDto.getId());
        userData.add("passwrd", loginDto.getPasswrd());

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> userDatarequestEntity = new HttpEntity<>(userData, headers);

        ResponseEntity<Map> userCheckRes = restTemplate.exchange(USERCHECKURL, HttpMethod.POST, userDatarequestEntity, Map.class);

        // JSON 응답 파싱
        Map<String, Object> userCheckResJson = userCheckRes.getBody();

        // 에러 코드 확인
        if (userCheckResJson != null && !userCheckResJson.get("error").equals("0000") && !userCheckResJson.get("error").equals("VL-3130")) {
            throw new RuntimeException(userCheckResJson.toString());
        }


        //로그인 데이터 생성
        MultiValueMap<String, String> loginData = new LinkedMultiValueMap<>();
        loginData.add("id", loginDto.getId());
        loginData.add("passwrd", loginDto.getPasswrd());
        loginData.add("redirect_uri", "http://lms.mju.ac.kr/ilos/bandi/sso/index.jsp");

        String loginURL = "https://sso1.mju.ac.kr/login/ajaxActionLogin2.do";
        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> loginDatarequestEntity = new HttpEntity<>(loginData, headers);

        // POST 요청 보내기 (응답 처리 없이 세션 생성 목적)
        restTemplate.exchange(loginURL, HttpMethod.POST, loginDatarequestEntity, String.class);
        log.info("로그인 완료");

        String token2URL = "https://sso1.mju.ac.kr/oauth2/token2.do";
        restTemplate.exchange(token2URL, HttpMethod.POST, loginDatarequestEntity, String.class);
        log.info("토크 유얄엘 처리 완료");

        String loginBandiURL = "https://lms.mju.ac.kr/ilos/lo/login_bandi_sso.acl";
        ResponseEntity<String> loginBandiRes = restTemplate.getForEntity(loginBandiURL, String.class);
        log.info("로그인 확인: {}", loginBandiRes);



        // 응답 텍스트 확인
        //if (loginBandiRes.getBody() != null && loginBandiRes.getBody().contains("로그인 정보가 일치하지 않습니다.")) {
        //    throw new RuntimeException("login_bandi_sso.acl 통과 실패");
        //}
        //log.info("로그인 응답 테스트");


        // 로그인 후 쿠키를 포함한 응답을 가져옴
        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(loginBandiURL), String.class);
        log.info("로그인 후 쿠키를 포함한 응답: {}", response);


        // 쿠키를 담을 맵 초기화
        Map<String, String> result = new HashMap<>();

        // 쿠키를 헤더에서 추출
        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        log.info("쿠키 리스트: {}",cookies);
        if (cookies != null) {
            for (String cookie : cookies) {
                List<HttpCookie> httpCookies = HttpCookie.parse(cookie);
                for (HttpCookie httpCookie : httpCookies) {
                    String name = httpCookie.getName();
                    String value = httpCookie.getValue();
                    // "JSESSIONID" 쿠키 필터링
                    if ("JSESSIONID".equals(name) && value.contains(".")) {
                        continue;
                    }
                    result.put(name, value);
                }
            }
        }
        log.info("쿠키 추출 결과: {}", result);

        String scouterCookie = valueOf(result.get("SCOUTER"));
        log.info("SCOUTER쿠키 값: {}", scouterCookie);

        return scouterCookie;
    }


    //public List<Map<String, String>> getSubjectList(int year, int term, String jsessionId) {
//
    //    String SUBJECT_URL = "https://lms.mju.ac.kr/ilos/mp/course_register_list.acl";
//
    //    // Set JSESSIONID cookie if provided
    //    HttpHeaders headers = new HttpHeaders();
    //    if (jsessionId != null && !jsessionId.isEmpty()) {
    //        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + jsessionId);
    //    }
//
    //    // Prepare request data
    //    MultiValueMap<String, String> subjectData = new LinkedMultiValueMap<>();
    //    subjectData.add("YEAR", valueOf(year));
    //    subjectData.add("TERM", valueOf(term));
    //    subjectData.add("encoding", "utf-8");
//
    //    // Create HttpEntity
    //    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(subjectData, headers);
//
    //    // Send POST request
    //    ResponseEntity<String> response = restTemplate.exchange(SUBJECT_URL, HttpMethod.POST, requestEntity, String.class);
    //    log.info("과목 추출한거: {}", response);
    //}
}
