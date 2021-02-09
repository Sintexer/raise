package com.ilyabuglakov.raise.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Question extends Entity {
    private String name;
    private String content;
    private Set<Answer> answers;
    private Test test;
    private int correctAmount;

    @Override
    public String toString() {
        return "Question{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", answers=" + answers +
                "}";
    }
}
