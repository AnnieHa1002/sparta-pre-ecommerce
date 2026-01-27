package com.sparta.ecommerce._global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
   
    // TEST T200
    TEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "T200", "테스트가 실패했습니다."),

    // Other Z000
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Z000", "UNAUTHORIZED"),
    RESTRICTED(HttpStatus.UNAUTHORIZED, "Z001", "RESTRICTED"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Z003", "FORBIDDEN"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Z004", "INPUT_VALUE_INVALID"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Z005", "INVALID_PARAMETER."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Z006", "INVALID_REQUEST"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Z007", "INTERNAL_SERVER_ERROR"),
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "Z008", "NOT_IMPLEMENTED"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
