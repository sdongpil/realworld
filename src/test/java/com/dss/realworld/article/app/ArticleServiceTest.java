package com.dss.realworld.article.app;

import com.dss.realworld.article.api.dto.CreateArticleRequestDto;
import com.dss.realworld.article.domain.Article;
import com.dss.realworld.article.domain.repository.ArticleRepository;
import com.dss.realworld.error.exception.ArticleNotFoundException;
import com.dss.realworld.util.ArticleFixtures;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        clearTable();
    }

    @AfterEach
    void teatDown() {
        clearTable();
    }

    private void clearTable() {
        articleRepository.deleteAll();
        articleRepository.resetAutoIncrement();
    }

    @Test
    void Should_ThrownException_When_ArticleAuthorIsNotMatch() {
        //given
        Long validUserId = 1L;
        Article newArticle = saveArticle(validUserId);
        Article savedArticle = articleRepository.findById(newArticle.getId()).orElseThrow(ArticleNotFoundException::new);
        assertThat(savedArticle.getId()).isEqualTo(validUserId);

        //when
        String savedSlug = savedArticle.getSlug();
        Long wrongAuthorId = 10L;

        //then
        assertThatThrownBy(() -> articleService.delete(savedSlug, wrongAuthorId))
                .hasMessageContaining("작성자가 일치하지 않습니다.");
    }

    @Test
    void Should_ThrownException_When_ArticleIsNotFound() {
        //given
        Long validUserId = 1L;
        Article newArticle = saveArticle(validUserId);
        Article savedArticle = articleRepository.findById(newArticle.getId()).orElseThrow(ArticleNotFoundException::new);
        assertThat(savedArticle.getId()).isEqualTo(validUserId);

        //when
        String wrongArticleSlug = "wrongArticleSlug";

        //then
        assertThatThrownBy(() -> articleService.delete(wrongArticleSlug, validUserId))
                .isInstanceOf(ArticleNotFoundException.class);
    }

    @Test
    void Should_CreateArticleSuccess_When_ArticleDtoAndLogonIdIsValid() {
        //given
        Long logonId = 1L;
        CreateArticleRequestDto createArticleRequestDto = createArticleDto();

        //when
        Article savedArticle = articleService.save(createArticleRequestDto, logonId).orElseThrow(ArticleNotFoundException::new);

        //then
        assertThat(savedArticle.getUserId()).isEqualTo(logonId);
    }

    private CreateArticleRequestDto createArticleDto() {
        return ArticleFixtures.createRequestDto();
    }

    @Test
    void Should_ArticleDeleteSuccess_When_ArticleSlugAndUserIdIsValid() {
        //given
        Long validUserId = 1L;
        Article newArticle = saveArticle(validUserId);
        Article savedArticle = articleRepository.findById(newArticle.getId()).orElseThrow(ArticleNotFoundException::new);
        assertThat(savedArticle.getUserId()).isEqualTo(validUserId);

        //when
        articleService.delete(savedArticle.getSlug(), validUserId);

        //then
        Assertions.assertThatThrownBy(()->articleRepository.findBySlug(savedArticle.getSlug()).orElseThrow(ArticleNotFoundException::new)).isInstanceOf(ArticleNotFoundException.class);
    }

    private Article saveArticle(Long userId) {
        Article newArticle = ArticleFixtures.create(userId);
        articleRepository.persist(newArticle);

        return newArticle;
    }
}