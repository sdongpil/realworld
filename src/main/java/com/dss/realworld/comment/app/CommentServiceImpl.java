package com.dss.realworld.comment.app;

import com.dss.realworld.article.domain.dto.GetArticleDto;
import com.dss.realworld.article.domain.repository.ArticleRepository;
import com.dss.realworld.comment.api.dto.AddCommentResponseDto;
import com.dss.realworld.comment.domain.Comment;
import com.dss.realworld.comment.api.dto.AddCommentRequestDto;
import com.dss.realworld.comment.api.dto.CommentAuthorDto;
import com.dss.realworld.comment.domain.dto.GetCommentDto;
import com.dss.realworld.comment.domain.repository.CommentRepository;
import com.dss.realworld.user.domain.repository.GetUserDto;
import com.dss.realworld.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public AddCommentResponseDto add(AddCommentRequestDto addCommentRequestDto, Long logonUserId, String slug) {
        Optional<GetArticleDto> foundArticle = articleRepository.getBySlug(slug);

        Comment comment = Comment.builder()
                .body(addCommentRequestDto.getComment().getBody())
                .articleId(foundArticle.get().getId())
                .userId(logonUserId) // todo 로그인 안 했을 때 예외 추가
                .build();

        commentRepository.add(comment);
        GetCommentDto foundComment = commentRepository.getById(comment.getId());
        GetUserDto foundUser = userRepository.getById(comment.getUserId());

        return new AddCommentResponseDto(foundComment, foundUser);
    }
}
