package com.example.mini_project.config;

import java.util.List;

import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;

public interface CommentService {
	CommentResponseDto createComment(CommentRequestDto dto);
    List<CommentResponseDto> getComments(Long answerId);
}
