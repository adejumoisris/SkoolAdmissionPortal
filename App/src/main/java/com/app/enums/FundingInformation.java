package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FundingInformation {
    PERSONAL_SAVINGS("personal savings"),
    FAMILY_SUPPORT("family support"),
    SCHOLARSHIP("scholarship"),
    EMPLOYER_SPONSORSHIP("employer sponsorship"),
    GOVERNMENT_FUNDING("government funding"),
    LOANS("loans"),
    GRANTS("grants");

    private final String description;
}
