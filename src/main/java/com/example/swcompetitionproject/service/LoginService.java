package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.authentication.JwtTokenProvider;
import com.example.swcompetitionproject.dto.request.auth.LoginDto;
import com.example.swcompetitionproject.dto.response.TokenResponseDto;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {

    private final RestTemplate restTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;


    private static final String USER_CHECK_URL = "https://sso1.mju.ac.kr/mju/userCheck.do"; //유저체크 URL
    private static final String LOGIN_URL = "https://sso1.mju.ac.kr/login/ajaxActionLogin2.do";
    private static final String TOKEN_URL = "https://sso1.mju.ac.kr/oauth2/token2.do";
    private static final String LOGIN_BANDI_URL = "https://lms.mju.ac.kr/ilos/lo/login_bandi_sso.acl";

    private static final String TEST="https://search.naver.com/search.naver?where=news&sm=tab_jum&query=뉴스";


    public TokenResponseDto login(LoginDto loginDto) {
        log.info("Starting login process for user: {}", loginDto.getId());

        /**
         * 요청할 데이터 requestEntity만들기
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> userCheckParams = new LinkedMultiValueMap<>();
        userCheckParams.add("id", loginDto.getId());
        userCheckParams.add("passwrd", loginDto.getPasswrd());
        userCheckParams.add("redirect_uri", "http://lms.mju.ac.kr/ilos/bandi/sso/index.jsp");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(userCheckParams, headers);

        /**
         * 유저 존재 확인
         */
        String userCheckResponse = restTemplate.postForObject(USER_CHECK_URL, requestEntity, String.class);

        //체크중에 요류코드가 존재한다면 로그인 실패
        if (userCheckResponse.contains("VL-2100")) {
            log.error("User check failed: {}", userCheckResponse);
            throw new RuntimeException("User check failed: " + userCheckResponse);
        }
        log.info("User check response: {}", userCheckResponse);

        /**
         * 로그인
         */
        ResponseEntity<String> loginResponseEntity = restTemplate.exchange(LOGIN_URL, HttpMethod.POST, requestEntity, String.class);
        HttpStatus statusCode = (HttpStatus) loginResponseEntity.getStatusCode();
        String loginRespons = loginResponseEntity.getBody();
        HttpHeaders headersss = loginResponseEntity.getHeaders();

        //결과 확인
        log.info("loginResponseEntity: {}", loginResponseEntity);
        log.info("HTTP Status Code: {}", statusCode);
        log.info("login response: {}", loginRespons);
        log.info("Response headers: {}", headersss);

        User user;
        //유저가 디비에 등록되어있는지 확인
        if (userRepository.findByStudentNumber(loginDto.getId()).isPresent()) {
            //등록되어져 있다면 유저 객체 찾기
            user = userRepository.findByStudentNumber(loginDto.getId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        } else {
            //등록되어있지 않는다면 유저 등록(회원가입과 동일)
            user = User.builder()
                    .studentNumber(loginDto.getId())
                    .build();
            userRepository.save(user);
        }


        //var login =restTemplate.getForObject(TEST,String.class);
        //log.info(login);


        /**
         * 현재 사용 안하는 코드
         * */
        //토큰 발급하는 URL로 연결
        //String tokenResponse = restTemplate.postForObject(TOKEN_URL, requestEntity, String.class);
        //log.info("tokenResponse: {}", tokenResponse);

        //첫 번째 요청에서 받은 리다이렉션 URL을 가져와 다시 요청을 보냄->이미 주소를 알고있기 때문에 주석처리
        HttpHeaders responseHeaders = restTemplate.postForEntity(TOKEN_URL, requestEntity, String.class).getHeaders();
        log.info("responseHeaders: {}", responseHeaders);
        String redirectUrl = responseHeaders.getLocation().toString(); // 리다이렉션 URL 가져오기
        log.info("redirectUrl: {}",redirectUrl);

        //// 쿠키를 가져와 로깅 또는 수동으로 처리
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(redirectUrl,requestEntity, String.class);
        HttpHeaders headersCookies = responseEntity.getHeaders();
        log.info("responseEntity: {}", responseEntity);

        //여기사부터 쿠키가 없다고 나옴
        List<String> cookies = headersCookies.get(HttpHeaders.SET_COOKIE);
        if (cookies != null) {
            for (String cookie : cookies) {
                log.info("JSESSIONID: {}", cookie);
                // 필요 시 쿠키를 수동으로 추가할 수 있음
            }
        }

        // 리다이렉션된 URL로 재요청
        String redirecTtokenResponse = restTemplate.postForObject(LOGIN_BANDI_URL, requestEntity, String.class);
        log.info("tokenResponse after redirection: {}", redirecTtokenResponse);


        /**
         * payload와 accessToken발급
         */
        String payload = String.valueOf(user.getId());
        String accessToken = jwtTokenProvider.createToken(payload);

        return new TokenResponseDto(accessToken);
    }
}
