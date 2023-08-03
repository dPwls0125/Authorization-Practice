package com.example.springsecurity2.configuration;

import com.example.springsecurity2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Security 사용을 위해 필요함.
//Security를 적용하게 되면, 모든 API에 인증이 필요하다는 정보가 들어가게 됨.
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationWebSecurity {

    private final UserService userService;

    @Value("${jwt.token.secret}")
    private String secretKey;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable()// ui가 아닌 토큰을 이용할것이기 때문에 disable 처리
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api/v1/users/login","/api/v1/users/join" ).permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/**").authenticated() // permit 제외한 모든 포스트 요청 인증 필요
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //토큰을 받으면, 받은 토큰을 가지고 풀어줘야 함. 풀려면 secretkey가 필요함.
                .addFilterBefore(new JwtFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();


    }
}
