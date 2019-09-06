package com.example.webflux.demo.web.controller;

import com.example.webflux.demo.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description: TestController
 * @Author zhangjiashuai
 * @Date 2019/8/28
 **/
@Slf4j
@RestController
public class TestController {

    @GetMapping("/test1/{param1}")
    public String test1(@PathVariable String param1) {
        log.info("param1: {}", param1);
        if("error".equals(param1)) {
            throw new ServiceException("param error");
        }
        return "test1 successfully completed.";
    }

    @GetMapping("/test2/{param2}")
    public String test2(@PathVariable String param2, ServerHttpRequest request, ServerHttpResponse response) {
        RequestPath path = request.getPath();
        String p1 = request.getQueryParams().getFirst("p1");
        HttpCookie c1 = request.getCookies().getFirst("c1");
        String h1 = request.getHeaders().getFirst("h1");
        log.info("path: {}, pathVariable: {}, queryParam: {}, cookie: {}, header: {}", path, param2, p1, c1.getValue(), h1);
        return "test2 successfully completed.";
    }

    @RequestMapping("/auth/error")
    public String authError() {
        return "not authorized";
    }
}
