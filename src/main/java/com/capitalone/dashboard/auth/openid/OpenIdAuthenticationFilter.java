package com.capitalone.dashboard.auth.openid;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.auth.AuthenticationResultHandler;
import com.capitalone.dashboard.client.RestClient;


public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private OpenIdAuthenticationService openIdAuthenticationService;
    protected RestClient restClient;
    
    @Autowired
    public OpenIdAuthenticationFilter(String defaultFilterProcessesUrl) {
    	super(defaultFilterProcessesUrl);
    }

    @Bean
    public OpenIdAuthenticationFilter buildOpenIdAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager,
                                      AuthenticationResultHandler authenticationResultHandler, RestClient restClient) {
    	OpenIdAuthenticationFilter openIdAuthenticationFilter = new OpenIdAuthenticationFilter(defaultFilterProcessesUrl);
    	openIdAuthenticationFilter.setAuthenticationManager(authenticationManager);
    	openIdAuthenticationFilter.setAuthenticationSuccessHandler(authenticationResultHandler);
        this.restClient = restClient;
        return openIdAuthenticationFilter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        return openIdAuthenticationService.getAuthentication(httpServletRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        openIdAuthenticationService.addAuthentication(response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OpenId SSO Authentication Failed");
    }
}
