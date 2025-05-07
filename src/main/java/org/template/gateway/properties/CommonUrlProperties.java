package org.template.gateway.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@Slf4j
public class CommonUrlProperties {
    protected String scheme;
    protected String host;
    protected String context;
    protected int port;

    public void setContext(String context) {
        this.context = StringUtils.isEmpty(context)?"":"/" + context;
    }

    public String getHost() {
        return getUrl().replaceFirst(context, "");
    }

    public String getUrl() {
        return getURL().toString();
    }

    public URL getURL() {
        URL url = null;
        try {
            return url = getURI().toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            log.error("!!! Error while building URL: {}", e.getMessage());
        }

        return url;
    }

    public URI getURI() {
        UriComponents uri = null;
        try {
            uri = getUriComponents();

        } catch (IllegalArgumentException e) {
            log.error("!!! Error while building uri: {}", e.getMessage());
        }

        return uri.toUri();
    }

    private UriComponents getUriComponents() {
        if(port == 0 || port == 80) {
            return UriComponentsBuilder.newInstance().scheme(scheme).host(host).path(context).build();
        } else {
            return UriComponentsBuilder.newInstance().scheme(scheme).host(host).path(context).port(port).build();
        }
    }
}
