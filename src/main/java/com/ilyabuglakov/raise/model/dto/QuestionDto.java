package com.ilyabuglakov.raise.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Integer id;
    private Set<Integer> answers;
}
