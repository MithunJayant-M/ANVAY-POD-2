package com.cts.mfrp.anvay.dto;

/**
 * Nested DTO used inside {@link ClubMemberSummaryDTO}. Carries only the
 * User fields the club-members table needs — no profilePicture, no password,
 * no lazy collections. Safe to serialize outside a Hibernate session.
 */
public record MemberUserDTO(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String role,
        Long leadingClubId
) {}
