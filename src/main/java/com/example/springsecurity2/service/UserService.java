package com.example.springsecurity2.service;

import com.example.springsecurity2.domain.User;
import com.example.springsecurity2.exception.AppException;
import com.example.springsecurity2.exception.ErrorCode;
import com.example.springsecurity2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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

}
