package org.template.gateway.filters.routing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import org.template.gateway.GatewayConsts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CustomTokenRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomTokenRelayGatewayFilterFactory.Config> {

    public static class Config {}

    public CustomTokenRelayGatewayFilterFactory() {
        super(Config.class);
    }

    public GatewayFilter apply() {
        return apply(new Config());
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> exchange.getPrincipal()
                .filter(Authentication.class::isInstance)
                .cast(Authentication.class)
                .filter(Authentication::isAuthenticated)
                .map(authentication -> setBearerAuth(exchange, authentication))
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }

    private ServerWebExchange setBearerAuth(ServerWebExchange exchange, Authentication authentication) {
        List<String> authorities = new ArrayList<>();
        authorities.add(GatewayConsts.FREE_USER);

        String jwtToken = (String) authentication.getCredentials();

        Object principal = authentication.getPrincipal();

        if (principal instanceof Map) {
            Map<String, Object> attrMap = (Map<String, Object>) principal;
            Map<String, List<String>> realmAccess = (Map<String, List<String>>) attrMap.get("realm_access");

            if (realmAccess != null && realmAccess.containsKey("roles")) {
                List<String> roles = realmAccess.get("roles");
                if (roles.contains(GatewayConsts.ADMIN)) authorities.add(GatewayConsts.ADMIN);
                if (roles.contains(GatewayConsts.PAID_USER)) authorities.add(GatewayConsts.PAID_USER);
            }
        }

        ServerHttpRequest request = exchange
                .getRequest()
                .mutate()
                .headers(headers -> {
                    headers.setBearerAuth(jwtToken);
                    headers.addAll(GatewayConsts.X_AUTHORITY_HEADER, authorities);
                })
                .build();

        return exchange.mutate().request(request).build();
    }
}
