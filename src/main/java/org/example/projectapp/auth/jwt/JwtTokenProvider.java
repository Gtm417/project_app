package org.example.projectapp.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final JwtConfig jwtConfig;

    public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, JwtConfig jwtConfig) {
        this.userDetailsService = userDetailsService;
        this.jwtConfig = jwtConfig;
    }

    @PostConstruct
    protected void init() {
        jwtConfig.setSecretKey(Base64.getEncoder().encodeToString(jwtConfig.getSecretKey().getBytes()));
    }

    public String createToken(String username, String role, boolean isRefreshToken) {
        Claims claims = Jwts.claims().setSubject(username);
        LocalDateTime now = LocalDateTime.now();
        Date validity;
        if (isRefreshToken) {
            LocalDateTime resultDate = now.plusDays(jwtConfig.getRefreshValidityInDays());
            validity = java.sql.Timestamp.valueOf(resultDate);
        } else {
            claims.put("role", role);
            LocalDateTime resultDate = now.plusHours(jwtConfig.getValidityInHours());
            validity = java.sql.Timestamp.valueOf(resultDate);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(java.sql.Timestamp.valueOf(now))
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        Jws<Claims> claimsJws = parseClaimsJws(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    private Jws<Claims> parseClaimsJws(String token) {
        return Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        Jws<Claims> claimsJws = parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body.getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (authHeader != null && authHeader.contains(jwtConfig.getTokenPrefix())) {
            return authHeader.replace(jwtConfig.getTokenPrefix(), "");
        }
        //todo add log Inconsist authHeader
        return null;
    }
}
