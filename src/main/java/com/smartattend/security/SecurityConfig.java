package com.smartattend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        // Public UI & Static Assets
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/admin/**",
                                "/faculty/**",
                                "/student/**",
                                "/biometric/**",
                                "/css/**",
                                "/js/**",
                                "/assets/**",
                                "/favicon.ico"
                        ).permitAll()
                        // Public API Endpoints (Auth, Docs, Actuator, WebSockets, Biometric Simulation)
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/biometric/events",
                                "/api/biometric/simulate",
                                "/api/biometric/stream/**",
                                "/api/biometric/live-counter/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/actuator/**",
                                "/h2-console/**",
                                "/ws/**"
                        ).permitAll()
                        // Role-based Access Rules
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/faculty/**").hasAnyRole("FACULTY", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/student/**", "/api/students/**").hasAnyRole("STUDENT", "FACULTY", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/attendance/sessions/**").hasAnyRole("FACULTY", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/attendance/override/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/attendance/**").hasAnyRole("FACULTY", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/reports/**").hasAnyRole("STUDENT", "FACULTY", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/subjects/**").hasAnyRole("STUDENT", "FACULTY", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/academic/**").hasAnyRole("STUDENT", "FACULTY", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/notifications/**").authenticated()
                        // Any other API requires authentication
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Auth-Token", "Origin", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
