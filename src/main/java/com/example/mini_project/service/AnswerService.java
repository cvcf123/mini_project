package com.example.mini_project.service;

import com.example.mini_project.dto.AnswerResponseDto;

import java.util.List;

public interface AnswerService {

    List<AnswerResponseDto> getAnswersByQuestion(Long questionId);
}
