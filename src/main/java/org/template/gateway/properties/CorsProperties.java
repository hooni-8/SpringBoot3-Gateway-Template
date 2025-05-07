package org.template.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperties {
    private List<String> allowedOrigin;
}
