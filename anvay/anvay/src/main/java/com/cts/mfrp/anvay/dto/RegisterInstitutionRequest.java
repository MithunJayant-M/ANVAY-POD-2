package com.cts.mfrp.anvay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterInstitutionRequest {

    @NotBlank(message = "Institution name is required")
    @Size(min = 2, max = 100, message = "Institution name must be 2-100 characters")
    @Pattern(
        regexp = "^[A-Za-z][A-Za-z0-9\\s.&'()-]{1,99}$",
        message = "Institution name must start with a letter and may only contain letters, digits, spaces and . & ' ( ) -"
    )
    private String institutionName;

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

    @Pattern(
        regexp = "^[6-9][0-9]{9}$",
        message = "Phone must be a valid 10-digit Indian mobile number starting with 6-9"
    )
    private String phone;

    private String address;

    @NotBlank(message = "Admin name is required")
    @Size(min = 2, max = 50, message = "Admin name must be 2-50 characters")
    @Pattern(
        regexp = "^[A-Za-z][A-Za-z\\s.'-]{1,49}$",
        message = "Admin name must start with a letter and may only contain letters, spaces and . ' -"
    )
    private String adminName;
}
