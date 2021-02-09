package com.ilyabuglakov.raise.domain.type;

public enum Characteristic {
    MEMORY,
    LOGIC,
    CALCULATIONS,
    REACTION;


    public String getPropertyName() {
        return name().toLowerCase().replace("_", ".");
    }
}
