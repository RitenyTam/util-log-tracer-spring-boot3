package com.riteny.log.tracer;

import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

public class RequestDataHolder {

    private static final ThreadLocal<String> requestIdHolder = new NamedThreadLocal<>("ApiRequestId");
    private static final ThreadLocal<String> inheritableRequestIdHolder = new NamedInheritableThreadLocal<>("InheritableApiRequestId");

    private static final ThreadLocal<String> entranceTypeHolder = new NamedThreadLocal<>("EntranceType");
    private static final ThreadLocal<String> inheritableEntranceTypeHolder = new NamedInheritableThreadLocal<>("InheritableEntranceType");


    public static String getApiRequestId() {
        return requestIdHolder.get() == null ? inheritableRequestIdHolder.get() : requestIdHolder.get();
    }

    public static void setRequestId(String apiContext) {
        requestIdHolder.set(apiContext);
        inheritableRequestIdHolder.set(apiContext);
    }

    public static void resetRequestId() {
        requestIdHolder.remove();
        inheritableRequestIdHolder.remove();
    }

    public static String getEntranceType() {
        return entranceTypeHolder.get() == null ? inheritableEntranceTypeHolder.get() : entranceTypeHolder.get();
    }

    public static void setEntranceType(String entranceType) {
        entranceTypeHolder.set(entranceType);
        inheritableEntranceTypeHolder.set(entranceType);
    }

    public static void resetEntranceType() {
        entranceTypeHolder.remove();
        inheritableEntranceTypeHolder.remove();

    }
}
