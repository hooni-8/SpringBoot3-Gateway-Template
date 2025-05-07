package org.template.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.template.gateway.filters.routing.CustomTokenRelayGatewayFilterFactory;
import org.template.gateway.common.router.RouterMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class RouterConfig {

    @Value("${spring.webflux.base-path}")
    String basePath;

    @Autowired
    private RouterMapper routerMapper;

    @Autowired
    CustomTokenRelayGatewayFilterFactory customTokenRelayGatewayFilterFactory;

    @Bean
    List<GatewayFilter> getFilters() {
        List<GatewayFilter> filters = new ArrayList<>();

        filters.add(customTokenRelayGatewayFilterFactory.apply());

        return filters;
    }

    @Bean
    RouteLocator cloudRoutes(RouteLocatorBuilder builder) {
        Builder rb = builder.routes();
        log.info("RouterMapper loaded: {}", routerMapper.list().size());
        routerMapper.list().forEach((id, router) -> {
            String routeId = String.format("%s-router", id);
            String predicator = router.getContext();
            String destination = router.getRoutedUrl();
            String routeService = router.getUrl();
            String prefixPath = router.getPrefix();
            String rewriteTo = router.getRewrite();
            boolean isRemoveContext = router.isRemoveContext();
            boolean isSkipFilters = router.isSkipFilters();

            String space1 = StringUtils.repeat(" ", 30 - routeId.length());
            String space2 = StringUtils.repeat(" ", 15 - predicator.length());
            String space3 = StringUtils.repeat(" ", 20 - destination.length());

            log.info("[{}]{} FROM {}{} TO {}{} URL {} {}", routeId, space1, predicator, space2, destination, space3, router.getHost(), isSkipFilters?"::SKIP FILTERS::":"");

            rb.route(routeId, r -> r.path(predicator + "/**")
                    .filters(f -> {
                        // 게이트웨이 컨텍스트 제거
                        f.rewritePath(basePath + "/(?<segment>.*)", "/${segment}");

                        if (isRemoveContext) {
                            f.rewritePath(predicator + "/(?<segment>.*)", "/${segment}");
                        } else {
                            if(StringUtils.isNotEmpty(router.getRewrite())) {
                                // 지정된 context로 rewrite
                                f.rewritePath(predicator + "/(?<segment>.*)", rewriteTo + "/${segment}");
                            }

                            if(StringUtils.isNotEmpty(router.getPrefix())) {
                                // 지정된 context 앞으로 prefix 추가
                                f.prefixPath(prefixPath);
                            }
                        }
                        //기본 필터 설정
                        f.saveSession();	//세션 저장

                        if(isSkipFilters) {
                            return f;

                        } else {
                            return f.filters(getFilters());

                        }
                    }).uri(routeService));
        });

        return rb.build();
    }
}