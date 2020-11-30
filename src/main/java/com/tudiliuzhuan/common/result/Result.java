package com.tudiliuzhuan.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 返回数据结果描述
 *
 * @author Zengzhong on 2019-08-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Result<T> extends ResultBase {
    private T data;

    public static <T> Result<T> wrap(boolean success, Integer code, String message, T data) {
        Result<T> result = new Result<T>();
        result.setSuccess(success);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> wrapSuccessfulResult(T data) {
        Result<T> result = new Result<T>();
        result.data = data;
        result.setSuccess(true);
        result.setCode(1);
        return result;
    }

    public static <T> Result<T> wrapSuccess(String message) {
        Result<T> result = new Result<T>();
        result.setSuccess(true);
        result.setMessage(message);
        result.setCode(1);
        return result;
    }

    public static <T> Result<T> wrapSuccess(Integer code, String message, T data) {
        Result<T> result = new Result<T>();
        result.setSuccess(true);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> wrapSuccessfulResult(String message, T data) {
        Result<T> result = new Result<T>();
        result.data = data;
        result.setSuccess(true);
        result.setCode(1);
        result.setMessage(message);
        return result;
    }


    public static <T> Result<T> wrapError() {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setCode(0);
        return result;
    }

    public static <T> Result<T> wrapError(T data) {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setCode(0);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> wrapError(Integer code, String message) {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> wrapErrorResult(Integer code, String message, T data) {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public T getData() {
        return this.data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}