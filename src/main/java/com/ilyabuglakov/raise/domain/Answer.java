package com.ilyabuglakov.raise.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Answer extends Entity {
    private String content;
    private boolean correct;
    private Question question;

    @Override
    public String toString() {
        return "Answer{" +
                "content='" + content + '\'' +
                ", correct=" + correct +
                '}';
    }
}
