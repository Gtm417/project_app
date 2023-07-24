package org.example.projectapp.restclient;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.example.projectapp.auth.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenRequestInterceptor implements RequestInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtTokenRequestInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String jwtToken = jwtTokenProvider.createServiceToken();
        requestTemplate.header("Authorization", "Bearer " + jwtToken);
    }
}
