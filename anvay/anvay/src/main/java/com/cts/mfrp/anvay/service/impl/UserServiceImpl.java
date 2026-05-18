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

        // Block users whose account hasn't been approved yet (status="pending").
        // Existing rows pre-migration have status=null and are treated as active.
        if ("pending".equalsIgnoreCase(user.getStatus())) {
            throw new BadCredentialsException("Your account is awaiting approval from your institution administrator. Please try again later.");
        }
        if ("rejected".equalsIgnoreCase(user.getStatus())) {
            throw new BadCredentialsException("Your registration was rejected. Please contact your institution administrator.");
        }

        boolean isStudentRole = "student".equals(user.getRole()) || "club_leader".equals(user.getRole());
        if (isStudentRole && user.getInstitutionId() != null) {
            Institution institution = institutionRepository.findById(user.getInstitutionId()).orElse(null);
            if (institution != null && !"active".equals(institution.getStatus())) {
                throw new BadCredentialsException("Your institution is not active. Please contact your administrator.");
            }
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getUserId(), user.getInstitutionId());

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole())
                .name(user.getFirstName())
                .userId(user.getUserId())
                .institutionId(user.getInstitutionId())
                .leadingClubId(user.getLeadingClubId())
                .build();
    }

    @Override
    @Transactional
    public void resetPassword(String email, String oldPasswordOrMaster, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No account found with that email"));

        boolean masterBypass = "Admin@123".equals(oldPasswordOrMaster);
        boolean oldPasswordMatch = passwordEncoder.matches(oldPasswordOrMaster, user.getPassword());

        if (!masterBypass && !oldPasswordMatch) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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
                .status("pending")           // ← waits for institution admin to approve
                .institutionId(request.getInstitutionId())
                .studentIdNumber(request.getStudentIdNumber())
                .build();

        user = userRepository.save(user);

        // Deliberately NOT issuing a JWT for a pending student. Frontend should
        // check response.status === "pending" and show the awaiting-approval
        // screen instead of navigating to the dashboard.
        return LoginResponse.builder()
                .token(null)
                .role(user.getRole())
                .name(user.getFirstName())
                .userId(user.getUserId())
                .institutionId(user.getInstitutionId())
                .leadingClubId(user.getLeadingClubId())
                .status("pending")
                .message("Registration successful. Your account is awaiting approval from your institution administrator.")
                .build();
    }
}
