package com.ilyabuglakov.raise.model;

import lombok.Getter;

/**
 * The enum Patterns.
 */
@Getter
public enum Patterns {
    /**
     * Password pattern.
     */
    PASSWORD(".{" + FormConstants.PASSWORD_MIN.getValue() + "," + FormConstants.PASSWORD_MAX.getValue() + "}"),
    /**
     * Name pattern.
     */
    NAME("^[^\\d',.-][^\\d\\n_!¡?÷¿\\/\\\\+=@#$%ˆ&*(){}|~<>;:\\[\\]]{2,}$"),
    /**
     * Email pattern.
     */
    EMAIL(".+@.+\\..+")
    ;


    private final String pattern;

    Patterns(String pattern) {
        this.pattern = pattern;
    }
}
