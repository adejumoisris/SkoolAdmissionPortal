package com.app.service.serviceInterface;

import com.app.dto.requestDto.SignUpRequest;
import com.app.dto.responseDto.AppResponse;

public interface UserService {

    public AppResponse SignUp(SignUpRequest signUpRequest);
}
