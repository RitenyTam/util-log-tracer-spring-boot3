package com.riteny.log.tracer;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiLogFormat {

    @JSONField(ordinal = 1)
    private String action;

    @JSONField(ordinal = 2)
    private String method;

    @JSONField(ordinal = 3)
    private String userIp;

    @JSONField(ordinal = 4)
    private Object userAgent;

    @JSONField(ordinal = 5)
    private Object request;

    @JSONField(ordinal = 6)
    private Object response;

    @JSONField(ordinal = 7)
    private Long usedTime;

    public ApiLogFormat(String action, String method, String userIp, Object userAgent, Object request) {
        this.action = action;
        this.method = method;
        this.userIp = userIp;
        this.userAgent = userAgent;
        this.request = request;
    }

    public ApiLogFormat(String action, String method, String userIp, Object userAgent, Object request, Object response, Long usedTime) {
        this.action = action;
        this.method = method;
        this.userIp = userIp;
        this.userAgent = userAgent;
        this.request = request;
        this.response = response;
        this.usedTime = usedTime;
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}
