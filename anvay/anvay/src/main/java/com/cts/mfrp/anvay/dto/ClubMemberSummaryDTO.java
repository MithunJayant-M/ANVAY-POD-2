package com.cts.mfrp.anvay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Returned by the club members / join-requests endpoints. Preserves the
 * shape the frontend already reads — {@code m.user?.firstName}, etc. — so
 * no template changes are needed.
 *
 * Built by mapping {@link com.cts.mfrp.anvay.entity.ClubMember} → DTO
 * inside the service's @Transactional boundary, so the User is fetched
 * via the existing LEFT JOIN FETCH and never lazy-loaded at serialization.
 */
public record ClubMemberSummaryDTO(
        Long id,
        Long clubId,
        Long userId,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        MemberUserDTO user
) {}
