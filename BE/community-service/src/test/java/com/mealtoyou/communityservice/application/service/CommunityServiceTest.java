package com.mealtoyou.communityservice.application.service;

import com.mealtoyou.communityservice.domain.model.Community;
import com.mealtoyou.communityservice.domain.repository.CommunityRepository;
import com.mealtoyou.communityservice.domain.repository.UserCommunityRepository;
import com.mealtoyou.communityservice.presentation.request.CreateCommunityRequest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommunityServiceTest {

    @Test
    public void testCreateCommunity() {
        // Create a mock of the CommunityRepository
        CommunityRepository communityRepository = mock(CommunityRepository.class);
        UserCommunityRepository userCommunityRepository = mock(UserCommunityRepository.class);

        // Create an instance of the CommunityService with the mock repository
        CommunityService communityService = new CommunityService(communityRepository, userCommunityRepository);

        // Create a dummy CreateCommunityDto and user ID
        CreateCommunityRequest createCommunityRequest = new CreateCommunityRequest(
                "제목",
                30,
                100,
                110,
                3
        );
        Long userId = 1L;

        // Create a dummy Community object to return from the repository
        Community savedCommunity = Community.builder()
                .leaderId(userId)
                .title(createCommunityRequest.title())
                .cntUsers(1)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(createCommunityRequest.period()))
                .dailyGoalCalories(createCommunityRequest.dailyGoalCalories())
                .dailyGoalSteps(createCommunityRequest.dailyGoalSteps())
                .weeklyMinGoal(createCommunityRequest.weeklyMinGoal())
                .build();

        // Mock the repository's save method to return the dummy Community
        when(communityRepository.save(any(Community.class))).thenReturn(Mono.just(savedCommunity));

        // Call the method to be tested
        Mono<Community> result = communityService.createCommunity(createCommunityRequest, userId);

        // Assert that the result matches the expected Community
        result.subscribe(community -> {
            // Assert the community properties here
            assert community.getCommunityId() != null;
            assert community.getLeaderId().equals(userId);
            assert community.getTitle().equals(createCommunityRequest.title());
            // Add more assertions for other properties if needed
        });
    }
}
