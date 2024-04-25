package com.mealtoyou.userservice.domain.repository;

import com.mealtoyou.userservice.application.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}