package com.riteny.log.tracer;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

@Service
public class MsgLogTracer {

    public static Logger apiLogger = LogTracerFactory.getLogger("log-tracer-api");
    public static Logger taskLogger = LogTracerFactory.getLogger("log-tracer-task");
    public static Logger otherLogger = null;

    @Autowired
    public MsgLogTracer(LogTracerProperties properties) {
        otherLogger = LogTracerFactory.getLogger("log-tracer-other"
                , properties.getApiBaseDir(), properties.getApiMaxHistory(), properties.getApiMaxFileSize());
    }

    public static void info(String event, String msg, Object... args) {
        Logger logger = getLoggerByEntrance(RequestDataHolder.getEntranceType());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BaseLogFormat<String> baseLogFormat = new BaseLogFormat<>(sdf.format(System.currentTimeMillis())
                , RequestDataHolder.getApiRequestId(), RequestDataHolder.getEntranceType(), event, msg);
        logger.info(baseLogFormat.toJsonString(), args);
    }

    public static void error(String event, String msg, Exception e) {
        Logger logger = getLoggerByEntrance(RequestDataHolder.getEntranceType());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BaseLogFormat<String> baseLogFormat = new BaseLogFormat<>(sdf.format(System.currentTimeMillis())
                , RequestDataHolder.getApiRequestId(), RequestDataHolder.getEntranceType(), event, msg);
        logger.error(baseLogFormat.toJsonString(), e);
    }

    private static Logger getLoggerByEntrance(String entrance) {
        if (StringUtils.isEmpty(entrance)) {
            return otherLogger;
        }
        switch (entrance) {
            case "api":
                return apiLogger;
            case "task":
                return taskLogger;
            default:
                return otherLogger;
        }
    }
}
