package org.template.gateway.controller.v1.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.template.gateway.common.code.StatusCode;
import org.template.gateway.common.model.LoginStatus;
import org.template.gateway.model.v1.auth.request.*;
import org.template.gateway.model.v1.auth.response.AuthResponse;
import org.template.gateway.model.v1.common.response.CommonResponse;
import org.template.gateway.service.v1.auth.AuthService;
import org.template.gateway.service.v1.auth.RefreshTokenService;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    // 회원가입
    @PostMapping("/register")
    public Mono<CommonResponse> register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest)
                .thenReturn(CommonResponse.getSuccess())
                .onErrorReturn(CommonResponse.getError());
    }

    // 아이디 중복 확인
    @PostMapping("/existsUserId")
    public Mono<Boolean> existsUserId(@RequestBody RegisterExistsId registerExistsId) {
        return authService.existsUserId(registerExistsId.getUserId());
    }

    // 로그인
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest)
                .map(authResponse -> {
                    // 로그인 실패 판단 (code 값을 비교)
                    if (StatusCode.LOGIN_FAIL.getCode().equals(authResponse.getCode())) {
                        return ResponseEntity.ok(authResponse); // 실패 응답 반환
                    }

                    return ResponseEntity.ok(authResponse); // 성공 응답 반환
                })
                .onErrorResume(e -> {
                    // 오류 발생 시 오류 응답 반환
                    return Mono.just(ResponseEntity.ok(AuthResponse.getError()));
                });
    }

    // refresh 토큰
    @PostMapping("/refresh")
    public Mono<ResponseEntity<AuthResponse>> refreshAccessToken(@RequestBody RefreshAccessTokenRequest tokenRequest) {
        String userCode = authService.getClaimsUserCode(tokenRequest.getRefreshToken());

        // 저장된 Refresh Token 확인
        String storedToken = refreshTokenService.getRefreshToken(userCode);

        if (storedToken == null || !storedToken.equals(tokenRequest.getRefreshToken())) {
            return Mono.just(ResponseEntity.ok(AuthResponse.getLoginFail()));
        }

        return authService.refreshToken(tokenRequest, userCode)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.ok(AuthResponse.getError())));
    }

    @PostMapping("/session")
    public LoginStatus getSession(@RequestBody RefreshAccessTokenRequest tokenRequest) {
        return authService.getSession(tokenRequest.getAccessToken());
    }

    // 로그아웃
    @PostMapping("/logout")
    public void logout(@RequestBody String userId) {
        refreshTokenService.deleteRefreshToken(userId);
    }

}
