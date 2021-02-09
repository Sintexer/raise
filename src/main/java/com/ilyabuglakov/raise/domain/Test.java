package com.ilyabuglakov.raise.domain;

import com.ilyabuglakov.raise.domain.type.Characteristic;
import com.ilyabuglakov.raise.domain.type.TestStatus;
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
public class Test extends Entity {
    private String testName;
    private User author;
    private int difficulty;
    private TestStatus status;
    private Set<Characteristic> characteristics;
    private Set<Question> questions;
    private TestCategory category;

    public static final int BASE_DIFFICULTY = 1;


    @Override
    public String toString() {
        return "Test{" +
                "testName='" + testName + '\'' +
                ", difficulty=" + difficulty +
                ", characteristics=" + characteristics +
                ", questions=" + questions +
                '}';
    }
}

