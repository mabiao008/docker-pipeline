package com.github.jadepeng.pipeline.agent.client;

import com.github.jadepeng.pipeline.agent.security.jwt.TokenProvider;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer";

    private static String DEFAULT_JWT_TOKEN;

    @Autowired
    public UserFeignClientInterceptor(TokenProvider tokenProvider){
        DEFAULT_JWT_TOKEN = tokenProvider.createFeignToken();
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header(AUTHORIZATION_HEADER,
                        String.format("%s %s", BEARER, DEFAULT_JWT_TOKEN));
    }
}
