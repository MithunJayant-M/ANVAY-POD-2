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
    private String type;
    private Integer registeredCount;
    private Integer totalCapacity;
    private Boolean isRegistered;
}