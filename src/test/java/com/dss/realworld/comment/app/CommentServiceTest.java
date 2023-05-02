package com.dss.realworld.comment.app;

import com.dss.realworld.article.domain.Article;
import com.dss.realworld.article.domain.repository.ArticleRepository;
import com.dss.realworld.comment.domain.dto.AddCommentRequestDto;
import com.dss.realworld.comment.domain.dto.AddCommentRequestDto.AddCommentDto;
import com.dss.realworld.comment.domain.dto.GetCommentAuthorDto;
import com.dss.realworld.comment.domain.repository.CommentRepository;
import com.dss.realworld.user.domain.User;
import com.dss.realworld.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        clearTable();

        User newUser = User.builder()
                .username("Jacob000")
                .email("jake000@jake.jake")
                .password("jakejake")
                .build();
        userRepository.addUser(newUser);

        Article newArticle = Article.builder()
                .title("How to train your dragon")
                .slug("How-to-train-your-dragon")
                .description("Ever wonder how?")
                .body("You have to believe")
                .userId(newUser.getId())
                .build();
        articleRepository.createArticle(newArticle);
    }

    @AfterEach
    void teatDown() {
        clearTable();
    }

    private void clearTable() {
        userRepository.deleteAll();
        userRepository.resetAutoIncrement();

        articleRepository.deleteAll();
        articleRepository.resetAutoIncrement();

        commentRepository.deleteAll();
        commentRepository.resetAutoIncrement();
    }

    @DisplayName(value = "매개변수들이 유효하면 댓글 작성 성공")
    @Test
    void t1() {
        AddCommentRequestDto addCommentRequestDto = createAddCommentRequestDto();
        Long logonUserId = 1L;
        String slug = "How-to-train-your-dragon";

        GetCommentAuthorDto saveComment = commentService.addComment(addCommentRequestDto, logonUserId, slug);

        Assertions.assertThat(saveComment.getBody()).isEqualTo("His name was my name too.");
    }

    private AddCommentRequestDto createAddCommentRequestDto() {
        AddCommentDto addCommentDto = AddCommentDto.builder()
                .body("His name was my name too.")
                .build();

        return new AddCommentRequestDto(addCommentDto);
    }
}