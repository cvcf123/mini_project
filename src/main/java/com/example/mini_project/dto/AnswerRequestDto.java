package com.example.mini_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequestDto {
    private Long questionId;   // 어느 질문의 답변인지
    private Long userId;       // 작성자
    private String content;    // 답변 내용
}