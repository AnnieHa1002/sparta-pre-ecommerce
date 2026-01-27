package com.sparta.ecommerce._global.exception.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 예외 로그 저장용 클래스입니다 추후 DB에 저장하거나 외부 모니터링 툴과 연동할 수 있도록 설계되었습니다.

@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ExceptionLog {

    private String message;
    private String methodName;
    private String serviceName;
    private Integer codeLineNumber;
    private String apiPath;
    private String stackTrace;

    @CreatedDate
    private LocalDateTime occurredAt;

    public ExceptionLog(
            String message,
            String methodName,
            String serviceName,
            String apiPath,
            Integer codeLineNumber,
            String stackTrace) {
        this.message = message;
        this.methodName = methodName;
        this.serviceName = serviceName;
        this.apiPath = apiPath;
        this.codeLineNumber = codeLineNumber;
        this.stackTrace = stackTrace;
    }
}
