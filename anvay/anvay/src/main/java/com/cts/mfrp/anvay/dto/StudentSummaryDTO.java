package com.cts.mfrp.anvay.dto;

/**
 * Lightweight projection of {@link com.cts.mfrp.anvay.entity.User} for student
 * list / leaderboard views. Explicitly omits:
 *   - {@code password} (security)
 *   - {@code profilePicture} (LONGTEXT base64 — the biggest contributor to
 *     payload bloat on large student lists)
 *
 * The canonical record constructor below MUST match the field order in
 * {@code SELECT new ...StudentSummaryDTO(...)} JPQL queries.
 */
public record StudentSummaryDTO(
        Long userId,
        Long institutionId,
        String email,
        String firstName,
        String lastName,
        String role,
        Integer totalPoints,
        Integer rankInLeaderboard,
        Integer registeredEventsCount,
        Integer joinedClubsCount,
        Long leadingClubId,
        String studentIdNumber
) {}
