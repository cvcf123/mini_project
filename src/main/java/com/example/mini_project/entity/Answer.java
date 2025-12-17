package com.example.mini_project.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql AUTO_INCREMENT
    private Long id;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Answer → User (N:1)
    @ManyToOne(fetch = FetchType.LAZY) //유저 정보가 필요할 때만 SELECT
    @JoinColumn(name = "user_id")
    private User user;

    // Answer → Question (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // Answer → Comment (1:N)
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL) //Answer 삭제하면 관련 Comment도 자동 삭제.
    private List<Comment> comments = new ArrayList<>();
}
