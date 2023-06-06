package com.judy.customdummy.common;

import com.judy.customdummy.HttpUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Slf4j
public class ApiInterceptor extends HandlerInterceptorAdapter {

    private final Bucket bucket;

    public ApiInterceptor() {
        Bandwidth limit = Bandwidth.classic(1, Refill.intervally(1, Duration.ofMinutes(1))); // bucket의 크기는 1, 1회 충전시 1회, 1분마다 충전
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (bucket.tryConsume(1)) {
            return true;
        }
        log.info("{} - API 요청 가능 횟수 초과", HttpUtil.getIp(request));
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        return false;
    }

}
