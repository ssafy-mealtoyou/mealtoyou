package com.mealtoyou.foodservice.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchConfig extends ReactiveElasticsearchConfiguration {
	@Value("${spring.data.elasticsearch.url}")
	private String url;
	@Value("${spring.data.elasticsearch.username}")
	private String username;
	@Value("${spring.data.elasticsearch.password}")
	private String password;

	@Override
	public ClientConfiguration clientConfiguration() {
		return ClientConfiguration.builder()
			.connectedTo(url)
			.withBasicAuth(username, password)
			.build();
	}
}
