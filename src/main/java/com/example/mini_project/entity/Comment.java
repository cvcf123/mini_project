package com.example.mini_project.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Comment → User (N:1)
    @ManyToOne(fetch = FetchType.LAZY) // 한유저는 여러 댓글 가능
    @JoinColumn(name = "user_id") //댓글은 하나의 유저에 속함(N:1)
    private User user;

    // Comment → Answer (N:1)
    @ManyToOne(fetch = FetchType.LAZY) //한 answer에 여러 댓글 가능
    @JoinColumn(name = "answer_id")
    private Answer answer;
}
