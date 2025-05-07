package org.template.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.frontend")
public class FrontendProperties extends CommonUrlProperties {
}
