package org.template.gateway.properties;

import com.nimbusds.jose.util.ArrayUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.security.access")
public class AccessProperties {
    private List<String> nologin = new ArrayList<>();

    public String[] getExcludesArray(String... pattern) {
        return ArrayUtils.concat(nologin.toArray(new String[0]), pattern);
    }
}
