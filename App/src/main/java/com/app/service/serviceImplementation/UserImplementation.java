package com.app.service.serviceImplementation;

import com.app.dto.requestDto.SignUpRequest;
import com.app.dto.responseDto.AppResponse;
import com.app.dto.responseDto.UserResponse;
import com.app.entity.User;
import com.app.repositories.UserRepository;
import com.app.service.serviceInterface.UserService;
import com.app.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserImplementation implements UserService {

    private final UserRepository userRepository;
    @Override
    public AppResponse SignUp(SignUpRequest signUpRequest) {


        User newUser = User.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .nationality(signUpRequest.getNationality())
                .build();

        User saveUser = userRepository.save(newUser);
        return  AppResponse.builder()
                .responseCode(AppUtils.SIGNUP_CREATED_SUCCESSFULLY)
                .responseMessage(AppUtils.SIGNUP_CREATION_MESSAGE)
                .userResponse(UserResponse.builder()
                        .firstName(saveUser.getFirstName())
                        .email(saveUser.getEmail())
                        .build())

                .build();
    }
}
