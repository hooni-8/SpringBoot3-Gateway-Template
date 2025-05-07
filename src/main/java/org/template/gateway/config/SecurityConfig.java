package org.template.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.template.gateway.filters.jwt.CustomJwtAuthenticationFilter;
import org.template.gateway.properties.AccessProperties;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private CustomJwtAuthenticationFilter jwtFilter;

    @Autowired
    private AccessProperties accessProperties;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        configExclude(http);

        http.csrf(csrf -> csrf.disable())
                .formLogin(login -> login.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .headers(headers -> headers.referrerPolicy(policy -> policy.policy(ReferrerPolicy.ORIGIN)))
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchange -> exchange.anyExchange().authenticated());

        return http.build();
    }

    private void configExclude(ServerHttpSecurity http) {
        String[] excludes = accessProperties.getExcludesArray();
        log.info("### Security Excluding patterns: " + Arrays.toString(excludes));

        http.authorizeExchange(exchanges -> {
            exchanges.pathMatchers(excludes).permitAll();
            exchanges.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        });
    }
}
