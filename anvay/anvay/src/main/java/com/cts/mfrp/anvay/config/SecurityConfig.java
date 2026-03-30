package com.cts.mfrp.anvay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Critical for POST/PUT/DELETE
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Disables all security checks
                );
        return http.build();
    }
}