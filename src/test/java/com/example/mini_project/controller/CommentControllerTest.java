package com.example.mini_project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.mini_project.config.MyUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.mini_project.service.CommentService;
import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    private MyUserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = MyUserDetails.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .name("테스트유저")
                .email("test@test.com")
                .authorities(Collections.emptyList())
                .createdAt(LocalDateTime.now())
                .build();
    }
        @Test
    void createComment_success() throws Exception {
        CommentResponseDto response = CommentResponseDto.builder()
                .id(1L)
                .name("tester")
                .content("테스트 댓글")
                .createdAt(LocalDateTime.now())
                .build();

        when(commentService.createComment(any(CommentRequestDto.class)))
                .thenReturn(response);

        String jsonRequest = """
                {
                  "answerId": 1,
                  "content": "테스트 댓글"
                }
                """;

        mockMvc.perform(
        	    post("/comments")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        .with(csrf())
        	        .contentType(org.springframework.http.MediaType.APPLICATION_JSON) // 패키지 명시
        	        .content(jsonRequest)
        	)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("tester"))
                .andExpect(jsonPath("$.content").value("테스트 댓글"));
    }

    @Test
    void getComments_success() throws Exception {

        List<CommentResponseDto> list = Arrays.asList(
                CommentResponseDto.builder()
                        .id(1L)
                        .name("tester")
                        .content("댓글 내용1")
                        .createdAt(LocalDateTime.now())
                        .build(),

                CommentResponseDto.builder()
                        .id(2L)
                        .name("tester2")
                        .content("댓글 내용2")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        when(commentService.getComments(1L)).thenReturn(list);

        mockMvc.perform(get("/comments/answer/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("댓글 내용1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].content").value("댓글 내용2"));
    }
}
