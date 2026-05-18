package com.cts.mfrp.anvay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterStudentRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
    @Pattern(
        regexp = "^[A-Za-z][A-Za-z\\s.'-]{1,49}$",
        message = "Name must start with a letter and may only contain letters, spaces and . ' - (no @, digits, or symbols)"
    )
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@(gmail\\.com|anvay\\.com|anvay\\.in)$",
        message = "Email must end with @gmail.com, @anvay.com, or @anvay.in"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be 6-64 characters")
    private String password;

    @NotNull(message = "Please select an institution")
    private Long institutionId;

    @NotBlank(message = "Student ID is required")
    @Pattern(
        regexp = "^\\d{2,4}[A-Za-z]{2,5}\\d{2,5}$",
        message = "Student ID must follow the format like 22IT161 (year + department code + roll number, e.g. 21CSE042)"
    )
    private String studentIdNumber;
}
