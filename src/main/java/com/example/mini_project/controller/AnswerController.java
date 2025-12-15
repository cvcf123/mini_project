package com.example.mini_project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mini_project.dto.AnswerRequestDto;
import com.example.mini_project.dto.AnswerResponseDto;
import com.example.mini_project.service.AnswerService;

import lombok.RequiredArgsConstructor;
/*
//특정 질문에 달린 답변 목록 조회 API 제공
@RestController
@RequestMapping("/answers")     
@RequiredArgsConstructor
public class AnswerController {
	private final AnswerService answerService;

    // 특정 질문에 달린 답변 목록 조회
    @GetMapping("/{questionId}/answers")
    public ResponseEntity<List<AnswerResponseDto>> getAnswersByQuestion1(
            @PathVariable Long questionId) {

        List<AnswerResponseDto> answers = answerService.getAnswersByQuestion(questionId);
        return ResponseEntity.ok(answers);
    }
    @PostMapping
    public ResponseEntity<AnswerResponseDto> createAnswer(
            @RequestBody AnswerRequestDto dto) {
        AnswerResponseDto response = answerService.createAnswer(dto);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponseDto> getAnswer(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswer(id));
    }
    
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerResponseDto>> getAnswersByQuestion(
            @PathVariable Long questionId) {
        return ResponseEntity.ok(answerService.getAnswersByQuestion(questionId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponseDto> updateAnswer(
            @PathVariable Long id,
            @RequestBody AnswerRequestDto dto) {

        return ResponseEntity.ok(answerService.updateAnswer(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }
}*/
@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    // 답변 생성
    @PostMapping
    public ResponseEntity<AnswerResponseDto> createAnswer(
            @RequestBody AnswerRequestDto dto) {

        return ResponseEntity.ok(answerService.createAnswer(dto));
    }

    // 특정 질문의 답변 목록 조회 (프론트에서 사용)
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerResponseDto>> getAnswersByQuestion(
            @PathVariable Long questionId) {

        return ResponseEntity.ok(answerService.getAnswersByQuestion(questionId));
    }

    // 답변 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponseDto> getAnswer(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswer(id));
    }

    // ✅ 답변 수정
    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponseDto> updateAnswer(
            @PathVariable Long id,
            @RequestBody AnswerRequestDto dto) {

        return ResponseEntity.ok(answerService.updateAnswer(id, dto));
    }

    // ✅ 답변 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }
}
