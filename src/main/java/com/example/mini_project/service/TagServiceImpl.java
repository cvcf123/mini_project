package com.example.mini_project.service;

import com.example.mini_project.dto.TagResponseDto;
import com.example.mini_project.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<TagResponseDto> getTags() {
        return tagRepository.findAll().stream()
            .map(tag -> new TagResponseDto(tag.getId(), tag.getName()))
            .toList();
    }
}

