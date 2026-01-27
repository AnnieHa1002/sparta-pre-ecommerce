package com.sparta.ecommerce._global.exception;

import com.sparta.ecommerce._global.Message;
import com.sparta.ecommerce._global.exception.domain.ExceptionLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static void logError(ExceptionCode code, ExceptionLog errorLog,
            StackTraceElement stack) {
        String number = errorLog.getCodeLineNumber() == -1 ? "" : errorLog.getCodeLineNumber()
                .toString();
        log.error(
                "ErrorDetails | code={} | message={} | service={} | method={} | line={} | path={}",
                code.getCode(), errorLog.getMessage(), errorLog.getServiceName(),
                errorLog.getMethodName(), number, stack == null ? "" : stack.toString());
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Message> handleBusinessException(final BusinessException e) {
        final ExceptionCode errorCode = e.getErrorCode();
        logError(errorCode, ExceptionHandleService.getExceptionLog(e),
                e.getStackTrace().length > 0 ? e.getStackTrace()[0] : null);
        return new ResponseEntity<>(Message.fail(errorCode), errorCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Message> handleGlobalException(final Exception e) {
        final String errorMessage = e.getMessage();
        final Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("message", errorMessage);
        final ExceptionCode code = ExceptionCode.INTERNAL_SERVER_ERROR;
        logError(code, ExceptionHandleService.getExceptionLog(e),
                e.getStackTrace().length > 0 ? e.getStackTrace()[0] : null);
        return new ResponseEntity<>(Message.fail(errorDetails, code),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
