package com.mealtoyou.healthservice.application.service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private static final String SECRET_KEY = "YourSecretKey123";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 90 * 24 * 60 * 60 * 1000L;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getUserId(String token) {
        if (token.startsWith(BEARER_TYPE)) {
            token = token.substring(7);
        }
        try {
            return Long.parseLong(
                    decrypt(Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String encrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey key = new SecretKeySpec(JwtTokenProvider.SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String decrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey key = new SecretKeySpec(JwtTokenProvider.SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(input));
        return new String(decryptedBytes);
    }

//    public boolean existsRefreshToken(String refreshToken) {
//        Optional<RefreshToken> token = refreshTokenRepository.findById(refreshToken);
//        return token.isPresent();
//    }

    public Mono<Void> setHeaderAccessToken(ServerHttpResponse response, String accessToken) {
        response.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return Mono.empty();
    }
}