package com.cts.mfrp.anvay.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for displaying club information in the dashboard.
 * Used for "Your Clubs" data table UI (US16P2_12).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubDashboardDTO {
    /**
     * Unique identifier for the club
     */
    private Long clubId;

    /**
     * Name of the club
     */
    private String clubName;

    /**
     * Type/Category of the club (badge value for frontend)
     */
    private String type;

    /**
     * Total number of members in the club
     */
    private Long membersCount;

    /**
     * Number of pending join requests (ClubMember where status = 'PENDING')
     */
    private Long joinRequestsCount;

    /**
     * Number of pending leadership applications
     */
    private Long leadershipAppsCount;

    /**
     * Club creation date
     */
    private LocalDateTime createdDate;
}
