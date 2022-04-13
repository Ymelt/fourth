package com.example.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.User;

import java.util.Calendar;
import java.util.Date;

public class TokenUtils {

    /**
     * 设置过期时间5分钟
     */
    private static final long EXPIRE_TIME = 5 * 60 * 1000;

//    public static String getToken(String userId,String sign){
//        return JWT.create().withAudience(userId)
//                    .withExpiresAt(DateUtil.offsetHour(new Date(),2))
//                    .sign(Algorithm.HMAC256(sign));
//    }
    public static String getToken(User user){
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String token;
        token = JWT.create().withAudience(String.valueOf(user.getUserId()),user.getUserName())
                            .withExpiresAt(date)
                            .sign(Algorithm.HMAC256(user.getUserPassword()));
        return token;

    }
}
