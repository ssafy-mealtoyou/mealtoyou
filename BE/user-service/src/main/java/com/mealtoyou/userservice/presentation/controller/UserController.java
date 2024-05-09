package com.mealtoyou.userservice.presentation.controller;

import com.mealtoyou.userservice.application.dto.request.UserInfoRequestDto;
import com.mealtoyou.userservice.application.dto.response.UserInfoResponseDto;
import com.mealtoyou.userservice.application.service.JwtTokenProvider;
import com.mealtoyou.userservice.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    private long getUserId(String token) {
        return jwtTokenProvider.getUserId(token);
    }

    @GetMapping("/users/profile")
    public Mono<UserInfoResponseDto> getUserProfile(@RequestHeader("Authorization") String token) {
        return userService.getUserProfile(getUserId(token));
    }

    @PostMapping(value = "/users/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<UserInfoResponseDto> updateUserProfile(
        @RequestHeader("Authorization") String token,
        @RequestPart(value = "userInfoRequestDto",required = false) UserInfoRequestDto userInfoRequestDto,
        @RequestPart(value = "image",required = false) FilePart image) {
        return userService.updateUserProfile(getUserId(token), image, userInfoRequestDto);
    }

}
