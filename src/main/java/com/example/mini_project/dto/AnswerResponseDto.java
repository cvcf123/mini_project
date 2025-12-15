package com.example.mini_project.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponseDto {
    private Long id;
    private String content;
    private String userName;
    private Long userId;   // ⭐ 추가
    private LocalDateTime createdAt;
}
