package com.example.mini_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Question;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
	 List<Answer> findByQuestionOrderByCreatedAtAsc(Question question);
}
