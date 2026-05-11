package com.cts.mfrp.anvay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Lightweight projection of {@link com.cts.mfrp.anvay.entity.Event} for list
 * views. Deliberately omits the heavy {@code imageData} (LONGTEXT base64) and
 * {@code eventRules} (up to 2 KB each) fields.
 *
 * The canonical record constructor below MUST match the field order in
 * {@code SELECT new ...EventSummaryDTO(...)} JPQL queries.
 */
public record EventSummaryDTO(
        Long eventId,
        Long clubId,
        String eventName,
        String category,
        String location,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endDate,
        String status,
        Integer maxParticipants,
        Float registrationFee,
        String participantType,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime registrationDeadline
) {}
