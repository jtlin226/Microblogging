package com.revature.Micro.util;

import com.revature.Micro.Entity.MicroUser;
import com.revature.Micro.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;



/*
source:
https://github.com/koushikkothagal/spring-security-jwt
 */

/**
 * SPRING SECURITY
 */
@Service
public class JwtUtil {

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getHashSignature()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateTempToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        return createTempToken(claims, userDetails.getUsername());
    }

    private String createTempToken(Map<String, Object> claims, String subject)
    {
        Date now = new Date(System.currentTimeMillis());
        Date expire = new Date(System.currentTimeMillis() + 1000*60*2); // one hour from issue time
        return Jwts.builder()
                .setClaims(claims) // is empty now - can add stuff to it
                .setSubject(subject) // the username
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS256, getHashSignature()).compact();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date(System.currentTimeMillis());
        Date expire = new Date(System.currentTimeMillis() + 1000*60*60*3); // one hour from issue time
        return Jwts.builder()
                .setClaims(claims) // is empty now - can add stuff to it
                .setSubject(subject) // the username
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS256, getHashSignature()).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String getHashSignature() {
        return "secret";
    }

    public static MicroUser extractUser(UserService userService){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUsername(authentication.getName());
    }

    public static String extractUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}

