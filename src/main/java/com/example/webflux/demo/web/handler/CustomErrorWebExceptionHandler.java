package com.example.webflux.demo.web.handler;

import com.example.webflux.demo.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;

/**
 * @ClassName CustomErrorWebExceptionHandler
 * @Description: 自定义异常处理类<br>必须设置HttpMessageWriter才能向前端返回消息
 * @Author zhangjiashuai
 * @Date 2019/8/28
 **/
@Slf4j
@Component
@Order(-2)
public class CustomErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {


    public CustomErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions
                .route(request -> {
                    Throwable error = errorAttributes.getError(request);
                    Map<String, Object> map = errorAttributes.getErrorAttributes(request, true);
                    log.info("errorAttributes map: {}", map);
                    return (error instanceof ServiceException);
                }, this::renderErrorResponse);
    }

    @Override
    public void afterPropertiesSet() {
        Jackson2JsonEncoder jackson2JsonEncoder = new Jackson2JsonEncoder();
        jackson2JsonEncoder.setStreamingMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        HttpMessageWriter<?> writer = new EncoderHttpMessageWriter<>(jackson2JsonEncoder);
        super.setMessageWriters(Arrays.asList(writer));
    }

    private Mono<ServerResponse> renderErrorResponse(
            ServerRequest request) {
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);
        // 这里可以自定义处理逻辑
        errorPropertiesMap.put("msg1", "this is the custom msg");
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(errorPropertiesMap));
    }
}
