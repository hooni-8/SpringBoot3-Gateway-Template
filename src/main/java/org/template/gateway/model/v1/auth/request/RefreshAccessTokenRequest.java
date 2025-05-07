package org.template.gateway.model.v1.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshAccessTokenRequest {

    private String accessToken;

    private String refreshToken;
}
