package com.app.services.impl;

import com.app.config.JwtService;
import com.app.dto.AuthenticationRequest;
import com.app.dto.AuthenticationResponse;
import com.app.entity.User;
import com.app.enums.Authority;
import com.app.enums.Country;
import com.app.repositories.UserRepository;
import com.app.services.AuthServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServices {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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

//            String jwtToken = jwtService.generateToken(savedUser);
//            AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
//                    .firstName(savedUser.getFirstName())
//                    .token(jwtToken)
//                    .build();

            AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                    .email(savedUser.getEmail())
                    .countryOfPermanentResidence(savedUser.getCountryOfResidence())
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .phoneNumber(savedUser.getPhoneNumber())
                    .role(savedUser.getAuthority())
                    .build();

            responsePayload.put("status", "success");
            responsePayload.put("message", "User created successfully");
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
        Map<String, Object> responsePayload = new HashMap<>();

        try {
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

        } catch (Exception e) {

            responsePayload.put("status", "failed");
            responsePayload.put("message", e.getMessage());
            responsePayload.put("data", null);

            return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
        }
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
            responsePayload.put("message", "User logged in successfully");
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
    public ResponseEntity<Map<String, Object>> forgotPassword(AuthenticationRequest request) {
        return null;
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
