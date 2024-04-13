package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QualificationType {
    BACHELORS("bachelors degree"),
    MASTER("masters degree"),
    DOCTORATE("doctorate degree"),
    PROFESSIONAL("professional certification"),
    DIPLOMA("diploma");

    private final String description;
}
