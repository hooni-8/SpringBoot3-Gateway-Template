package org.template.gateway.common.code;

import lombok.Getter;

@Getter
public enum StatusCode {
    SUCCESS("0000", "SUCCESS")
    , LOGIN_FAIL("9998", "LOGIN_FAIL")
    , ERROR("9999", "ERROR");


    private final String code;

    private final String message;

    StatusCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
