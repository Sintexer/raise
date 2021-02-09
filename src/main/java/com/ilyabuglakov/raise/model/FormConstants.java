package com.ilyabuglakov.raise.model;

import lombok.Getter;

/**
 * The enum Form constants.
 */
public enum FormConstants {
    /**
     * Email length form constants.
     */
    EMAIL_LENGTH(256),
    /**
     * Name length form constants.
     */
    NAME_LENGTH(40),
    /**
     * Surname length form constants.
     */
    SURNAME_LENGTH(80),
    /**
     * Password min form constants.
     */
    PASSWORD_MIN(5),
    /**
     * Password max form constants.
     */
    PASSWORD_MAX(256);
    @Getter
    private final int value;

    FormConstants(int value) {
        this.value = value;
    }
}
