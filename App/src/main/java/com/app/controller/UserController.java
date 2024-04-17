package com.app.controller;

import com.app.dto.requestDto.SignUpRequest;
import com.app.dto.responseDto.AppResponse;
import com.app.service.serviceInterface.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    @PostMapping("/signup")
    public AppResponse SignUp(@RequestBody @Valid SignUpRequest signUpRequest){
        return userService.SignUp(signUpRequest);

    }

    public String getUser() {
        return "User";
    }
}
