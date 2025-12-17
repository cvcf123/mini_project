package com.example.mini_project.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponseDto {
	private Long id;
    private String content;
    private String name;     // 작성자 이름
    private Long userId;     // ⭐ 추가 (핵심)
    private LocalDateTime createdAt;
}