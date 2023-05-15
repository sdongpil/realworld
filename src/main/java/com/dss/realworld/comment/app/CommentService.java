package com.dss.realworld.comment.app;

import com.dss.realworld.comment.api.dto.AddCommentRequestDto;
import com.dss.realworld.comment.api.dto.AddCommentResponseDto;
import com.dss.realworld.comment.api.dto.CommentAuthorDto;
import com.dss.realworld.common.dto.AuthorDto;

import java.util.List;

public interface CommentService {

    AddCommentResponseDto add(AddCommentRequestDto addCommentRequestDto, Long logonUserId, String slug);

    int deleteComment(String slug,Long commentId,Long userId);

    AuthorDto getAuthor(Long userId);
    List<CommentAuthorDto> getAll(String slug);
}