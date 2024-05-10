package com.mealtoyou.communityservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("community_diet")
@Getter
@Builder
public class CommunityDiet {

    @Id
    @Column("community_diet_id")
    private Long communityDietId;

    @Column("community_id")
    private Long communityId;

    @Column("diet_id")
    private Long dietId;

}
