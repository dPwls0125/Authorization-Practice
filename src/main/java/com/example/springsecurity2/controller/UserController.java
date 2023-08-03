package com.example.springsecurity2.controller;


import com.example.springsecurity2.domain.dto.UserJoinRequest;
import com.example.springsecurity2.domain.dto.UserLoginRequest;
import com.example.springsecurity2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest dto){
        userService.join(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body("회원가입을 성공했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> log(@RequestBody UserLoginRequest dto){
        String token = userService.login(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body(token);  // 로그인하고 반환받은 토큰을 responsebody에 넘김
    }
}
