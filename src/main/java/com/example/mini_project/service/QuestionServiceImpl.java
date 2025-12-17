package com.example.mini_project.service;

import com.example.mini_project.dto.question.QuestionDetailDto;
import com.example.mini_project.dto.question.QuestionDto;
import com.example.mini_project.dto.question.QuestionListDto;
import com.example.mini_project.entity.Question;
import com.example.mini_project.entity.Question_tag;
import com.example.mini_project.entity.Tag;
import com.example.mini_project.entity.User;
import com.example.mini_project.repository.QuestionRepository;
import com.example.mini_project.repository.TagRepository;
import com.example.mini_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public QuestionDetailDto createQuestion(Long userId, QuestionDto request) {
        User writer = findUser(userId);
        Question question = Question.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .writer(writer)
            .questionTags(new ArrayList<>())
            .createdAt(LocalDateTime.now())
            .build();

        List<Tag> tags = findTags(request.getTagIds());
        addQuestionTags(question, tags);

        Question saved = questionRepository.save(question);
        return toDetailResponse(saved);
    }

    @Override
    public QuestionDetailDto getQuestion(Long questionId) {
        Question question = findQuestion(questionId);
        return toDetailResponse(question);
    }

    @Override
    public List<QuestionListDto> getQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
            .map(this::toListResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<QuestionListDto> searchQuestions(String keyword) {
        List<Question> questions = questionRepository.searchByTitleOrContent(keyword);
        return questions.stream()
            .map(this::toListResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<QuestionListDto> getQuestionsByTag(Long tagId) {
        List<Question> questions = questionRepository.findByTagId(tagId);
        return questions.stream()
            .map(this::toListResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<QuestionListDto> searchQuestionsByTag(String keyword, Long tagId) {
        List<Question> questions = questionRepository.searchByTitleOrContentAndTagId(keyword, tagId);
        return questions.stream()
            .map(this::toListResponse)
            .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public QuestionDetailDto updateQuestion(Long questionId, Long userId, QuestionDto request) {
        Question question = findQuestion(questionId);
        validateWriter(question, userId);

        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setUpdatedAt(LocalDateTime.now());

        List<Tag> tags = findTags(request.getTagIds());
        resetQuestionTags(question, tags);

        return toDetailResponse(question);
    }


    @Override
    @Transactional
    public void deleteQuestion(Long questionId, Long userId) {
        Question question = findQuestion(questionId);
        validateWriter(question, userId);
        questionRepository.delete(question);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다."));
    }

    private List<Tag> findTags(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new ArrayList<>();
        }
        return tagRepository.findByIdIn(tagIds);
    }

    private void addQuestionTags(Question question, List<Tag> tags) {
        for (Tag tag : tags) {
            Question_tag questionTag = Question_tag.builder()
                .question(question)
                .tag(tag)
                .build();
            question.getQuestionTags().add(questionTag);
        }
    }

    private void resetQuestionTags(Question question, List<Tag> tags) {
        question.getQuestionTags().clear();
        addQuestionTags(question, tags);
    }

    private void validateWriter(Question question, Long userId) {
        Long writerId = question.getWriter().getId();
        if (!writerId.equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정/삭제할 수 있습니다.");
        }
    }
    private QuestionListDto toListResponse(Question question) {
        List<String> tagNames = question.getQuestionTags().stream()
            .map(qt -> qt.getTag().getName())
            .collect(Collectors.toList());

        return new QuestionListDto(
            question.getId(),
            question.getTitle(),
            question.getWriter().getName(),
            question.getCreatedAt(),
            tagNames
        );
    }
    private QuestionDetailDto toDetailResponse(Question question) {
        List<String> tagNames = question.getQuestionTags().stream()
            .map(qt -> qt.getTag().getName())
            .collect(Collectors.toList());
        return new QuestionDetailDto(
            question.getId(),
            question.getTitle(),
            question.getContent(),
            question.getWriter().getName(),
            question.getWriter().getId(),
            question.getCreatedAt(),
            question.getUpdatedAt(),
            tagNames
        );
    }
}
