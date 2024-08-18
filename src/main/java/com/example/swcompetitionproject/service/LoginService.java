package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.authentication.JwtTokenProvider;
import com.example.swcompetitionproject.dto.request.auth.LoginDto;
import com.example.swcompetitionproject.dto.response.auth.TokenResponseDto;
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

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {

    private final RestTemplate restTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;


    public static final String usercheckURL = "https://sso1.mju.ac.kr/mju/userCheck.do";
    public static final String loginURL = "https://sso1.mju.ac.kr/login/ajaxActionLogin2.do";
    public static final String token2URL = "https://sso1.mju.ac.kr/oauth2/token2.do";
    public static final String loginBandiURL = "https://lms.mju.ac.kr/ilos/lo/login_bandi_sso.acl";


    public TokenResponseDto login(LoginDto loginDto) {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //유저 정보 입력 데이터
        MultiValueMap<String, String> userData = new LinkedMultiValueMap<>();
        userData.add("id", loginDto.getId());
        userData.add("passwrd", loginDto.getPasswrd());

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> userDatarequestEntity = new HttpEntity<>(userData, headers);

        //유저 존재 확인
        ResponseEntity<Map> userCheckRes = restTemplate.exchange(usercheckURL, HttpMethod.POST, userDatarequestEntity, Map.class);

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

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> loginDatarequestEntity = new HttpEntity<>(loginData, headers);

        // POST 요청 보내기 (응답 처리 없이 세션 생성 목적)
        restTemplate.exchange(loginURL, HttpMethod.POST, loginDatarequestEntity, String.class);
        log.info("로그인 완료");


        restTemplate.exchange(token2URL, HttpMethod.POST, loginDatarequestEntity, String.class);
        log.info("토큰 유얄엘 처리 완료");


        ResponseEntity<String> loginBandiRes = restTemplate.getForEntity(loginBandiURL, String.class);
        log.info("로그인 확인: {}", loginBandiRes);

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

        /**
         * payload와 accessToken발급
         */
        String payload = String.valueOf(user.getId());
        String accessToken = jwtTokenProvider.createToken(payload);

        return new TokenResponseDto(accessToken);
    }
}
