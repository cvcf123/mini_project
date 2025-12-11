package com.example.mini_project.dto.question;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionListDto {

    private Long id;
    private String title;
    private String writerNickname;
    private LocalDateTime createdAt;
    private List<String> tags;
}