package com.cts.mfrp.anvay.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFeedDTO {
    private Long eventId;
    private String title;
    private String location;
    private String institution;
    private Long institutionId;
    private String type;
    private String participantType;
    private Integer registeredCount;
    private Integer totalCapacity;
    private Boolean isRegistered;
}
