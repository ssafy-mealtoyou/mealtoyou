package com.mealtoyou.communityservice.infrastructure.exception;

public class EmptyCommunityException extends RuntimeException {
    public EmptyCommunityException(String message) {
        super(message);
    }
}