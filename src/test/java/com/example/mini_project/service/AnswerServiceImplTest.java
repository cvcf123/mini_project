package com.example.mini_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.mini_project.entity.User;
import com.example.mini_project.dto.AnswerResponseDto;
import com.example.mini_project.entity.Answer;
import com.example.mini_project.entity.Question;
import com.example.mini_project.repository.AnswerRepository;
import com.example.mini_project.repository.QuestionRepository;

public class AnswerServiceImplTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private AnswerServiceImpl answerService;

    public AnswerServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAnswersByQuestion_success() {

        // given: 질문 생성
        Question question = Question.builder()
                .id(1L)
                .title("테스트 질문")
                .content("내용입니다")
                .createdAt(LocalDateTime.now())
                .build();

        // mock 유저 생성
        User user = User.builder()
                .id(1L)
                .name("tester")
                .email("test@test.com")
                .password("1234")
                .createdAt(LocalDateTime.now())
                .build();

        // mock 답변 1
        Answer answer1 = Answer.builder()
                .id(10L)
                .content("첫 번째 답변입니다.")
                .user(user)
                .question(question)
                .createdAt(LocalDateTime.now())
                .build();

        // mock 답변 2
        Answer answer2 = Answer.builder()
                .id(11L)
                .content("두 번째 답변입니다.")
                .user(user)
                .question(question)
                .createdAt(LocalDateTime.now())
                .build();

        List<Answer> answerList = Arrays.asList(answer1, answer2);

        // mocking repository behavior
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionOrderByCreatedAtAsc(question)).thenReturn(answerList);

        // when
        List<AnswerResponseDto> result = answerService.getAnswersByQuestion(1L);

        // then
        assertEquals(2, result.size());

        assertEquals(10L, result.get(0).getId());
        assertEquals("첫 번째 답변입니다.", result.get(0).getContent());
        assertEquals("tester", result.get(0).getUserName());

        assertEquals(11L, result.get(1).getId());
        assertEquals("두 번째 답변입니다.", result.get(1).getContent());
        assertEquals("tester", result.get(1).getUserName());
    }
}
