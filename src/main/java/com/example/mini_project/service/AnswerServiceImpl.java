package com.example.mini_project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mini_project.dto.AnswerRequestDto;
import com.example.mini_project.dto.AnswerResponseDto;
import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Question;
import com.example.mini_project.entity.User;
import com.example.mini_project.repository.AnswerRepository;
import com.example.mini_project.repository.QuestionRepository;
import com.example.mini_project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepo;
    private final QuestionRepository questionRepo;
    private final UserRepository userRepo;

    @Override
    public AnswerResponseDto createAnswer(AnswerRequestDto dto) {

        Question question = questionRepo.findById(dto.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("질문 없음"));

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Answer answer = Answer.builder()
                .content(dto.getContent())
                .question(question)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        Answer saved = answerRepo.save(answer);
        return toDto(saved);
    }

    @Override
    public AnswerResponseDto getAnswer(Long answerId) {
        Answer answer = answerRepo.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변 없음"));
        return toDto(answer);
    }

    @Override
    public AnswerResponseDto updateAnswer(Long answerId, AnswerRequestDto dto) {

        Answer answer = answerRepo.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변 없음"));

        answer.setContent(dto.getContent());
        return toDto(answerRepo.save(answer));
    }

    @Override
    public void deleteAnswer(Long answerId) {
        answerRepo.deleteById(answerId);
    }

    @Override
    public List<AnswerResponseDto> getAnswersByQuestion(Long questionId) {

        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문 없음"));

        return answerRepo.findByQuestionOrderByCreatedAtAsc(question)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AnswerResponseDto toDto(Answer answer) {
        return AnswerResponseDto.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .userName(answer.getUser().getName()) // 중요
                .createdAt(answer.getCreatedAt())
                .build();
    }
}
