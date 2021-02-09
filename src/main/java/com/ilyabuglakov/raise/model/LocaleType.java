package com.ilyabuglakov.raise.model;

import lombok.Getter;

/**
 * The enum Locale type.
 */
@Getter
public enum LocaleType {
    /**
     * Russian locale type.
     */
    RUSSIAN("ru_RU", "Русский"),
    /**
     * The English us.
     */
    ENGLISH_US("en_US", "English US"),
    /**
     * Swedish locale type.
     */
    SWEDISH("sv_SE", "Svenska");

    private final String locale;
    private final String viewName;

    LocaleType(String locale, String viewName) {
        this.locale = locale;
        this.viewName = viewName;
    }
}
