package com.ilyabuglakov.raise.model.dto;

import com.ilyabuglakov.raise.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDto {
    Integer userId;
    Integer testId;
    Integer authorId;
    int result;
    Set<Question> incorrectQuestions;
}
