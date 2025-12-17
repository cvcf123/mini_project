package com.example.mini_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.mini_project.entity.User;
import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;
import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Comment;
import com.example.mini_project.repository.AnswerRepository;
import com.example.mini_project.repository.CommentRepository;
import com.example.mini_project.repository.UserRepository;

public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    public CommentServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createComment_success() {

        // given
        CommentRequestDto request = CommentRequestDto.builder()
                .answerId(1L)
                .content("테스트 댓글")
                .userId(1L)
                .build();

        // mock User
        User user = User.builder()
                .id(1L)
                .name("tester")
                .email("test@test.com")
                .password("1234")
                .createdAt(LocalDateTime.now())
                .build();

        // mock Answer
        Answer answer = Answer.builder()
                .id(1L)
                .content("답변 내용")
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        // mock Saved Comment
        Comment saved = Comment.builder()
                .id(10L)
                .content("테스트 댓글")
                .user(user)
                .answer(answer)
                .createdAt(LocalDateTime.now())
                .build();

        // mocking repository behavior
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(saved);

        // when
        CommentResponseDto response = commentService.createComment(request);

        // then
        assertEquals(10L, response.getId());
        assertEquals("테스트 댓글", response.getContent());
        assertEquals("tester", response.getName());
    }
}