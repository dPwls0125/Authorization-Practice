package com.example.springsecurity2.service;

import com.example.springsecurity2.domain.User;
import com.example.springsecurity2.exception.AppException;
import com.example.springsecurity2.exception.ErrorCode;
import com.example.springsecurity2.repository.UserRepository;
import com.example.springsecurity2.utills.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private Long expireTimeMs = 1000 * 60 * 60l;

    public String join(String userName, String password) {

        // userName 중복 체크
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + "는 이미 존재합니다.");
                });

        // 저장
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .build();
        userRepository.save(user);

        return "SUCCESS";
    }

    public String login(String userName, String password) {
        // userName 없음
        User selectedUsr = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다"));

        // password 틀림
        if(!encoder.matches(password, selectedUsr.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다");
        }

        String token = JwtUtil.createToken(selectedUsr.getUserName(), secretKey, expireTimeMs);

        // error 안 나면 토큰 발행
        return token;
    }
}