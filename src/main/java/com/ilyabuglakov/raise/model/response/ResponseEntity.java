package com.ilyabuglakov.raise.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntity {
    private String link;
    private boolean isRedirect;
    private Map<String, Object> attributes = new HashMap<>();
    private boolean errorOccurred = false;

    public Object setAttribute(String key, Object value) {
        return attributes.put(key, value);
    }
}
