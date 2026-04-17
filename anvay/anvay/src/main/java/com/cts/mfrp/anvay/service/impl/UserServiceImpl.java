package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.dto.LoginRequest;
import com.cts.mfrp.anvay.dto.LoginResponse;
import com.cts.mfrp.anvay.dto.RegisterStudentRequest;
import com.cts.mfrp.anvay.entity.Institution;
import com.cts.mfrp.anvay.entity.User;
import com.cts.mfrp.anvay.repository.InstitutionRepository;
import com.cts.mfrp.anvay.repository.UserRepository;
import com.cts.mfrp.anvay.security.JwtUtil;
import com.cts.mfrp.anvay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if ("student".equals(user.getRole()) && user.getInstitutionId() != null) {
            Institution institution = institutionRepository.findById(user.getInstitutionId()).orElse(null);
            if (institution != null && !"active".equals(institution.getStatus())) {
                throw new BadCredentialsException("Your institution has not been approved yet. Please wait for admin approval.");
            }
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getUserId(), user.getInstitutionId());

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole())
                .name(user.getFirstName())
                .userId(user.getUserId())
                .institutionId(user.getInstitutionId())
                .build();
    }

    @Override
    @Transactional
    public LoginResponse registerStudent(RegisterStudentRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (request.getInstitutionId() == null) {
            throw new RuntimeException("Please select an institution to register");
        }

        Institution institution = institutionRepository.findById(request.getInstitutionId())
                .orElseThrow(() -> new RuntimeException("Selected institution not found"));
        if (!"active".equals(institution.getStatus())) {
            throw new RuntimeException("Selected institution has not been approved yet");
        }

        User user = User.builder()
                .firstName(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("student")
                .institutionId(request.getInstitutionId())
                .build();

        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getUserId(), user.getInstitutionId());

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole())
                .name(user.getFirstName())
                .userId(user.getUserId())
                .institutionId(user.getInstitutionId())
                .build();
    }
}
