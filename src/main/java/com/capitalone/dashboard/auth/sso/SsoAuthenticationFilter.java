package com.capitalone.dashboard.auth.sso;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.capitalone.dashboard.auth.AuthenticationResultHandler;

public class SsoAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final Log LOGGER = LogFactory.getLog(SsoAuthenticationFilter.class);
	
	@Autowired
	private SsoAuthenticationService ssoAuthenticationService;
	
	@Autowired
	public SsoAuthenticationFilter() {
		super();
	}
	
	@Bean
	public SsoAuthenticationFilter buildSsoAuthenticationFilter(String path, AuthenticationManager authManager, AuthenticationResultHandler authenticationResultHandler) {
		
		SsoAuthenticationFilter filter = new SsoAuthenticationFilter();
		filter.setAuthenticationManager(authManager);
		filter.setAuthenticationSuccessHandler(authenticationResultHandler);
		filter.setFilterProcessesUrl(path);
		return filter;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		Authentication authenticated = null;
		Map<String, String> headersMap = new HashMap<>();
		
		if(request.getHeader("cookiesheader") == null) {
			LOGGER.debug("no header found for user details");
			return authenticated;
		}
		
		Enumeration<String> headers = request.getHeaderNames();
		
		if(headers == null || headers.hasMoreElements() == false) {
			return authenticated;
		}
		while(headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			headersMap.put(headerName, request.getHeader(headerName));
		}
		
		authenticated = ssoAuthenticationService.getAuthenticationFromHeaders(headersMap);
    	return authenticated;
	}
}
