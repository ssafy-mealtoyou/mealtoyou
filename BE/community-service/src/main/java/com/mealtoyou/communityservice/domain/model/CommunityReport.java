package com.mealtoyou.communityservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("community_group")
@Getter
@Builder
public class CommunityReport {

    @Id
    @Column("community_report_id")
    private Long communityReportId;

    @Column("user_id")
    private Long userId;

    @Column("group_id")
    private Long groupId;

    private ReportType type;

    private String desc;

}