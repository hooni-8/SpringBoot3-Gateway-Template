package org.template.gateway.model.v1.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String userId;

    private String userPassword;

    private String userEmail;

    private String userName;
}
