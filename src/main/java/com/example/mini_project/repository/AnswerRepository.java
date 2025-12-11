package com.example.mini_project.repository;

import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionOrderByCreatedAtAsc(Question question);
}
