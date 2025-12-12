package com.example.mini_project.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.mini_project.dto.AnswerResponseDto;
import com.example.mini_project.service.AnswerService;

@WebMvcTest(AnswerController.class)
public class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnswerService answerService;

    @Test
    void getAnswersByQuestion_success() throws Exception {
        List<AnswerResponseDto> responses = Arrays.asList(
                AnswerResponseDto.builder()
                        .id(1L)
                        .content("첫 번째 답변")
                        .userName("tester")
                        .createdAt(LocalDateTime.now())
                        .build(),
                AnswerResponseDto.builder()
                        .id(2L)
                        .content("두 번째 답변")
                        .userName("tester2")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        when(answerService.getAnswersByQuestion(1L)).thenReturn(responses);

        mockMvc.perform(get("/answers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("첫 번째 답변"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].content").value("두 번째 답변"));
    }
}