package com.cccc.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class Jwt_Utils {
    //有效期为
    public static final Long time = 10*60*60 *1000L;// 60 *1000  一分钟（五天）
    //设置秘钥明文
    public static final String signature = "xiluhulu";
    public static String createToken(int id,String role){
//        加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        JwtBuilder builder = Jwts.builder();
        String jwtBuilder = builder
                //header:头部
                .setHeaderParam("head", "jwt")
                //payload:载体
                .claim("role",role)
                .claim("id",id)
                //主题
                .setSubject("xxx")
                //有效时间
                .setExpiration(new Date(System.currentTimeMillis()+time))
                //jwt的id（属于载体中的）
                .setId(UUID.randomUUID().toString())
                .signWith(signatureAlgorithm,signature)
                .compact();

        return jwtBuilder;
    }
    public static Claims parseToken(String token) throws ExpiredJwtException,UnsupportedJwtException,MalformedJwtException,SignatureException,IllegalArgumentException{
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey(signature).parseClaimsJws(token);
        Claims tokenBody = claimsJws.getBody();
        return tokenBody;
    }
}
