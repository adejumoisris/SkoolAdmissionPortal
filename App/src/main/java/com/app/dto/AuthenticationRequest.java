package com.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class AuthenticationRequest {
    private String firstName;
    private String lastName;
    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @Size(min = 11, message = "Phone number must be at least 11 characters long")
    private String phoneNumber;
    private String countryOfPermanentResidence;
    private String role;
    private String passwordResetToken;
}
