package com.example.mini_project.dto.question;


import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class QuestionDetailDto {

    private Long id;
    private String title;
    private String content;
    private String writerNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> tags;
}
