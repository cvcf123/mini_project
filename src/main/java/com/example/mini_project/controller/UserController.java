package com.example.mini_project.controller;

import com.example.mini_project.config.MyUserDetails;
import com.example.mini_project.dto.UserDto;
import com.example.mini_project.dto.UserResultDto;
import com.example.mini_project.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("") // post /users
    public UserResultDto insertUser(UserDto userDto) { // 원래 UserDto 를 받아서 UserService 에 전달하는 게 올바른 방법.
        return userService.insertUser(userDto);
    }

    @GetMapping("/info")
    public UserDto infoUser(@AuthenticationPrincipal MyUserDetails userDetails) {
        return UserDto.builder()
                .id(userDetails.getId())
                .name(userDetails.getName())
                .email(userDetails.getEmail())
                .createdAt(userDetails.getCreatedAt())
                .build();
    }
}
