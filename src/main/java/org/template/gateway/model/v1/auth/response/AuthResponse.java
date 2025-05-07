package org.template.gateway.model.v1.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.template.gateway.common.code.StatusCode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;

    private String code;
    private String message;

    public static AuthResponse getSuccess(String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static AuthResponse getLoginFail() {
        return AuthResponse.builder()
                .code(StatusCode.LOGIN_FAIL.getCode())
                .message(StatusCode.LOGIN_FAIL.getMessage())
                .build();
    }

    public static AuthResponse getError() {
        return AuthResponse.builder()
                .code(StatusCode.ERROR.getCode())
                .message(StatusCode.ERROR.getMessage())
                .build();
    }
}
