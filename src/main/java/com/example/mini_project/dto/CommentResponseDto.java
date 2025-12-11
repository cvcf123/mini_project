package com.example.mini_project.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponseDto {
	private Long id;
    private String name;
    private String content;
    private LocalDateTime createdAt;
}
