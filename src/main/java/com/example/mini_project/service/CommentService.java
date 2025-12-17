package com.example.mini_project.service;

import java.util.List;

import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;

public interface CommentService {

    CommentResponseDto createComment(CommentRequestDto dto);

    List<CommentResponseDto> getComments(Long answerId);

    CommentResponseDto updateComment(Long id, CommentRequestDto dto);

    void deleteComment(Long id);
}
