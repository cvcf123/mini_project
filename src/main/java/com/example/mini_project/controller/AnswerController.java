package com.example.mini_project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mini_project.dto.AnswerResponseDto;
import com.example.mini_project.service.AnswerService;

import lombok.RequiredArgsConstructor;

//특정 질문에 달린 답변 목록 조회 API 제공
@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class AnswerController {
	private final AnswerService answerService;

    // 특정 질문에 달린 답변 목록 조회
    @GetMapping("/{questionId}/answers")
    public ResponseEntity<List<AnswerResponseDto>> getAnswersByQuestion(
            @PathVariable Long questionId) {

        List<AnswerResponseDto> answers = answerService.getAnswersByQuestion(questionId);
        return ResponseEntity.ok(answers);
    }
}
