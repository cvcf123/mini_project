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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/*import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.example.mini_project.service.CommentService;
import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

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

        mockMvc.perform(get("/comments/answer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("댓글 내용1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].content").value("댓글 내용2"));
    }
}*/


import com.example.mini_project.dto.CommentRequestDto;
import com.example.mini_project.dto.CommentResponseDto;
import com.example.mini_project.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(CommentController.class)


public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean  
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    // CREATE
    @Test
    void createComment_success() throws Exception {

        CommentResponseDto response = CommentResponseDto.builder()
                .id(1L)
                .name("홍길동")
                .content("테스트 댓글")
                .createdAt(LocalDateTime.now())
                .build();

        when(commentService.createComment(any())).thenReturn(response);

        CommentRequestDto request = CommentRequestDto.builder()
                .answerId(1L)
                .content("테스트 댓글")
                .build();

        mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("테스트 댓글"))
                .andExpect(jsonPath("$.name").value("홍길동"));
    }

    // READ — 특정 Answer의 댓글 목록 조회
    @Test
    void getComments_success() throws Exception {

        List<CommentResponseDto> responses = Arrays.asList(
                CommentResponseDto.builder()
                        .id(1L).name("user1").content("댓글1").createdAt(LocalDateTime.now()).build(),
                CommentResponseDto.builder()
                        .id(2L).name("user2").content("댓글2").createdAt(LocalDateTime.now()).build()
        );

        when(commentService.getComments(1L)).thenReturn(responses);

        mockMvc.perform(get("/comments/answer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("댓글1"))
                .andExpect(jsonPath("$[1].content").value("댓글2"));
    }

    // UPDATE
    @Test
    void updateComment_success() throws Exception {

        CommentResponseDto response = CommentResponseDto.builder()
                .id(1L)
                .name("홍길동")
                .content("수정된 댓글")
                .createdAt(LocalDateTime.now())
                .build();

        when(commentService.updateComment(eq(1L), any())).thenReturn(response);

        CommentRequestDto request = CommentRequestDto.builder()
                .answerId(1L)
                .content("수정된 댓글")
                .build();

        mockMvc.perform(put("/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 댓글"));
    }

    // DELETE
    @Test
    void deleteComment_success() throws Exception {

        mockMvc.perform(delete("/comments/1"))
                .andExpect(status().isNoContent());
    }
}

