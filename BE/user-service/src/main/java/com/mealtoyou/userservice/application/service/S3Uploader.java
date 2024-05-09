package com.mealtoyou.userservice.application.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class S3Uploader {

  private final AmazonS3 amazonS3;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucketName;

  private String generateFileName(String fileName) {
    return UUID.randomUUID() + "_" + fileName;
  }

  private String upload(String dir, ObjectMetadata metadata, String fileName, InputStream inputStream) {
    String fileDir = dir + "/" + generateFileName(fileName);
    PutObjectRequest request = new PutObjectRequest(bucketName, fileDir, inputStream, metadata);

    request.withCannedAcl(CannedAccessControlList.PublicRead);
    amazonS3.putObject(request);
    return amazonS3.getUrl(bucketName, fileDir).toString();
  }

  private Mono<String> uploadFilePart(String dir, FilePart file) {
    // 임시 파일 디렉토리 생성
    Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
    Path tempFilePath = tempDir.resolve(generateFileName(file.filename()));

    // FilePart의 내용을 임시 파일로 전송
    return file.transferTo(tempFilePath)
        .then(Mono.fromCallable(() -> {
          try (InputStream inputStream = Files.newInputStream(tempFilePath)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(Files.size(tempFilePath));
            metadata.setContentType(file.headers().getContentType().toString());
            return upload(dir, metadata, file.filename(), inputStream);
          } finally {
            try {
              Files.deleteIfExists(tempFilePath);
            } catch (IOException ignored) {
            }
          }
        }));
  }

  public Mono<String> upload(FilePart file) {
    return uploadFilePart("meal-to-you", file);
  }
}

