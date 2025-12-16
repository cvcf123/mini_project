package com.example.mini_project.repository;

import com.example.mini_project.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
        select distinct q
        from Question q
        where lower(q.title) like lower(concat('%', :keyword, '%'))
           or lower(q.content) like lower(concat('%', :keyword, '%'))
        order by q.createdAt desc
        """)
    List<Question> searchByTitleOrContent(@Param("keyword") String keyword);
}
