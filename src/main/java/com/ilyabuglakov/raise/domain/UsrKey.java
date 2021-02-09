package com.ilyabuglakov.raise.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsrKey {
    private String key;
    private Integer userId;
    private LocalDateTime timestamp;
}
