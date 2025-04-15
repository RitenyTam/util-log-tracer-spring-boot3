package com.riteny.log.tracer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Riteny
 * 2020/9/18  17:19
 */
@Data
@ConfigurationProperties(prefix = "log.tracer")
public class LogTracerProperties {

    private String apiBaseDir = "logs";

    private Integer apiMaxHistory = 30;

    private String apiMaxFileSize = "50MB";

    private String otherBaseDir = "logs";

    private Integer otherMaxHistory = 30;

    private String otherMaxFileSize = "50MB";

    private String taskBaseDir = "logs";

    private Integer taskMaxHistory = 30;

    private String taskMaxFileSize = "50MB";
}
