package com.security.zap.api.authentication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.security.zap.api.ZapHelper;
import com.security.zap.api.exception.ZapClientException;
import com.security.zap.api.model.AuthenticationInfo;
import com.security.zap.utils.ZapInfo;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

/**
 * Class to handle form based authentication via ZAP.
 * 

 */
public class FormAuthenticationHandler extends AbstractAuthenticationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(FormAuthenticationHandler.class);
	
	private static final String FORM_AUTHENTICATION_TYPE = "formBasedAuthentication";
	
	private static final String LOGIN_URL_PARAM = "loginUrl";
	private static final String LOGIN_REQUEST_DATA_PARAM = "loginRequestData";
	
	protected FormAuthenticationHandler(ClientApi api, ZapInfo zapInfo, AuthenticationInfo authenticationInfo) {
		super(api, zapInfo, authenticationInfo);
	}

	@Override
	protected void setupAuthentication(String targetUrl) {
		setFormAuthenticationMethod();
		createAndEnableUser();
		setupUserCredentials();
		enableForcedUserMode();
	}
	
	private void setFormAuthenticationMethod() {
		try {
			String encodedLoginUrl = URLEncoder.encode(getAuthenticationInfo().getLoginUrl(), UTF_8);
			String encodedLoginRequestData = URLEncoder.encode(getAuthenticationInfo().getFullLoginRequestData(), UTF_8);
			String authParams = LOGIN_URL_PARAM + "=" + encodedLoginUrl + 
					"&" + LOGIN_REQUEST_DATA_PARAM + "=" + encodedLoginRequestData;
			
			LOGGER.debug("Setting form authentication method with params: {}", authParams);
			ApiResponse response = getApi().authentication.setAuthenticationMethod(
							getApiKey(), ZAP_DEFAULT_CONTEXT_ID, FORM_AUTHENTICATION_TYPE, authParams);
			ZapHelper.validateResponse(response, "Set form authentication method.");
		} catch (ClientApiException | UnsupportedEncodingException e) {
			LOGGER.error("Error setting up form authentication method.", e);
			throw new ZapClientException(e);
		}
	}
	
}
