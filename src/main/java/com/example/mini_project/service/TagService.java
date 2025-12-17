package com.example.mini_project.service;

import com.example.mini_project.dto.TagResponseDto;

import java.util.List;

public interface TagService {
    List<TagResponseDto> getTags();
}

