package org.template.gateway;

public class GatewayConsts {
	public static final String ROOT = "/gateway";

	public static final String X_AUTHORITY_HEADER = "x-authority";

	public static final String FREE_USER = "USER";
	
	public static final String PAID_USER = "SUBSCRIBER";

	public static final String ADMIN = "ADMIN";

	public static final String ROLE_PREFIX = "ROLE_";

	public static final String MAKE_ROLE(String role) {
		return ROLE_PREFIX + role;
	}
}