package com.example.mini_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 Answer에 달린 댓글을 생성일 기준 오름차순으로 조회
    List<Comment> findByAnswerOrderByCreatedAtAsc(Answer answer);
}
