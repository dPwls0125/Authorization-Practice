package com.example.springsecurity2.utills;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.sql.Date;

public class JwtUtil {
    // 만료 여부 반환
    public static boolean isExpired(String token, String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration().before(new java.util.Date());
    }
    // get UserName
    // 파싱이란 문장 성분을 분해하고 필요한 내용을 찾음
    public static String getUserName(String token, String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userName", String.class);
    }
    //토큰 발급
    public static String createToken(String userName, String key, Long expireTimeMs ){
        Claims claims = Jwts.claims(); // 일종의 map
        claims.put("userName",userName); // key = username, 받아온 username
        // claim _ jwt 에 원하는 내용을 담을 수 있음.
        return Jwts.builder() // jwt 빌더.
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) //오늘 날짜(만든 날짜)
                .setExpiration(new Date(System.currentTimeMillis()+expireTimeMs)) // 민료 날짜
                .signWith(SignatureAlgorithm.HS256,key) //HS256 이라는 key를 알고리즘을 싸인 하겠다. , 알고리즘은 여러가지가 있음
                .compact()
                ;
    }
}
