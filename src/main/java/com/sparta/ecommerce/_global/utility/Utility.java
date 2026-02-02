package com.sparta.ecommerce._global.utility;

import com.sparta.ecommerce.product.entity.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class Utility {

    public record CursorInfo(LocalDateTime cursorCreatedAt, Long cursorId) {}

    public static CursorInfo getCursorInfo(String cursor) {
        String decoded = new String(Base64.getDecoder()
                .decode(cursor));
        String[] parts = decoded.split(",");
        LocalDateTime cursorCreatedAt = LocalDateTime.parse(parts[0]);
        Long cursorId = Long.parseLong(parts[1]);
        return new CursorInfo(cursorCreatedAt, cursorId);
    }

    public static String getNextCursor(Product last) {
        String nextCursor;
        nextCursor = Base64.getEncoder()
                .encodeToString((last.getCreatedAt() + "," + last.getId()).getBytes());
        return nextCursor;
    }
}
