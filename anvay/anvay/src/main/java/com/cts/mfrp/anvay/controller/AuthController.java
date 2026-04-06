package com.cts.mfrp.anvay.controller;

import com.cts.mfrp.anvay.dto.LoginRequest;
import com.cts.mfrp.anvay.dto.LoginResponse;
import com.cts.mfrp.anvay.dto.RegisterInstitutionRequest;
import com.cts.mfrp.anvay.dto.RegisterStudentRequest;
import com.cts.mfrp.anvay.service.InstitutionService;
import com.cts.mfrp.anvay.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final InstitutionService institutionService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/register/institution")
    public ResponseEntity<?> registerInstitution(@Valid @RequestBody RegisterInstitutionRequest request) {
        try {
            LoginResponse response = institutionService.registerInstitution(request);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody RegisterStudentRequest request) {
        try {
            LoginResponse response = userService.registerStudent(request);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
