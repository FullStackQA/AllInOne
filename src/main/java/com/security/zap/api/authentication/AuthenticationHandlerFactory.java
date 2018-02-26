package com.security.zap.api.authentication;

import com.security.zap.api.model.AuthenticationInfo;
import com.security.zap.utils.ZapInfo;
import org.zaproxy.clientapi.core.ClientApi;

/**
 * Factory to create the correct {@link AuthenticationHandler} based on the given {@link AuthenticationInfo} instance.
 * 

 */
public final class AuthenticationHandlerFactory {

	public static AuthenticationHandler makeHandler(ClientApi api, ZapInfo zapInfo, AuthenticationInfo authenticationInfo) {
		if (authenticationInfo == null) {
			return null;
		}
		
		switch (authenticationInfo.getType()) {
			case FORM:     return new FormAuthenticationHandler(api, zapInfo, authenticationInfo);
			case CAS:      return new CasAuthenticationHandler(api, zapInfo, authenticationInfo);
			case SELENIUM: return new SeleniumAuthenticationHandler(api, zapInfo, authenticationInfo);
			case HTTP:     return new HttpAuthenticationHandler(api, zapInfo, authenticationInfo);
			default:       return null;
		}
	}

	private AuthenticationHandlerFactory() {}
	
}
