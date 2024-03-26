package org.util;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    NB("NB");

    private final String value;

    private Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

