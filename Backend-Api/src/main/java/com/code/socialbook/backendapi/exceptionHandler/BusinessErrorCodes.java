package com.code.socialbook.backendapi.exceptionHandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No Code."),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "New Password does not match"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled."),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User Account is Locked."),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or password failed."),

    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description ) {
        this.httpStatus = httpStatus;
        this.description = description;
        this.code = code;
    }




}
