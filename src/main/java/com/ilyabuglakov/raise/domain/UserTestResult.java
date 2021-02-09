package com.ilyabuglakov.raise.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserTestResult extends Entity {
    private User user;
    private Test test;
    private int result;
}
