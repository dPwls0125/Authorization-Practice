package com.example.springsecurity2.configuration;

import com.example.springsecurity2.service.UserService;
import com.example.springsecurity2.utills.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        final String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("AUTHORIZATION : {}",authentication);

        // 토큰 안보내면 block
        if(authentication == null || !authentication.startsWith("Bearer")){
            log.error("AUTHORIZATION을 잘못 보냈습니다.");
            filterChain.doFilter(request,response);
            return;
        }
        //Token 꺼내기
        String token = authentication.split(" ")[1];

        // Token expired 여부 확인
        if(JwtUtil.isExpired(token, secretKey)){
            log.error("토큰이 만료되었습니다.");
            filterChain.doFilter(request,response);
            return;
        }
        // UserName Token에서 꺼내기 _ controller에서 사용 가능
        String userName = JwtUtil.getUserName(token, secretKey);
        log.info("userName:{}",userName);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName,null, List.of(new SimpleGrantedAuthority("USER")));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 토큰에 싸인(권한 부여)
        filterChain.doFilter(request,response);
    }
}
