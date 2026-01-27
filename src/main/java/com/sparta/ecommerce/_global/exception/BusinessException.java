package com.sparta.ecommerce._global.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Getter
@Slf4j
public class BusinessException extends RuntimeException {

    private final ExceptionCode errorCode;
    private final String detailMessage;

    public BusinessException(ExceptionCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = "";
        logException(errorCode, null, null);
    }

    public BusinessException(ExceptionCode errorCode, Exception e) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = e.getMessage();
        logException(errorCode, e, null);
    }

    public BusinessException(ExceptionCode errorCode, String message) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = message;
        logException(errorCode, null, message);
    }

    public void logException(ExceptionCode errorCode, Exception e, String message) {
        LocalDateTime now = LocalDateTime.now();
        String stage = System.getenv("SPRING_PROFILES_ACTIVE");
        if (stage == null) stage = System.getenv("STAGE");
        if (stage == null) stage = "unknown";
        String service = System.getProperty("spring.application.name", "unknown");
        StackTraceElement ste = null;
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement elem : stack) {
            if (!elem.getClassName().equals(BusinessException.class.getName()) && !elem.getClassName().startsWith("java.lang.Thread")) {
                ste = elem;
                break;
            }
        }
        String className = ste != null ? ste.getClassName() : "unknown";
        String methodName = ste != null ? ste.getMethodName() : "unknown";
        int line = ste != null ? ste.getLineNumber() : -1;

        // 메시지/예외 널 처리: message가 null이면 e.getMessage() 사용, e가 null이면 에러코드를 INTERNAL_SERVER_ERROR로 고정
        ExceptionCode codeToLog = errorCode;
        String detail = message != null ? message : (e != null ? e.getMessage() : "");
        if (e == null) {
            codeToLog = ExceptionCode.INTERNAL_SERVER_ERROR;
        }

        log.error("time={} stage={} service={} class={} method={} line={} code={} message={} detail={}",
                now, stage, service, className, methodName, line,
                codeToLog != null ? codeToLog.getCode() : "unknown",
                codeToLog != null ? codeToLog.getMessage() : "unknown",
                detail);
    }
}
