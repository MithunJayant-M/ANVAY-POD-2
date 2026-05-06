package com.cts.mfrp.anvay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChatRequest {

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 2000, message = "Message too long")
    private String message;

    private Long userId;
    private Long institutionId;
}
