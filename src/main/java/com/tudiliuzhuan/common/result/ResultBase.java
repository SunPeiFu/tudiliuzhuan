package com.tudiliuzhuan.common.result;

import lombok.Data;

/**
 * 各种请求，函数返回结果
 *
 * @author Zengzhong on 2019-08-01
 */
@Data
public class ResultBase {
    private boolean success;
    private Integer code;
    private String message;
}