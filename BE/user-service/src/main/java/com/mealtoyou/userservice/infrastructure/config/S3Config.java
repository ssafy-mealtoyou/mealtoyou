package com.mealtoyou.userservice.infrastructure.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
  @Value("${spring.cloud.aws.credentials.access-key}")
  private String accessKey;
  @Value("${spring.cloud.aws.credentials.secret-key}")
  private String secretKey;


  @Bean
  public AmazonS3 amazonS3() {
    String regionName = "kr-standard";
    String endPoint = "https://kr.object.ncloudstorage.com";
    AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
        .build();

    return amazonS3;
  }
}
