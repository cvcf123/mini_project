package com.example.mini_project.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.mini_project.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import com.example.mini_project.dto.AnswerResponseDto;
import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Question;
import com.example.mini_project.repository.AnswerRepository;
import com.example.mini_project.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;


    @Override
    public List<AnswerResponseDto> getAnswersByQuestion(Long questionId) {

        // 질문 조회
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문이 존재하지 않습니다."));


        // 해당 질문에 달린 답변 조회
        List<Answer> answers = answerRepository.findByQuestionOrderByCreatedAtAsc(question);

        // DTO 변환
        return answers.stream()
                .map(answer -> AnswerResponseDto.builder()
                        .id(answer.getId())
                        .content(answer.getContent())
                        .userName(answer.getUser().getName())
                        .createdAt(answer.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());
    }
}