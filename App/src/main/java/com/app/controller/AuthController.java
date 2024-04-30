package com.app.controller;

import com.app.config.utils.EmailService;
import com.app.dto.AuthenticationRequest;
import com.app.services.AuthServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${logo.url}")
    private String logoUrl;

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

    @PatchMapping("forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody AuthenticationRequest request) {
        System.out.println(request);

        return authServices.sendResetPasswordMail(request);
    }

    @GetMapping("send-otp")
    public ResponseEntity sendOtp() {

//        emailService.sendEmail("abdullahi@onepipe.io", "Test Email", "This is a test email");

        System.out.println(logoUrl);
        String resetLink = "http://localhost:3000/create-password/" + "12345";
        String subject = "Reset Password";
        String htmlBody = "<html><body>" +
                "<div style=\"align-items:center\">" +
                "<img height=\"200px\" src=\"logoUrl\" alt=\"My logo\"/>" +
                "<h1 style=\"text-align:center;\">Request for Password Reset.</h1>" +
                "<p style=\"font-size:18px; text-align:center\">" +
                "You recently requested to reset your password for your account. Please click the link below to reset it, Reset link remains valid for 10 Minutes. Kindly ignore this email if you didn't request for a password reset." +
                "</p>" +
                "<br>" +
                "<div style=\"margin: 2% 35%\">" +
                "<button style=\"padding:15px; background:#248232; border-radius:20px; border:none; font-size:20px;\"><a style=\"text-decoration:none; color:white;\" href=\"resetLink\">Reset Password</a></button>" +
                "</div>" +
                "</div>" +
                "</body></html>";
        emailService.sendEmailWithHtml("abdullahi@onepipe.io", subject, htmlBody);

        return ResponseEntity.ok("Email sent successfully");
    }
}
