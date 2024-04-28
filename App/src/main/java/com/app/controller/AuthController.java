package com.app.controller;

import com.app.config.utils.EmailService;
import com.app.dto.AuthenticationRequest;
import com.app.services.AuthServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthServices authServices;
    private final EmailService emailService;

    @PostMapping("signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody AuthenticationRequest request) {

        return authServices.signup(request);
    }

    @PatchMapping("create-password")
    public ResponseEntity<Map<String, Object>> createPassword(@RequestBody AuthenticationRequest request) {

        return authServices.createPassword(request);
    }

    @PostMapping("login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthenticationRequest request) {

        return authServices.login(request);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody AuthenticationRequest request) {

        return authServices.sendResetPasswordMail(request);
    }

    @GetMapping("send-otp")
    public ResponseEntity sendOtp() {

        emailService.sendEmail("abdullahi@onepipe.io", "Test Email", "This is a test email");
//        emailService.sendEmailWithHtml("abdullahi@onepipe.io");

        return ResponseEntity.ok("Email sent successfully");
    }
}
