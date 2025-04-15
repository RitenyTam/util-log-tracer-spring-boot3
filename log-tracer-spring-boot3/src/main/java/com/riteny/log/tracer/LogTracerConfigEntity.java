package com.riteny.log.tracer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Riteny
 * 2020/11/5  11:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogTracerConfigEntity {

    private String baseDir = "logs";

    private Integer maxHistory = 30;

    private String maxFileSize = "50MB";
}
