package com.shop.bill_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    USER_EXISTED(401,  HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(401, HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD_OR_USERNAME(403, HttpStatus.BAD_REQUEST),
    ACCESSDENIED(404, HttpStatus.FORBIDDEN),
    INVALID_INPUT(405, HttpStatus.BAD_REQUEST),
    POST_NOT_EXISTED(406, HttpStatus.BAD_REQUEST),
    NOT_AUTHENTICATED(407, HttpStatus.UNAUTHORIZED),
    POST_NOT_FOUND(408, HttpStatus.NOT_FOUND),
    COMMENT_NOT_EXISTED(409, HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(410, HttpStatus.BAD_REQUEST),
    CANNOT_SEND_EMAIL(412, HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, HttpStatusCode statusCode) {
        this.code = code;
        this.httpStatusCode = statusCode;
    }

    private final int code;
    private final HttpStatusCode httpStatusCode;
}