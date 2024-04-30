package com.app.services.impl;

import com.app.config.JwtService;
import com.app.config.utils.EmailService;
import com.app.dto.AuthenticationRequest;
import com.app.dto.AuthenticationResponse;
import com.app.entity.User;
import com.app.enums.Authority;
import com.app.enums.Country;
import com.app.repositories.UserRepository;
import com.app.services.AuthServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServices {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${logo.url}")
    private String logoUrl;

    private static final Long PASSWORD_TOKEN_VALIDITY = (long) (10 * 60 * 1000);
    private static final String EXPIRED_TOKEN = "Token is expired. Please try again.";
    private static final String INVALID_TOKEN = "Invalid Token";
    private static final String VALID_TOKEN = "Valid Token";

    @Override
    public ResponseEntity<Map<String, Object>> signup(AuthenticationRequest request) {
        Map<String, Object> responsePayload = new HashMap<>();

        try {
            Optional<User> userByEmail = userRepository.findFirstByEmail(request.getEmail());
            if (userByEmail.isPresent()) {
                responsePayload.put("status", "failed");
                responsePayload.put("message", "Email already exists");
                responsePayload.put("data", null);

                return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
            }

            Optional<User> userByPhoneNumber = userRepository.findFirstByPhoneNumber(request.getPhoneNumber());
            if (userByPhoneNumber.isPresent()) {
                responsePayload.put("status", "failed");
                responsePayload.put("message", "Phone number already exists");
                responsePayload.put("data", null);

                return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
            }

            if (!isValidCountry(request)) {
                responsePayload.put("status", "failed");
                responsePayload.put("message", "Invalid country");
                responsePayload.put("data", null);

                return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
            }

            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .authority(Authority.ROLE_USER.getAuthority())
                    .countryOfResidence(request.getCountryOfPermanentResidence())
                    .build();

            User savedUser = userRepository.save(user);

            AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                    .email(savedUser.getEmail())
                    .countryOfPermanentResidence(savedUser.getCountryOfResidence())
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .phoneNumber(savedUser.getPhoneNumber())
                    .role(savedUser.getAuthority())
                    .build();

            responsePayload.put("status", "success");
            responsePayload.put("message", "Account created successfully");
            responsePayload.put("data", authenticationRequest);

            return new ResponseEntity<>(responsePayload, HttpStatus.CREATED);

        } catch (Exception e) {

            responsePayload.put("status", "failed");
            responsePayload.put("message", e.getMessage());
            responsePayload.put("data", null);

            return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<Map<String, Object>> createPassword(AuthenticationRequest request) {
        log.info("Payload: {}", request);
        Map<String, Object> responsePayload = new HashMap<>();

        try {

            if (request.getPasswordResetToken() != null) {
                log.info("Password reset request received with token: {}", request.getPasswordResetToken());

                String validation = validateResetPasswordToken(request.getPasswordResetToken());

                if (validation.equalsIgnoreCase(INVALID_TOKEN)) {

                    responsePayload.put("status", "failed");
                    responsePayload.put("message", validation);
                    responsePayload.put("data", null);

                    return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
                }

                if (validation.equalsIgnoreCase(EXPIRED_TOKEN)) {

                    responsePayload.put("status", "failed");
                    responsePayload.put("message", validation);
                    responsePayload.put("data", null);

                    return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
                }

                if (validation.equalsIgnoreCase(VALID_TOKEN)) {
                    log.info("Request valid. Proceeding with password reset.");

                    Optional<User> optionalUser = userRepository.findFirstByPasswordResetToken(request.getPasswordResetToken());

                    User user = optionalUser.get();

                    log.info("{} created new pin", user.getEmail());

                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setPasswordResetToken(null);
                    userRepository.save(user);

                    log.info("Password reset successful for {}", user.getEmail());

                    responsePayload.put("status", "success");
                    responsePayload.put("message", "Pin reset successful.");
                    responsePayload.put("data", null);

                    return new ResponseEntity<>(responsePayload, HttpStatus.OK);
                }

            } else {
                Optional<User> userByEmail = userRepository.findFirstByEmail(request.getEmail());
                if (userByEmail.isEmpty()) {
                    responsePayload.put("status", "failed");
                    responsePayload.put("message", "Email already exists");
                    responsePayload.put("data", null);

                    return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
                }

                log.info("{} is creating password", request.getEmail());

                User user = userByEmail.get();
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                userRepository.save(user);

                responsePayload.put("status", "success");
                responsePayload.put("message", "Password created successfully");
                responsePayload.put("data", null);

                return new ResponseEntity<>(responsePayload, HttpStatus.OK);
            }

        } catch (Exception e) {

            responsePayload.put("status", "failed");
            responsePayload.put("message", e.getMessage());
            responsePayload.put("data", null);

            return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
        }

        responsePayload.put("status", "failed");
        responsePayload.put("message", "Invalid request");
        responsePayload.put("data", null);

        return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> login(AuthenticationRequest request) {

        Map<String, Object> responsePayload = new HashMap<>();

        try {

            Optional<User> userByEmail = userRepository.findFirstByEmail(request.getEmail());

            if (userByEmail.isEmpty()) {

                responsePayload.put("status", "failed");
                responsePayload.put("message", "Invalid Email or Password");
                responsePayload.put("data", null);

                return new ResponseEntity<>(responsePayload, HttpStatus.UNAUTHORIZED);
            }

            User user = userByEmail.get();

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

                responsePayload.put("status", "failed");
                responsePayload.put("message", "Invalid Email or Password");
                responsePayload.put("data", null);

                return new ResponseEntity<>(responsePayload, HttpStatus.UNAUTHORIZED);
            }

            String jwtToken = jwtService.generateToken(user);

            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getAuthority())
                    .token(jwtToken)
                    .build();

            responsePayload.put("status", "success");
            responsePayload.put("message", "Log in successful");
            responsePayload.put("data", authenticationResponse);

            return new ResponseEntity<>(responsePayload, HttpStatus.OK);

        } catch (Exception e) {

            responsePayload.put("status", "failed");
            responsePayload.put("message", e.getMessage());
            responsePayload.put("data", null);

            return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> sendResetPasswordMail(AuthenticationRequest request) {

        Map<String, Object> responsePayload = new HashMap<>();

        try {

            Optional<User> userByEmail = userRepository.findFirstByEmail(request.getEmail());
            if (userByEmail.isEmpty()) {
                responsePayload.put("status", "failed");
                responsePayload.put("message", "Email not found");
                responsePayload.put("data", null);

                return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
            }

            User user = userByEmail.get();
            String token = UUID.randomUUID().toString();

            user.setPasswordResetToken(token);
            user.setPasswordResetTokenTimeStamp(new Date());
            userRepository.save(user);

            String resetLink = "http://localhost:3000/create-password/" + token;
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
                    "<button style=\"padding:15px; background:#248232; border-radius:20px; border:none; font-size:20px;\"><a style=\"text-decoration:none; color:white;\" href=\"" + resetLink + "\">Reset Password</a></button>" +
                    "</div>" +
                    "</div>" +
                    "</body></html>";

            emailService.sendEmailWithHtml(request.getEmail(), subject, htmlBody);

            responsePayload.put("status", "success");
            responsePayload.put("message", "Reset password mail sent successfully.");
            responsePayload.put("data", null);

            return new ResponseEntity<>(responsePayload, HttpStatus.OK);

        } catch (Exception e) {

            responsePayload.put("status", "failed");
            responsePayload.put("message", e.getMessage());
            responsePayload.put("data", null);

            return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String validateResetPasswordToken(String resetPasswordToken) {

        if (Strings.isBlank(resetPasswordToken)) {

            return INVALID_TOKEN;
        }

        Optional<User> optionalUser = userRepository.findFirstByPasswordResetToken(resetPasswordToken);

        if (optionalUser.isEmpty()) {

            return INVALID_TOKEN;
        }

        User user = optionalUser.get();

        Date passwordResetTokenTimeStamp = user.getPasswordResetTokenTimeStamp();
        Date currentTime = new Date();
        long timeDifference = currentTime.getTime() - passwordResetTokenTimeStamp.getTime();

        if (timeDifference > PASSWORD_TOKEN_VALIDITY) {

            log.info("{} reset password token expired", user.getEmail());

            return EXPIRED_TOKEN;
        }

        return VALID_TOKEN;

    }

    public boolean isValidCountry(AuthenticationRequest request) {
        try {
            Country.valueOf(request.getCountryOfPermanentResidence().toUpperCase().replace(" ", "_"));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
