package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Disability {
    PHYSICAL("physical disability"),
    SENSORY("sensory disability"),
    COGNITIVE("cognitive/neurological disability"),
    PSYCHIATRIC("psychiatric/mental health disability"),
    CHRONIC("chronic heart condition"),
    INVISIBLE("invisible disability"),
    TEMPORARY("temporary disability"),
    DEVELOPMENTAL("developmental disability"),
    OTHER("other/prefer not to say"),
    NONE("no disability");

    private final String description;
}
