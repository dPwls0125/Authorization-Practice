package com.example.springsecurity2.service;

import com.example.springsecurity2.domain.User;
import com.example.springsecurity2.exception.AppException;
import com.example.springsecurity2.exception.ErrorCode;
import com.example.springsecurity2.repository.UserRepository;
import com.example.springsecurity2.utills.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.regex.Pattern.matches;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder ; // bean 에러

    @Value("${jwt.token.secret}")
    private String key; // key 값에 속성값이 들어감.


    public String join(String userName, String password){
        //USername 중복 check
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName +" 은 이미 있습니다.");
                });

        // 저장
        User user = User.builder()
                .userName(userName)
                .password(password)
                .build(); // 객체 생성 메서드 호출
        userRepository.save(user);
        return "Success" ;
    }

    public String login(String userName, String password){
        //userName 없음
        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));
        //password 틀림
        if(! encoder.matches(selectedUser.getPassword(), password)){
                throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드를 잘못 입력헀습니다.");
        }

//        String token = JwtTokenUtil.createToken(selectedUser.getUserName(),key)
        // 앞에서 Exception 안났으면 토큰 발행.
        return "token 리턴" ;
    }
}
