package com.sparta.ecommerce._global.component;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class CursorCode {
    public record CursorInfo(LocalDateTime cursorCreatedAt, Long cursorId) {}

    public CursorInfo decode(String cursor) {
        String decoded = new String(Base64.getDecoder()
                .decode(cursor));
        String[] parts = decoded.split(",");
        LocalDateTime cursorCreatedAt = LocalDateTime.parse(parts[0]);
        Long cursorId = Long.parseLong(parts[1]);
        return new CursorInfo(cursorCreatedAt, cursorId);

    }

    public String encode(LocalDateTime createdAt, Long id) {
        return Base64.getEncoder()
                .encodeToString((createdAt + "," + id).getBytes());
    }
}
