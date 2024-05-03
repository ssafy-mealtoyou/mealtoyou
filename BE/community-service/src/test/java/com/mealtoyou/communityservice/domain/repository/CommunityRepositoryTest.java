package com.mealtoyou.communityservice.domain.repository;

import com.mealtoyou.communityservice.domain.model.Community;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@DataR2dbcTest
class CommunityRepositoryTest {

    @Autowired
    private CommunityRepository communityRepository;

    @Test
    @DisplayName("그룹 생성")
    void insertCommunity() {
        Community community = Community.builder()
                .leaderId(1L)
                .title("Group 1")
                .cntUsers(1)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .dailyGoalCalories(300)
                .dailyGoalSteps(400)
                .weeklyMinGoal(3)
                .build();

        // Insert the group into the repository using custom query method
        Mono<Community> communityMono = communityRepository.save(community);

        // Verify the saved group
        StepVerifier.create(communityMono)
                .expectNextCount(1)
                .verifyComplete();
    }
}
