package com.cts.mfrp.anvay.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Bean
    public OpenAPI anvayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Anvay Platform API")
                        .description("""
                                **Anvay** — Multi-Tenant Campus Event & Club Management System

                                *Pod 2 | Cognizant MFRP Project*

                                This API powers three distinct roles:
                                - **Student** — browse/register for events, join clubs, apply for leadership
                                - **Institution Admin** — create events, manage clubs and members
                                - **Super Admin** — approve institutions, view analytics, manage the entire platform

                                Authenticate via `POST /api/auth/login` to obtain a JWT, then click **Authorize** and paste `Bearer <token>`.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pod 2 Team")
                                .email("pod2@anvay.io"))
                        .license(new License()
                                .name("Internal — Cognizant MFRP")
                                .url("#")))
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Local development")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("BearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Paste the JWT token returned by POST /api/auth/login")));
    }

    // ── Student-facing APIs ────────────────────────────────────────────────────
    @Bean
    public GroupedOpenApi studentApis() {
        return GroupedOpenApi.builder()
                .group("1-Student-API")
                .displayName("Student API")
                .pathsToMatch(
                        "/api/students/**",
                        "/api/events/feed",
                        "/api/events/my-registrations",
                        "/api/events/register",
                        "/api/events/{eventId}",
                        "/api/clubs/user/**",
                        "/api/clubs/institution/**",
                        "/api/clubs/{clubId}/join",
                        "/api/clubs/{clubId}/members/approved",
                        "/api/leadership-applications",
                        "/api/leadership-applications/user/**",
                        "/api/achievements/**",
                        "/api/chat"
                )
                .build();
    }

    // ── Institution Admin APIs ─────────────────────────────────────────────────
    @Bean
    public GroupedOpenApi institutionApis() {
        return GroupedOpenApi.builder()
                .group("2-Institution-API")
                .displayName("Institution API")
                .pathsToMatch(
                        "/api/events/**",
                        "/api/clubs/**",
                        "/api/leadership-applications/**",
                        "/api/members/**",
                        "/api/institutions/{institutionId}",
                        "/api/institutions/leaderboard"
                )
                .build();
    }

    // ── Super Admin APIs ───────────────────────────────────────────────────────
    @Bean
    public GroupedOpenApi adminApis() {
        return GroupedOpenApi.builder()
                .group("3-Admin-API")
                .displayName("Super Admin API")
                .pathsToMatch(
                        "/api/super-admin/**",
                        "/api/institutions/**",
                        "/api/auth/**"
                )
                .build();
    }

    // ── All APIs (default view) ────────────────────────────────────────────────
    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder()
                .group("0-All-APIs")
                .displayName("All APIs")
                .pathsToMatch("/api/**")
                .build();
    }
}
