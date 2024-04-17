package com.app.dto.requestDto;

import com.app.enums.Gender;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String nationality;
    private Gender gender;
}
