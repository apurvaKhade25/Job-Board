/*
package com.jobboard.job_board.Util;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JavaUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // convert secret string to Key object
    private Key getSigningKey(){
        byte[] keyBytes=secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
*/
