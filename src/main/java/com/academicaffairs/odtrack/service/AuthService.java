package com.academicaffairs.odtrack.service;

import com.academicaffairs.odtrack.payload.JwtAuthResponse;
import com.academicaffairs.odtrack.payload.LoginDto;
import com.academicaffairs.odtrack.payload.StudentLoginDto;
import com.academicaffairs.odtrack.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthResponse loginStaff(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication.getName());
        return new JwtAuthResponse(token, "Bearer");
    }

    public JwtAuthResponse loginStudent(StudentLoginDto studentLoginDto) {
        // For students, we are authenticating based on register number and DOB.
        // The CustomUserDetailsService will handle loading the user by register number.
        // Since students don't have a password in the traditional sense, we'll pass an empty string
        // or a placeholder for the password in the UsernamePasswordAuthenticationToken.
        // The actual DOB verification should happen within CustomUserDetailsService or here if needed.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(studentLoginDto.getRegisterNumber(), studentLoginDto.getDob())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication.getName());
        return new JwtAuthResponse(token, "Bearer");
    }
}
