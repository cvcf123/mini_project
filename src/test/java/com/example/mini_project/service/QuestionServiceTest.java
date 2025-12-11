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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private User testUser;
    private Tag testTag;
    private Question testQuestion;
    private QuestionDto questionDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("테스트유저")
                .email("test@test.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .build();

        testTag = Tag.builder()
                .id(1L)
                .name("Java")
                .build();

        testQuestion = Question.builder()
                .id(1L)
                .title("테스트 질문")
                .content("테스트 내용")
                .writer(testUser)
                .questionTags(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        questionDto = mock(QuestionDto.class);
        when(questionDto.getTitle()).thenReturn("테스트 질문");
        when(questionDto.getContent()).thenReturn("테스트 내용");
        when(questionDto.getTagIds()).thenReturn(Arrays.asList(1L));
    }

    @Test
    @DisplayName("질문 생성 성공")
    void createQuestion_Success() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
        given(tagRepository.findByIdIn(anyList())).willReturn(Arrays.asList(testTag));
        given(questionRepository.save(any(Question.class))).willReturn(testQuestion);

        // when
        QuestionDetailDto result = questionService.createQuestion(1L, questionDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("테스트 질문");
        assertThat(result.getContent()).isEqualTo("테스트 내용");
        assertThat(result.getWriterNickname()).isEqualTo("테스트유저");
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    @DisplayName("질문 생성 실패 - 존재하지 않는 사용자")
    void createQuestion_UserNotFound() {
        // given
        given(userRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> questionService.createQuestion(999L, questionDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("질문 상세 조회 성공")
    void getQuestion_Success() {
        // given
        given(questionRepository.findById(1L)).willReturn(Optional.of(testQuestion));

        // when
        QuestionDetailDto result = questionService.getQuestion(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("테스트 질문");
    }

    @Test
    @DisplayName("질문 상세 조회 실패 - 존재하지 않는 질문")
    void getQuestion_NotFound() {
        // given
        given(questionRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> questionService.getQuestion(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 질문입니다.");
    }

    @Test
    @DisplayName("질문 목록 조회 성공")
    void getQuestions_Success() {
        // given
        List<Question> questions = Arrays.asList(testQuestion);
        given(questionRepository.findAll()).willReturn(questions);

        // when
        List<QuestionListDto> result = questionService.getQuestions();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("테스트 질문");
    }

    @Test
    @DisplayName("질문 수정 성공")
    void updateQuestion_Success() {
        // given
        given(questionRepository.findById(1L)).willReturn(Optional.of(testQuestion));
        given(tagRepository.findByIdIn(anyList())).willReturn(Arrays.asList(testTag));

        QuestionDto updateDto = mock(QuestionDto.class);
        when(updateDto.getTitle()).thenReturn("수정된 질문");
        when(updateDto.getContent()).thenReturn("수정된 내용");
        when(updateDto.getTagIds()).thenReturn(Arrays.asList(1L));

        // when
        QuestionDetailDto result = questionService.updateQuestion(1L, 1L, updateDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("수정된 질문");
        assertThat(result.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    @DisplayName("질문 수정 실패 - 작성자가 아닌 경우")
    void updateQuestion_NotWriter() {
        // given
        given(questionRepository.findById(1L)).willReturn(Optional.of(testQuestion));

        // when & then
        assertThatThrownBy(() -> questionService.updateQuestion(1L, 999L, questionDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작성자만 수정/삭제할 수 있습니다.");
    }

    @Test
    @DisplayName("질문 삭제 성공")
    void deleteQuestion_Success() {
        // given
        given(questionRepository.findById(1L)).willReturn(Optional.of(testQuestion));
        doNothing().when(questionRepository).delete(any(Question.class));

        // when
        questionService.deleteQuestion(1L, 1L);

        // then
        verify(questionRepository, times(1)).delete(testQuestion);
    }

    @Test
    @DisplayName("질문 삭제 실패 - 작성자가 아닌 경우")
    void deleteQuestion_NotWriter() {
        // given
        given(questionRepository.findById(1L)).willReturn(Optional.of(testQuestion));

        // when & then
        assertThatThrownBy(() -> questionService.deleteQuestion(1L, 999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작성자만 수정/삭제할 수 있습니다.");
    }

    @Test
    @DisplayName("질문 삭제 실패 - 존재하지 않는 질문")
    void deleteQuestion_NotFound() {
        // given
        given(questionRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> questionService.deleteQuestion(999L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 질문입니다.");
    }

    @Test
    @DisplayName("태그 없이 질문 생성 성공")
    void createQuestion_WithoutTags() {
        // given
        QuestionDto noTagDto = mock(QuestionDto.class);
        when(noTagDto.getTitle()).thenReturn("태그 없는 질문");
        when(noTagDto.getContent()).thenReturn("태그 없는 내용");
        when(noTagDto.getTagIds()).thenReturn(null);

        given(userRepository.findById(1L)).willReturn(Optional.of(testUser));
        given(questionRepository.save(any(Question.class))).willReturn(testQuestion);

        // when
        QuestionDetailDto result = questionService.createQuestion(1L, noTagDto);

        // then
        assertThat(result).isNotNull();
        verify(tagRepository, never()).findByIdIn(anyList());
    }
}
