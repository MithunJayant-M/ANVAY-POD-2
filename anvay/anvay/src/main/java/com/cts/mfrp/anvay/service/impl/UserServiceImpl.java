package com.cts.mfrp.anvay.service.impl;

import com.cts.mfrp.anvay.dto.LoginRequest;
import com.cts.mfrp.anvay.dto.LoginResponse;
import com.cts.mfrp.anvay.dto.RegisterStudentRequest;
import com.cts.mfrp.anvay.entity.User;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getUserId(), user.getInstitutionId());

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole())
                .name(user.getName())
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

        User user = User.builder()
                .name(request.getName())
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
                .name(user.getName())
                .userId(user.getUserId())
                .institutionId(user.getInstitutionId())
                .build();
    }
}
