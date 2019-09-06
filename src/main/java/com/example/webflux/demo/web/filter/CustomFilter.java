package com.example.webflux.demo.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @ClassName CustomFilter
 * @Description: Filter, 好像没办法设置拦截路径
 * @Author zhangjiashuai
 * @Date 2019/8/28
 **/
@Slf4j
@Component
@Order(-1)
public class CustomFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String authHeader = "h2";
        String h2 = request.getHeaders().getFirst(authHeader);
        if (h2 != null) {
            return webFilterChain.filter(serverWebExchange);
        }
        log.error("can not accept this request without header [{}]", authHeader);
        ServerHttpRequest authErrorReq = request.mutate().path("/auth/error").build();
        ServerWebExchange authErrorExchange = serverWebExchange.mutate().request(authErrorReq).build();
        return webFilterChain.filter(authErrorExchange);
    }
}
