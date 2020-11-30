package com.tudiliuzhuan.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 作者:  sunpeifu
 * 日期:  2020/5/25
 * 描述:
 * @author sunpeifu
 */
@Configuration
@Data
public class MiniProgramConfig {

    @Value(value = "${miniProgram.appId}")
    private String appId;

    @Value(value = "${miniProgram.secret}")
    private String secret;

}
