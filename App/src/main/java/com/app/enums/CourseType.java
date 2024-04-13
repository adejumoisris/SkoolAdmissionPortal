package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CourseType {
    UNDERGRADUATE("undergraduate degree"),
    POSTGRADUATE("postgraduate degree"),
    OTHERS("others");

    private final String description;
}
