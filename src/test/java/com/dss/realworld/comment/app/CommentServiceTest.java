package com.dss.realworld.comment.app;

import com.dss.realworld.article.domain.Article;
import com.dss.realworld.article.domain.repository.ArticleRepository;
import com.dss.realworld.comment.api.dto.AddCommentRequestDto;
import com.dss.realworld.comment.api.dto.AddCommentResponseDto;
import com.dss.realworld.comment.api.dto.CommentAuthorDto;
import com.dss.realworld.comment.domain.Comment;
import com.dss.realworld.comment.domain.repository.CommentRepository;
import com.dss.realworld.user.domain.User;
import com.dss.realworld.user.domain.repository.UserRepository;
import com.dss.realworld.util.ArticleFixtures;
import com.dss.realworld.util.CommentFixtures;
import com.dss.realworld.util.UserFixtures;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

        User newUser = UserFixtures.create();
        userRepository.persist(newUser);

        Article newArticle = ArticleFixtures.of(newUser.getId());
        articleRepository.persist(newArticle);
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
        String slug = "How-to-train-your-dragon-1";

        AddCommentResponseDto saveComment = commentService.add(addCommentRequestDto, logonUserId, slug);

        Assertions.assertThat(saveComment.getBody()).isEqualTo("His name was my name too.");
    }

    @DisplayName(value = "commentId가 유효하면 댓글 삭제 성공")
    @Test
    void t2() {
        Comment comment1 = CommentFixtures.create();
        commentRepository.add(comment1);

        Long commnetId = 1L;
        Long articleId = 1L;
        Long userId = 1L;

        final int deleteComment = commentRepository.deleteComment(commnetId,articleId,userId);

        assertThat(deleteComment).isEqualTo(1);
    }

    @DisplayName(value = "slug 유효하면 comment 리스트 가져오기")
    @Test
    void t3() {
        Comment comment1 = CommentFixtures.create();
        Comment comment2 = CommentFixtures.create();
        commentRepository.add(comment1);
        commentRepository.add(comment2);

        String slug = "How-to-train-your-dragon-1";
        List<CommentAuthorDto> comments = commentService.getAll(slug);

        assertThat(comments.size()).isEqualTo(2);
    }



    private AddCommentRequestDto createAddCommentRequestDto() {
        String body = "His name was my name too.";

        return new AddCommentRequestDto(body);
    }
}