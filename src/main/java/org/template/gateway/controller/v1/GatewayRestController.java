package org.template.gateway.controller.v1;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.template.gateway.common.model.LoginStatus;
import org.template.gateway.filters.jwt.JwtTokenProvider;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gateway")
public class GatewayRestController {

//    @Autowired
//    JwtTokenProvider jwtTokenProvider;
//
//    @GetMapping("/session")
//    public Mono<LoginStatus> session (@Parameter(hidden = true) @AuthenticationPrincipal Authentication authentication) {
//        LoginStatus loginInfo = new LoginStatus();
//
//        return Mono.just(loginInfo);
//    }
}
