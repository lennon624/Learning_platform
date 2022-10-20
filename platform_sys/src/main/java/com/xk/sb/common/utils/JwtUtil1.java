package com.xk.sb.common.utils;

import com.xk.sb.common.constants.Result;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 *
 * @author zhangmingcheng
 */
@Configuration
public class JwtUtil1 {

    private static long EXPIRATION_TIME = 3600000; // 1 hour

    private static String SECRET = "XE6XI6NG6YUAN77MDK4z";// 秘钥

    private static final String USER_ID = "id";



    /**
     * 生成jwtToken
     *
     * @param id
     * @return
     */
    public static String generateToken(String id) {
        HashMap<String, Object> map = new HashMap<>();
        // you can put any data in the map
        map.put(USER_ID,id);
        String jwt = Jwts.builder().setClaims(map).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
        return jwt;
    }

    /**
     * 校验jwtToken
     *
     * @param token
     * @return
     */
    public static Result validateToken(String token) {
        Result result = new Result();
        if (token != null) {
            Map<String, Object> body = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            String id = (String) (body.get(USER_ID));
            if (id == null || id.isEmpty()) {
               result.fail("token without id");
            } else {
                result.setData(id);
                result.success();
            }
        } else {
            result.fail("Missing token");
        }
        return result;
    }


}