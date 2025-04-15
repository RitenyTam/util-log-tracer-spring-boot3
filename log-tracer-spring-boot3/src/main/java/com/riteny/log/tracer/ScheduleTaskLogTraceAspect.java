package com.riteny.log.tracer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Aspect
@Component
public class ScheduleTaskLogTraceAspect implements HandlerInterceptor {

    private final Logger logger;

    @Autowired
    public ScheduleTaskLogTraceAspect(LogTracerProperties properties) {
        this.logger = LogTracerFactory.getLogger("log-tracer-task", properties.getApiBaseDir(), properties.getApiMaxHistory(), properties.getApiMaxFileSize());
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        RequestDataHolder.setRequestId(UUID.randomUUID().toString());
        RequestDataHolder.setEntranceType("scheduleTask");

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Scheduled scheduledAnnotation = method.getAnnotation(Scheduled.class);
        String cron = scheduledAnnotation.cron();
        Long fixedDelay = scheduledAnnotation.fixedDelay();
        String fixedDelayString = scheduledAnnotation.fixedDelayString();
        Long fixedRate = scheduledAnnotation.fixedRate();
        String fixedRateString = scheduledAnnotation.fixedRateString();

        String requestId = RequestDataHolder.getApiRequestId();
        long startTime = System.currentTimeMillis();

        logger.info(new BaseLogFormat<>(sdf.format(startTime), requestId, "scheduleTask", "scheduleTask",
                new ScheduleTaskLogFormat(cron, fixedDelay, fixedDelayString, fixedRate, fixedRateString)).toJsonString());

        try {
            //记录执行完成后，返回的参数
            Object object = joinPoint.proceed();
            logger.info(new BaseLogFormat<>(sdf.format(startTime), requestId, "scheduleTask", "scheduleTask",
                    new ScheduleTaskLogFormat(cron, fixedDelay, fixedDelayString, fixedRate, fixedRateString
                            , System.currentTimeMillis() - startTime)).toJsonString());
            return object;
        } catch (Exception e) {
            //记录异常时的原因
            logger.info(new BaseLogFormat<>(sdf.format(startTime), requestId, "scheduleTask", "scheduleTask",
                    new ScheduleTaskLogFormat(cron, fixedDelay, fixedDelayString, fixedRate, fixedRateString
                            , e.getMessage(), System.currentTimeMillis() - startTime)).toJsonString());
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RequestDataHolder.resetRequestId();
            RequestDataHolder.resetEntranceType();
        }
    }
}
