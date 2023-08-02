package com.example.springsecurity2.utills;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.sql.Date;

public class JwtTokenUtil {


    //토큰 발급
    public static String createToken(String userName, String key, long expireTimeMs ){
        Claims claims = Jwts.claims(); // 일종의 map
        claims.put("userName",userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 만든 날ㅏ
                .setExpiration(new Date(System.currentTimeMillis()+expireTimeMs)) // 민료 날짜
                .signWith(SignatureAlgorithm.HS256,key) //HS256 이라는 key를 알고리즘을 싸인 하겠다.
                .compact()
                ;
    }
}
