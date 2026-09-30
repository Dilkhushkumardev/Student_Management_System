package com.smartattend.service;

import com.smartattend.dto.AuthDto.*;
import com.smartattend.entity.RefreshToken;
import com.smartattend.entity.User;
import com.smartattend.exception.BadRequestException;
import com.smartattend.exception.ResourceNotFoundException;
import com.smartattend.repository.FacultyRepository;
import com.smartattend.repository.RefreshTokenRepository;
import com.smartattend.repository.StudentRepository;
import com.smartattend.repository.UserRepository;
import com.smartattend.security.CustomUserDetails;
import com.smartattend.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Value("${smartattend.jwt.refresh-expiration-ms:604800000}")
    private Long refreshTokenDurationMs;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       StudentRepository studentRepository,
                       FacultyRepository facultyRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       JwtUtils jwtUtils,
                       PasswordEncoder passwordEncoder,
                       AuditService auditService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    @Transactional
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userDetails.getId()));

        RefreshToken refreshToken = createRefreshToken(user);

        // Resolve student or faculty metadata if applicable
        Long studentId = null;
        Long facultyId = null;
        String rollNo = null;
        String regNo = null;
        String deptName = null;
        String secName = null;

        var optStudent = studentRepository.findByUserId(user.getId());
        if (optStudent.isPresent()) {
            var s = optStudent.get();
            studentId = s.getId();
            rollNo = s.getRollNo();
            regNo = s.getUniversityRegNo();
            if (s.getDepartment() != null) deptName = s.getDepartment().getName();
            if (s.getSection() != null) secName = s.getSection().getName();
        }

        var optFaculty = facultyRepository.findByUserId(user.getId());
        if (optFaculty.isPresent()) {
            var f = optFaculty.get();
            facultyId = f.getId();
            if (f.getDepartment() != null) deptName = f.getDepartment().getName();
        }

        auditService.log("LOGIN", "User", user.getId().toString(), null, "SUCCESS", null,
                "User " + user.getUsername() + " logged in successfully.");

        return JwtResponse.builder()
                .token(jwt)
                .type("Bearer")
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .fullName(userDetails.getFullName())
                .email(userDetails.getEmail())
                .roles(roles)
                .studentId(studentId)
                .facultyId(facultyId)
                .rollNo(rollNo)
                .registrationNo(regNo)
                .departmentName(deptName)
                .sectionName(secName)
                .build();
    }

    @Transactional
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return TokenRefreshResponse.builder()
                            .accessToken(token)
                            .refreshToken(requestRefreshToken)
                            .tokenType("Bearer")
                            .build();
                })
                .orElseThrow(() -> new BadRequestException("Refresh token is not in database or expired", "INVALID_REFRESH_TOKEN"));
    }

    private RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0 || (token.getRevoked() != null && token.getRevoked())) {
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token was expired. Please make a new sign in request", "EXPIRED_REFRESH_TOKEN");
        }
        return token;
    }

    @Transactional
    public void logout(Long userId) {
        userRepository.findById(userId).ifPresent(refreshTokenRepository::deleteByUser);
        auditService.log("LOGOUT", "User", userId.toString(), null, "SUCCESS", null, "User logged out");
    }
}
