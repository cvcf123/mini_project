package com.example.mini_project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;
import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Comment;
import com.example.mini_project.entity.User;
import com.example.mini_project.repository.AnswerRepository;
import com.example.mini_project.repository.CommentRepository;
import com.example.mini_project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;
    private final AnswerRepository answerRepo;
    private final UserRepository userRepo;

    @Override
    public CommentResponseDto createComment(CommentRequestDto dto) {

        Answer answer = answerRepo.findById(dto.getAnswerId())
                .orElseThrow(() -> new IllegalArgumentException("답변 없음"));

        // 로그인 유저 기반으로 1명 고정이라면 dto에 userId 추가 필요
        User user = userRepo.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .answer(answer)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        Comment saved = commentRepo.save(comment);
        return toDto(saved);
    }

    @Override
    public List<CommentResponseDto> getComments(Long answerId) {

        Answer answer = answerRepo.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변 없음"));

        return commentRepo.findByAnswerOrderByCreatedAtAsc(answer)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto updateComment(Long id, CommentRequestDto dto) {

        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        comment.setContent(dto.getContent());
        return toDto(commentRepo.save(comment));
    }

    @Override
    public void deleteComment(Long id) {
        commentRepo.deleteById(id);
    }

    private CommentResponseDto toDto(Comment comment) {
    	return CommentResponseDto.builder()
    	        .id(comment.getId())
    	        .content(comment.getContent())
    	        .name(comment.getUser().getName())
    	        .userId(comment.getUser().getId())   // ⭐ 핵심
    	        .createdAt(comment.getCreatedAt())
    	        .build();

    }
}