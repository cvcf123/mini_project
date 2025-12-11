package com.example.mini_project.controller;

import com.example.mini_project.config.MyUserDetails;
import com.example.mini_project.dto.question.QuestionDetailDto;
import com.example.mini_project.dto.question.QuestionDto;
import com.example.mini_project.dto.question.QuestionListDto;
import com.example.mini_project.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private QuestionService questionService;

    private MyUserDetails userDetails;
    private QuestionDetailDto questionDetailDto;
    private QuestionListDto questionListDto;

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

        questionDetailDto = new QuestionDetailDto(
            1L,
            "테스트 질문",
            "테스트 내용",
            "테스트유저",
            LocalDateTime.now(),
            null,
            Arrays.asList("Java", "Spring")
        );

        questionListDto = new QuestionListDto(
            1L,
            "테스트 질문",
            "테스트유저",
            LocalDateTime.now(),
            Arrays.asList("Java", "Spring")
        );
    }

    @Test
    @DisplayName("질문 생성 성공")
    void createQuestion_Success() throws Exception {
        // given
        given(questionService.createQuestion(eq(1L), any(QuestionDto.class)))
            .willReturn(questionDetailDto);

        String requestBody = """
                {
                    "title": "테스트 질문",
                    "content": "테스트 내용",
                    "tagIds": [1, 2]
                }
                """;

        // when & then
        mockMvc.perform(post("/questions")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("테스트 질문"))
            .andExpect(jsonPath("$.content").value("테스트 내용"))
            .andExpect(jsonPath("$.writerNickname").value("테스트유저"));
    }

    @Test
    @DisplayName("질문 상세 조회 성공")
    void getQuestion_Success() throws Exception {
        // given
        given(questionService.getQuestion(1L)).willReturn(questionDetailDto);

        // when & then
        mockMvc.perform(get("/questions/1")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("테스트 질문"))
            .andExpect(jsonPath("$.content").value("테스트 내용"));
    }

    @Test
    @DisplayName("질문 상세 조회 실패 - 존재하지 않는 질문")
    void getQuestion_NotFound() throws Exception {
        // given
        given(questionService.getQuestion(999L))
            .willThrow(new IllegalArgumentException("존재하지 않는 질문입니다."));

        // when & then
        mockMvc.perform(get("/questions/999")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("질문 목록 조회 성공")
    void getQuestions_Success() throws Exception {
        // given
        List<QuestionListDto> questions = Arrays.asList(questionListDto);
        given(questionService.getQuestions()).willReturn(questions);

        // when & then
        mockMvc.perform(get("/questions")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("테스트 질문"));
    }

    @Test
    @DisplayName("질문 수정 성공")
    void updateQuestion_Success() throws Exception {
        // given
        QuestionDetailDto updatedDto = new QuestionDetailDto(
            1L,
            "수정된 질문",
            "수정된 내용",
            "테스트유저",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Arrays.asList("Java")
        );
        given(questionService.updateQuestion(eq(1L), eq(1L), any(QuestionDto.class)))
            .willReturn(updatedDto);

        String requestBody = """
                {
                    "title": "수정된 질문",
                    "content": "수정된 내용",
                    "tagIds": [1]
                }
                """;

        // when & then
        mockMvc.perform(put("/questions/1")
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("수정된 질문"))
            .andExpect(jsonPath("$.content").value("수정된 내용"));
    }

    @Test
    @DisplayName("질문 수정 실패 - 작성자가 아닌 경우")
    void updateQuestion_NotWriter() throws Exception {
        // given
        given(questionService.updateQuestion(eq(1L), eq(1L), any(QuestionDto.class)))
            .willThrow(new IllegalArgumentException("작성자만 수정/삭제할 수 있습니다."));

        String requestBody = """
                {
                    "title": "수정된 질문",
                    "content": "수정된 내용",
                    "tagIds": [1]
                }
                """;

        // when & then
        mockMvc.perform(put("/questions/1")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("질문 삭제 성공")
    void deleteQuestion_Success() throws Exception {
        // given
        doNothing().when(questionService).deleteQuestion(1L, 1L);

        // when & then
        mockMvc.perform(delete("/questions/1")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("질문 삭제 실패 - 작성자가 아닌 경우")
    void deleteQuestion_NotWriter() throws Exception {
        // given
        doThrow(new IllegalArgumentException("작성자만 수정/삭제할 수 있습니다."))
            .when(questionService).deleteQuestion(1L, 1L);

        // when & then
        mockMvc.perform(delete("/questions/1")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증되지 않은 사용자의 질문 생성 요청")
    void createQuestion_Unauthorized() throws Exception {
        String requestBody = """
                {
                    "title": "테스트 질문",
                    "content": "테스트 내용",
                    "tagIds": [1]
                }
                """;

        // when & then
        mockMvc.perform(post("/questions")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }
}