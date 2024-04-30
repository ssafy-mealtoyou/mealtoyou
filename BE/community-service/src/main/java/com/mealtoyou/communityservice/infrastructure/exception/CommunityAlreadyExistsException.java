package com.mealtoyou.communityservice.infrastructure.exception;

public class CommunityAlreadyExistsException extends RuntimeException {
    public CommunityAlreadyExistsException(String message) {
        super(message);
    }
}