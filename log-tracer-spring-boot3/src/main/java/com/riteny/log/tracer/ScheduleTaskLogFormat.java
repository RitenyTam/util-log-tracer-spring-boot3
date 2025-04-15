package com.riteny.log.tracer;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScheduleTaskLogFormat {

    @JSONField(ordinal = 1)
    private String cron;

    @JSONField(ordinal = 2)
    private Long fixedDelay;

    @JSONField(ordinal = 3)
    private String fixedDelayString;

    @JSONField(ordinal = 4)
    private Long fixedRate;

    @JSONField(ordinal = 5)
    private String fixedRateString;

    @JSONField(ordinal = 6)
    private String msg;

    @JSONField(ordinal = 7)
    private Long usedTime;

    public ScheduleTaskLogFormat(String fixedRateString, Long fixedRate, String fixedDelayString, Long fixedDelay, String cron) {
        this.fixedRateString = fixedRateString;
        this.fixedRate = fixedRate;
        this.fixedDelayString = fixedDelayString;
        this.fixedDelay = fixedDelay;
        this.cron = cron;
    }

    public ScheduleTaskLogFormat(String cron, Long fixedDelay, String fixedDelayString, Long fixedRate, String fixedRateString, Long usedTime) {
        this.cron = cron;
        this.fixedDelay = fixedDelay;
        this.fixedDelayString = fixedDelayString;
        this.fixedRate = fixedRate;
        this.fixedRateString = fixedRateString;
        this.usedTime = usedTime;
    }

    public ScheduleTaskLogFormat(String cron, Long fixedDelay, String fixedDelayString, Long fixedRate, String fixedRateString, String msg, Long usedTime) {
        this.cron = cron;
        this.fixedDelay = fixedDelay;
        this.fixedDelayString = fixedDelayString;
        this.fixedRate = fixedRate;
        this.fixedRateString = fixedRateString;
        this.msg = msg;
        this.usedTime = usedTime;
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}
