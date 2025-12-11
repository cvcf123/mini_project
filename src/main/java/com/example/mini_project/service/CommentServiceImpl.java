package com.example.mini_project.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mini_project.config.CommentService;
import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;
import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Comment;
import com.example.mini_project.entity.User;
import com.example.mini_project.repository.CommentRepository;
import com.example.mini_project.repository.UserRepository;
import com.example.mini_project.repository.UserRepository.AnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponseDto createComment(CommentRequestDto dto) {

        // 1) 현재 로그인된 유저 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = auth.getName();

        User loginUser = userRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2) 댓글이 달린 Answer 조회
        Answer answer = answerRepository.findById(dto.getAnswerId())
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        // 3) Comment 엔티티 생성
        Comment comment = Comment.builder()
                .content(dto.getContent())
                .created_at(LocalDateTime.now())
                .user(loginUser)
                .answer(answer)
                .build();

        // 4) 저장
        Comment saved = commentRepository.save(comment);

        // 5) DTO 변환 후 반환
        return CommentResponseDto.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .userName(saved.getUser().getUser())
                .createdAt(saved.getCreatedAt().toString())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long answerId) {

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        return commentRepository.findByAnswerOrderByCreatedAtAsc(answer)
                .stream()
                .map(c -> CommentResponseDto.builder()
                        .id(c.getId())
                        .content(c.getContent())
                        .userName(c.getUser().getUsername())
                        .createdAt(c.getCreatedAt().toString())
                        .build()
                ).toList();
    }
}
