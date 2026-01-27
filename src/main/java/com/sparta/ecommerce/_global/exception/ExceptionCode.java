package com.sparta.ecommerce._global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    // PRODUCT P100
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P100", "해당 상품을 찾을 수 없습니다."),
    PRODUCT_STOCK_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "P101", "상품 재고가 부족합니다."),
    PRODUCT_SOLD_OUT(HttpStatus.BAD_REQUEST, "P102", "상품이 품절되었습니다."),
    PRICE_NOT_CHANGEABLE(HttpStatus.BAD_REQUEST, "P103", "이미 판매된 상품의 가격은 변경할 수 없습니다."),

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
