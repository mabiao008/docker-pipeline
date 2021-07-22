package com.github.jadepeng.pipeline.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.github.jadepeng.pipeline.core.dto.BasePayloadResponse;
import com.github.jadepeng.pipeline.core.dto.RetCode;

/**
 * 统一返回格式
 * @author jqpeng
 */
@Configuration
public class GlobalResultResponseConfig {

    @RestControllerAdvice
    static class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object result, MethodParameter methodParameter,
                                      MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                      ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
            if (!(result instanceof BasePayloadResponse)) {
                return BasePayloadResponse.success(result);
            }

            return result;
        }
    }
}
