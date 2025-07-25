package com.academicaffairs.odtrack.controller;

import com.academicaffairs.odtrack.payload.JwtAuthResponse;
import com.academicaffairs.odtrack.payload.LoginDto;
import com.academicaffairs.odtrack.payload.StudentLoginDto;
import com.academicaffairs.odtrack.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/staff")
    public ResponseEntity<JwtAuthResponse> authenticateStaff(@RequestBody LoginDto loginDto) {
        JwtAuthResponse jwtAuthResponse = authService.loginStaff(loginDto);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/student")
    public ResponseEntity<JwtAuthResponse> authenticateStudent(@RequestBody StudentLoginDto studentLoginDto) {
        JwtAuthResponse jwtAuthResponse = authService.loginStudent(studentLoginDto);
        return ResponseEntity.ok(jwtAuthResponse);
    }
}
