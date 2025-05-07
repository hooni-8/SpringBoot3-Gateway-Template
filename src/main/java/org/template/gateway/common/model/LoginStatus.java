package org.template.gateway.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginStatus {

    private boolean status = false;

    private String userCode;

    private String name;

    private String role;

}
