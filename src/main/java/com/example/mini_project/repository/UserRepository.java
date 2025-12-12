package com.example.mini_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Comment;
import com.example.mini_project.entity.User;

public interface UserRepository extends JpaRepository <User,Long> {
    boolean existsByEmail(String email);
    public interface AnswerRepository extends JpaRepository<Answer, Long> {}
    public interface CommentRepository extends JpaRepository<Comment, Long> {}
    Optional<User> findByUser(String user);
    Optional<User> findByEmail(String email);
}