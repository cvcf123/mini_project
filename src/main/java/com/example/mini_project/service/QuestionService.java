package com.example.mini_project.service;

import com.example.mini_project.dto.question.QuestionDetailDto;
import com.example.mini_project.dto.question.QuestionDto;
import com.example.mini_project.dto.question.QuestionListDto;

import java.util.List;
public interface QuestionService {

    QuestionDetailDto createQuestion(Long userId, QuestionDto request);

    QuestionDetailDto getQuestion(Long questionId);

    List<QuestionListDto> getQuestions();

    List<QuestionListDto> searchQuestions(String keyword);

    List<QuestionListDto> getQuestionsByTag(Long tagId);

    List<QuestionListDto> searchQuestionsByTag(String keyword, Long tagId);

    QuestionDetailDto updateQuestion(Long questionId, Long userId, QuestionDto request);

    void deleteQuestion(Long questionId, Long userId);
}
