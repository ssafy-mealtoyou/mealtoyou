package com.mealtoyou.communityservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_group")
@Getter
@Builder
public class UserCommunity {

    @Id
    @Column("user_community_id")
    private Long userCommunityId;

    @Column("user_id")
    private Long userId;

    @Column("community_id")
    private Long communityId;

}