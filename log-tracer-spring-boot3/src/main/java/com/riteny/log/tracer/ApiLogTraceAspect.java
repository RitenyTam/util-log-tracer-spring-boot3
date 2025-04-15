package com.riteny.log.tracer;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Aspect
@Component
public class ApiLogTraceAspect implements HandlerInterceptor {

    private final Logger logger;
    private final Logger apiLogger = LogTracerFactory.getLogger("api");

    @Autowired
    public ApiLogTraceAspect(LogTracerProperties properties) {
        this.logger = LogTracerFactory.getLogger("log-tracer-api", properties.getApiBaseDir(), properties.getApiMaxHistory(), properties.getApiMaxFileSize());
    }


    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) "
            + "|| @annotation(org.springframework.web.bind.annotation.PostMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        RequestDataHolder.setRequestId(UUID.randomUUID().toString());
        RequestDataHolder.setEntranceType("api");
        Object[] args = joinPoint.getArgs();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        //记录执行开始时间，访问路径，入参，日期和访问者IP信息等
        HttpServletRequest request = attributes.getRequest();
        String requestId = RequestDataHolder.getApiRequestId();
        long startTime = System.currentTimeMillis();
        String requestURI = request.getRequestURI();
        String ip = request.getRemoteAddr();
        String methodStr = request.getMethod();
        String useragent = request.getHeader("User-Agent");
        logger.info(new BaseLogFormat<>(sdf.format(startTime), requestId, "api", "api",
                new ApiLogFormat(requestURI, methodStr, ip, useragent, args)).toJsonString());
        apiLogger.info("#Request : {} #Method : {} #Remote IP : {} #Param : {} #RequestId : {} #userAgent {}"
                , requestURI, methodStr, ip, args, requestId, useragent);

        try {
            //记录执行完成后，返回的参数
            Object object = joinPoint.proceed();
            logger.info(new BaseLogFormat<>(sdf.format(startTime), requestId, "api", "api",
                    new ApiLogFormat(requestURI, methodStr, ip, useragent, args, object
                            , System.currentTimeMillis() - startTime)).toJsonString());
            apiLogger.info("#Response : {} #Method : {} #Remote IP : {} #Result : {} #RequestId : {} #userAgent {} #usedTime : {}"
                    , requestURI, methodStr, ip, object, requestId, useragent, System.currentTimeMillis() - startTime);

            return object;
        } catch (Exception e) {
            //记录异常时的原因
            logger.info(new BaseLogFormat<>(sdf.format(startTime), requestId, "api", "api",
                    new ApiLogFormat(requestURI, methodStr, ip, useragent, args, e.getMessage()
                            , System.currentTimeMillis() - startTime)).toJsonString());
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RequestDataHolder.resetRequestId();
            RequestDataHolder.resetEntranceType();
        }
    }
}
