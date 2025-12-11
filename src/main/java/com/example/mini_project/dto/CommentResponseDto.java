package com.example.mini_project.dto;

import lombok.Data;

@Data
public class CommentResponseDto {
	private Long id;
    private String name;
    private String content;
    private String created_at;
}
