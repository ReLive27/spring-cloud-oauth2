package com.relive.config;

import feign.RequestTemplate;

/**
 * Feign 拦截器
 *
 * @Author ReLive
 * @Date 2021/3/5-19:17
 */
public class FeignInterceptor {
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json");
    }
}
