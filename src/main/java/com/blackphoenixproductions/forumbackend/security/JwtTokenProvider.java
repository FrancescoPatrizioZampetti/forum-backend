package com.blackphoenixproductions.forumbackend.security;

import dto.openApi.exception.CustomException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long validityInMilliseconds;
    private final long refreshTokenValidiyInMilliseconds;
    private final MyUserDetailsService myUserDetails;
    private final String ROLE_REFRESH = "ROLE_REFRESH";
    private final String ROLE_RESET = "ROLE_RESET";
    private final String PREFIX = "ROLE_";

    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret") String secretKey,
                            @Value("${jwt.expirationDateInMs}") long validityInMilliseconds,
                            @Value("${jwt.refreshExpirationDateInMs}") long refreshTokenValidiyInMilliseconds,
                            MyUserDetailsService myUserDetails) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
        this.myUserDetails = myUserDetails;
        this.refreshTokenValidiyInMilliseconds = refreshTokenValidiyInMilliseconds;
    }


    public String createToken(String username, String role) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", new SimpleGrantedAuthority(PREFIX + role));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
    }

    public String createRefreshToken(String username) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", new SimpleGrantedAuthority(ROLE_REFRESH));

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidiyInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
    }


    public String createResetToken(String username, String oldPassword) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", new SimpleGrantedAuthority(ROLE_RESET));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, oldPassword) // uso la vecchia password come secret key
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Authentication getRefreshTokenAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_REFRESH));
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }


    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("JWT token scaduto oppure non valido", HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean validateResetToken(String token, String password) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(password).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("JWT token scaduto oppure non valido", HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean isRefreshToken(String token){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            Claims claimsBody = claims.getBody();
            LinkedHashMap claimsMap = claimsBody.get("auth", LinkedHashMap.class);
            String role = (String) claimsMap.get("authority");
            return role.equalsIgnoreCase(ROLE_REFRESH) ? true : false;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("JWT token scaduto oppure non valido", HttpStatus.UNAUTHORIZED);
        }
    }






}
