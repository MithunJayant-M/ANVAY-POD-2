package com.cts.mfrp.anvay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;        // null when account is pending — no auth granted
    private String role;
    private String name;
    private Long userId;
    private Long institutionId;
    private Long leadingClubId;
    private String status;       // "pending" / "active" / null (legacy = active)
    private String message;      // human-readable note (e.g., "Awaiting approval")
}
