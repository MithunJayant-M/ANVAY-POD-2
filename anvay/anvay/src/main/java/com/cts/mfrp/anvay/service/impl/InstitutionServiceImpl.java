package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.dto.LoginResponse;
import com.cts.mfrp.anvay.dto.RegisterInstitutionRequest;
import com.cts.mfrp.anvay.entity.Institution;
import com.cts.mfrp.anvay.entity.User;
import com.cts.mfrp.anvay.repository.InstitutionRepository;
import com.cts.mfrp.anvay.repository.UserRepository;
import com.cts.mfrp.anvay.security.JwtUtil;
import com.cts.mfrp.anvay.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public LoginResponse registerInstitution(RegisterInstitutionRequest request) {
        if (institutionRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Institution email already registered");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Create institution (pending approval by super admin)
        Institution institution = Institution.builder()
                .institutionName(request.getInstitutionName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .status("pending")
                .build();
        institution = institutionRepository.save(institution);

        // Create institution admin user
        User adminUser = User.builder()
                .name(request.getAdminName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("institution")
                .institutionId(institution.getInstitutionId())
                .build();
        adminUser = userRepository.save(adminUser);

        String token = jwtUtil.generateToken(
                adminUser.getEmail(), adminUser.getRole(),
                adminUser.getUserId(), adminUser.getInstitutionId()
        );

        return LoginResponse.builder()
                .token(token)
                .role(adminUser.getRole())
                .name(adminUser.getName())
                .userId(adminUser.getUserId())
                .institutionId(adminUser.getInstitutionId())
                .build();
    }
}
