package com.example.mini_project.controller;

import com.example.mini_project.config.MyUserDetails;
import com.example.mini_project.dto.question.QuestionDetailDto;
import com.example.mini_project.dto.question.QuestionDto;
import com.example.mini_project.dto.question.QuestionListDto;
import com.example.mini_project.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionDetailDto> createQuestion(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestBody QuestionDto request) {
        QuestionDetailDto response = questionService.createQuestion(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDetailDto> getQuestion(@PathVariable Long questionId) {
        QuestionDetailDto response = questionService.getQuestion(questionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<QuestionListDto>> getQuestions(
            @RequestParam(value = "q", required = false) String keyword) {
        List<QuestionListDto> response = (keyword == null || keyword.isBlank())
                ? questionService.getQuestions()
                : questionService.searchQuestions(keyword.trim());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionDetailDto> updateQuestion(
            @PathVariable Long questionId,
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestBody QuestionDto request) {
        QuestionDetailDto response = questionService.updateQuestion(questionId, userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long questionId,
            @AuthenticationPrincipal MyUserDetails userDetails) {
        questionService.deleteQuestion(questionId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
