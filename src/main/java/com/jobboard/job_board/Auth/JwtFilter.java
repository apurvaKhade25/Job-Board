package com.jobboard.job_board.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // convert secret string to Key object
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    //    JWT string → data (verify + decode)
    public Claims extractAllClaims(String token) {           //JWT string → data (verify + decode)
        return Jwts.parserBuilder()                         // start with building a parser
                .setSigningKey(getSigningKey())
                //tell it your secret key
                .build()                                    // built the parser
                .parseClaimsJws(token)                      //parse + extract calims
                .getBody();                                 // get the payload (claims)

    }

    // extract email
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // get role from token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // get expiry date from token
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // validate token — email matches + not expired
    public boolean validateToken(String token, String email) {
        String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }


}
