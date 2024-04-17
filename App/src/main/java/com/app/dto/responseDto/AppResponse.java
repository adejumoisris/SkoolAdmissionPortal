package com.app.dto.responseDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppResponse {
    private String responseCode;
    private String responseMessage;
    private UserResponse userResponse;
}
