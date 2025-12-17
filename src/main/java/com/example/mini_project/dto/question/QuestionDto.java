package com.example.mini_project.dto.question;


import lombok.Getter;

import java.util.List;

@Getter
public class QuestionDto {
    private String title;

    private String content;

    private List<Long> tagIds;
}
