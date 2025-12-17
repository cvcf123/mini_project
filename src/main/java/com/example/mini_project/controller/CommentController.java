package com.example.mini_project.controller;

import java.util.List;

import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;
import com.example.mini_project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
/*
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto) {
        CommentResponseDto response = commentService.createComment(dto);
        return ResponseEntity.ok(response);
    }

    // 특정 Answer에 달린 댓글 조회
    @GetMapping("/answer/{answerId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long answerId) {
        List<CommentResponseDto> comments = commentService.getComments(answerId);
        return ResponseEntity.ok(comments);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto dto) {

        CommentResponseDto response = commentService.updateComment(id, dto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {

        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
    
}
*/
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestBody CommentRequestDto dto) {

        return ResponseEntity.ok(commentService.createComment(dto));
    }

    // 특정 답변의 댓글 목록
    @GetMapping("/answer/{answerId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @PathVariable Long answerId) {

        return ResponseEntity.ok(commentService.getComments(answerId));
    }

    // ✅ 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto dto) {

        return ResponseEntity.ok(commentService.updateComment(id, dto));
    }

    // ✅ 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {

        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

