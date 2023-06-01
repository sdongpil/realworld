package com.dss.realworld.error.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends AbstractBaseException {

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    public CommentNotFoundException() {
        this("댓글이 존재하지 않습니다.");
    }

    public CommentNotFoundException(final String message) {
        super(message);
    }
}
