package org.codingtext.admin.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24L; //액세스토큰 유효시간: 24 시간

    @Value("${spring.jwt.secret}")
    private String secret;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createToken(long id, String email) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (SignatureException e) {
            log.error("Invalid JWT signature, signature 가 유효하지 않은 토큰 입니다.");
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.error("Invalid JWT token, 유효하지 않은 jwt 토큰 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token. 만료된 jwt 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}