package com.example.mini_project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mini_project.dto.AnswerRequestDto;
import com.example.mini_project.dto.AnswerResponseDto;
import com.example.mini_project.service.AnswerService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(AnswerController.class)
@AutoConfigureMockMvc(addFilters = false)

public class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean  
    private AnswerService answerService;

    @Autowired
    private ObjectMapper objectMapper;

    // CREATE
    @Test
    void createAnswer_success() throws Exception {

        AnswerResponseDto response = AnswerResponseDto.builder()
                .id(1L)
                .content("테스트 답변")
                .userName("홍길동")
                .createdAt(LocalDateTime.now())
                .build();

        when(answerService.createAnswer(any())).thenReturn(response);

        AnswerRequestDto request = AnswerRequestDto.builder()
                .questionId(1L)
                .userId(1L)
                .content("테스트 답변")
                .build();

        mockMvc.perform(post("/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("테스트 답변"));
    }

    // GET 단일 조회
    @Test
    void getAnswer_success() throws Exception {

        AnswerResponseDto response = AnswerResponseDto.builder()
                .id(1L)
                .content("테스트 답변")
                .userName("홍길동")
                .createdAt(LocalDateTime.now())
                .build();

        when(answerService.getAnswer(1L)).thenReturn(response);

        mockMvc.perform(get("/answers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // GET 특정 질문의 답변 목록 조회
    @Test
    void getAnswersByQuestion_success() throws Exception {

        List<AnswerResponseDto> responses = Arrays.asList(
                AnswerResponseDto.builder().id(1L).content("답변1").userName("user1").createdAt(LocalDateTime.now()).build(),
                AnswerResponseDto.builder().id(2L).content("답변2").userName("user2").createdAt(LocalDateTime.now()).build()
        );

        when(answerService.getAnswersByQuestion(1L)).thenReturn(responses);

        mockMvc.perform(get("/answers/question/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("답변1"))
                .andExpect(jsonPath("$[1].content").value("답변2"));
    }

    // UPDATE
    @Test
    void updateAnswer_success() throws Exception {

        AnswerResponseDto response = AnswerResponseDto.builder()
                .id(1L)
                .content("수정된 답변")
                .userName("홍길동")
                .createdAt(LocalDateTime.now())
                .build();

        when(answerService.updateAnswer(eq(1L), any())).thenReturn(response);

        AnswerRequestDto req = AnswerRequestDto.builder()
                .questionId(1L)
                .userId(1L)
                .content("수정된 답변")
                .build();

        mockMvc.perform(put("/answers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 답변"));
    } 

    // DELETE
    @Test
    void deleteAnswer_success() throws Exception {

        mockMvc.perform(delete("/answers/1"))
                .andExpect(status().isNoContent());
    }
}
