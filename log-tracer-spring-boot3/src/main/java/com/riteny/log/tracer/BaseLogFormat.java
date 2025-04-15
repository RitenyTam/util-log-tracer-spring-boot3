package com.riteny.log.tracer;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseLogFormat<T> {

    @JSONField(ordinal = 1)
    private String systemDate;

    @JSONField(ordinal = 2)
    private String threadName;

    @JSONField(ordinal = 3)
    private String requestId;


    @JSONField(ordinal = 4)
    private String entranceType;

    @JSONField(ordinal = 5)
    private String type;

    @JSONField(ordinal = 6)
    private String event;

    @JSONField(ordinal = 7)
    private T msg;

    public BaseLogFormat(String systemDate, String requestId, String type, String event, T msg) {
        this.systemDate = systemDate;
        this.requestId = requestId;
        this.type = type;
        this.event = event;
        this.msg = msg;
        this.threadName = Thread.currentThread().getName();
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}
