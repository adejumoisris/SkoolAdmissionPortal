package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Authority {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    private final String authority;
}
