package org.template.gateway.config;

import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import org.template.gateway.exception.GlobalErrorAttributes;
import org.template.gateway.exception.GlobalErrorWebExceptionHandler;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer.class)
@AutoConfigureBefore(ErrorWebFluxAutoConfiguration.class)
public class ExceptionConfig {

    @Bean
    @Order(-1)
    ErrorWebExceptionHandler errorWebExceptionHandler(WebProperties webProperties, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer, ObjectProvider<ViewResolver> viewResolvers) {
        GlobalErrorWebExceptionHandler customErrorWebExceptionHandler = new GlobalErrorWebExceptionHandler(new GlobalErrorAttributes(), webProperties.getResources(), applicationContext);
        customErrorWebExceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        customErrorWebExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        customErrorWebExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());

        return customErrorWebExceptionHandler;
    }
}
