package com.example.mini_project.service;

import com.example.mini_project.dto.UserDto;
import com.example.mini_project.dto.UserResultDto;

public interface UserService {
    UserResultDto insertUser(UserDto userDto);
}
