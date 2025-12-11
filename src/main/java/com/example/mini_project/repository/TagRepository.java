package com.example.mini_project.repository;

import java.util.List;

import com.example.mini_project.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByIdIn(Iterable<Long> ids);
}
