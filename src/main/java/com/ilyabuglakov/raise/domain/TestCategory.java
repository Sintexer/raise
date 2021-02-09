package com.ilyabuglakov.raise.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestCategory extends Entity {
    private String category;
    private TestCategory parent;

    @Override
    public String toString() {
        return "TestCategory{" +
                "category='" + category + '\'' +
                '}';
    }
}
