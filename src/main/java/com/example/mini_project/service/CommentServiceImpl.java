package com.example.mini_project.service;

import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;
import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Comment;
import com.example.mini_project.entity.User;
import com.example.mini_project.repository.AnswerRepository;
import com.example.mini_project.repository.CommentRepository;
import com.example.mini_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponseDto createComment(CommentRequestDto dto) {

        //  1. Answer 조회
        Answer answer = answerRepository.findById(dto.getAnswerId())
                .orElseThrow(() -> new IllegalArgumentException("해당 답변이 존재하지 않습니다."));

        //  2. 현재 로그인 유저 조회 → 너의 UserService 구조에 따라 나중에 수정 가능
        // 지금은 테스트 용으로 user_id = 1 고정
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        //  3. Comment 엔티티 생성
        Comment comment = Comment.builder()
                .content(dto.getContent())
                .user(user)
                .answer(answer)
                .createdAt(LocalDateTime.now())
                .build();

        Comment saved = commentRepository.save(comment);

        //  4. CommentResponseDto 로 변환하여 반환
        return CommentResponseDto.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .name(saved.getUser().getName())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public List<CommentResponseDto> getComments(Long answerId) {

        //  Answer 조회
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 답변이 존재하지 않습니다."));

        // 댓글 리스트 조회 (created_at ASC 기준)
        List<Comment> commentList = commentRepository.findByAnswerOrderByCreatedAtAsc(answer);

        return commentList.stream()
                .map(comment -> CommentResponseDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .name(comment.getUser().getName())
                        .createdAt(comment.getCreatedAt())   
                        .build()
                )
                .collect(Collectors.toList());
    }
}