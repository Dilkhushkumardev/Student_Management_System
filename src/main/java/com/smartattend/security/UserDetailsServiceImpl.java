package com.smartattend.security;

import com.smartattend.entity.User;
import com.smartattend.repository.StudentRepository;
import com.smartattend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new UsernameNotFoundException("Username/Identifier cannot be empty");
        }

        final String cleanId = identifier.trim();
        final String normalizedRoll = cleanId.replaceAll("[\\s\\-_]", "").toUpperCase();

        User user = userRepository.findByUsername(cleanId)
                .or(() -> userRepository.findByEmail(cleanId))
                .or(() -> userRepository.findByUsername(normalizedRoll))
                .or(() -> studentRepository.findByRollNo(cleanId).map(s -> s.getUser()))
                .or(() -> studentRepository.findByRollNo(normalizedRoll).map(s -> s.getUser()))
                .or(() -> studentRepository.findByUniversityRegNo(cleanId).map(s -> s.getUser()))
                .or(() -> studentRepository.findByBiometricId(cleanId).map(s -> s.getUser()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + identifier));

        return CustomUserDetails.build(user);
    }
}
