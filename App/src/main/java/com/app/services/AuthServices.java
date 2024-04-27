package com.app.services;

import com.app.dto.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthServices {

    ResponseEntity<Map<String, Object>> signup(AuthenticationRequest request);

    ResponseEntity<Map<String, Object>> createPassword(AuthenticationRequest request);

    ResponseEntity<Map<String, Object>> login(AuthenticationRequest request);

    ResponseEntity<Map<String, Object>> forgotPassword(AuthenticationRequest request);
}
