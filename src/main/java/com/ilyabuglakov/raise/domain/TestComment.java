package com.ilyabuglakov.raise.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TestComment extends Entity {
    private String content;
    private LocalDateTime timestamp;
    private Test test;
    private User user;
}
