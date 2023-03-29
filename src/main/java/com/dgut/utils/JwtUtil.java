package com.dgut.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JwtUtil {
    private static String SIGNAL = "1HU&**UUY**(GNH";
    /**
     * 生成token
     */
    public static String getToken(Map<String, String> map){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 30);             //设置过期时间为30天

        //创建jwt builder
        final JWTCreator.Builder builder = JWT.create();
        //payload
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        String token = builder.withExpiresAt(instance.getTime())//指定令牌过期时间
                .sign(Algorithm.HMAC256(SIGNAL));//sign
        return token;
    }

    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(SIGNAL)).build().verify(token);
    }
}
