package com.eulji.clubon.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/*.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/auth/email-verifications/send",
                                "/api/auth/email-verifications/confirm",
                                "/api/auth/tokens/refresh",
                                "/api/auth/logout",
                                "/api/auth/login-id/find",
                                "/api/auth/password-resets/**",
                                "/error"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/departments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/clubs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/clubs/*").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("SCHOOL_ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/inquiries").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/clubs/*/bookmarks").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/clubs/*/bookmarks").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/users/me/bookmarks").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/clubs/*/application-form").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/clubs/*/applications").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/users/me/applications").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/applications/*").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/club-creation-requests").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/club-creation-requests/my").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/club-creation-requests/*").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/clubs/*/dashboard").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/clubs/*/members").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/clubs/*/activity-logs").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/clubs/*/members/*/role").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/clubs/*/members/*").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/clubs").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/clubs/*").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/clubs/*/records").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/clubs/*/record-images/presigned-url").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/clubs/*/records/*").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/clubs/*/records/*").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/clubs/*/applications").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/applications/*/status").hasRole("CLUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/me/posts").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/me/reviews").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/notifications").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/notifications/unread-count").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/notifications/*/read").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/notifications/read-all").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/me/clubs").hasAnyRole("STUDENT", "CLUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/clubs/*/reviews").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/clubs/*/reviews").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/clubs/*/reviews/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/clubs/*/reviews/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/clubs/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/clubs/*/records", "/api/clubs/*/records/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/inquiries/my").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
