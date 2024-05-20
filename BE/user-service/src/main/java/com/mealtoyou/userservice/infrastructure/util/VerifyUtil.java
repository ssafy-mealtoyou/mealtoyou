package com.mealtoyou.userservice.infrastructure.util;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component //유틸이지만 @Value를 주입받기 위해 사용
public class VerifyUtil {

	private static String staticGoogleIss1;
	private static String staticGoogleIss2;
	private static String staticGoogleAud;
	private static JwkProvider staticGoogleJwkProvider;

	// @Value("${google.iss1}")
	private String googleIss1 = "https://accounts.google.com";
	// @Value("${google.iss2}")
	private String googleIss2 = "accounts.google.com";
	@Value("${google.clientId}")
	private String googleAud;
	// @Value("${google.jwks}")
	private String googleJwks = "https://www.googleapis.com/oauth2/v3/certs";

	public static OIDCDto googleIdTokenVerify(String idToken) {
		try {
			DecodedJWT decodedJWT = JWT.decode(idToken);
			OIDCDto decodedIdToken = payloadDecoder(decodedJWT.getPayload());
			if (!decodedIdToken.getIss().equals(staticGoogleIss1) && !decodedIdToken.getIss()
				.equals(staticGoogleIss2)) {
				return null;
			}
			Jwk jwk = staticGoogleJwkProvider.get(decodedJWT.getKeyId()); //kid로 가져오기
			Algorithm algorithm = Algorithm.RSA256((RSAPublicKey)jwk.getPublicKey(), null);
			JWTVerifier verifier = JWT.require(algorithm).withAudience(staticGoogleAud).build();
			DecodedJWT jwt = verifier.verify(idToken);
			return decodedIdToken;
		} catch (JwkException je) {
			return null;
		}
	}

	@PostConstruct
	private void init() {
		try {
			staticGoogleIss1 = googleIss1;
			staticGoogleIss2 = googleIss2;
			staticGoogleAud = googleAud;

			staticGoogleJwkProvider = new JwkProviderBuilder(new URL(googleJwks))
				.cached(10, 30, TimeUnit.DAYS) // 30일간 최대 10개 캐시
				.build();
		} catch (Exception e) {

		}
	}

	public static OIDCDto payloadDecoder(String jwtPayload) {
		try {
			String payload = new String(Base64.getUrlDecoder().decode(jwtPayload));
			ObjectMapper mapper = new ObjectMapper().configure(
				DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			return mapper.readValue(payload, OIDCDto.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException();
		}
	}

}
