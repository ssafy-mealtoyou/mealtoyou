package com.mealtoyou.userservice.infrastructure.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mealtoyou.userservice.domain.model.User;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OIDCDto {

	private String iss;
	private String aud;

	@NotNull(message = "sub 반드시")
	private String sub;
	private long iat;
	private long exp;
	private String nonce;
	private String givenName;
	private String picture;
	private String email;

	public User toEntity() {
		return User.builder()
			.email(this.email)
			.nickname(this.givenName)
			.socialKey("google")
			.userImageUrl(this.picture)
			.role("USER")
			.build();
	}
}
