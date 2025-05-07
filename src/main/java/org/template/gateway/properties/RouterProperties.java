package org.template.gateway.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class RouterProperties extends CommonUrlProperties {
    private String prefix;
    private String rewrite;
    private boolean removeContext = false;
    private boolean skipFilters = false;

    public void setPrefix(String prefix) {
        this.prefix = StringUtils.isEmpty(prefix) ? "" : "/" + prefix;
    }

    public void setRewrite(String rewrite) {
        this.rewrite = StringUtils.isEmpty(rewrite) ? "" : "/" + rewrite;
    }

    public String getRoutedUrl() {
        String routedUrl = context;

        if (this.removeContext) {
            routedUrl = routedUrl.replaceFirst(this.context, "");
        } else {
            if (StringUtils.isNotEmpty(rewrite)) {
                routedUrl = routedUrl.replaceFirst(this.context, this.rewrite);
            }
            if (StringUtils.isNotEmpty(prefix)) {
                routedUrl = this.prefix + routedUrl;
            }
        }

        return routedUrl;
    }
}
