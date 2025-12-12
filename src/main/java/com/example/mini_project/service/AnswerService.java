package com.example.mini_project.service;

import com.example.mini_project.dto.AnswerRequestDto;
import com.example.mini_project.dto.AnswerResponseDto;

import java.util.List;

public interface AnswerService {

    AnswerResponseDto createAnswer(AnswerRequestDto dto);

    AnswerResponseDto getAnswer(Long answerId);

    AnswerResponseDto updateAnswer(Long answerId, AnswerRequestDto dto);

    void deleteAnswer(Long answerId);

    List<AnswerResponseDto> getAnswersByQuestion(Long questionId);
}
