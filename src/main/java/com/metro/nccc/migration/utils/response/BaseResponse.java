package com.metro.nccc.migration.utils.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-8-14 下午5:22
 */
@Getter
public class BaseResponse<T> implements Serializable {
    protected String code;
    protected String msg;
    protected T data;
    @JsonIgnore
    protected ResponseStatus status;

    public BaseResponse() {
    }

    public BaseResponse(ResponseStatus status) {
        this.code = status.getCode();
        this.msg = status.getDesc();
        this.status = status;
    }

    public BaseResponse(ResponseStatus status, String msg) {
        this.code = status.getCode();
        this.msg = msg;
        this.status = status;
    }

    public BaseResponse(ResponseStatus status, T data) {
        this.code = status.getCode();
        this.msg = status.getDesc();
        this.status = status;
        this.data = data;
    }

    public BaseResponse(ResponseStatus status, T data, String msg) {
        this.code = status.getCode();
        this.msg = msg;
        this.status = status;
        this.data = data;
    }


    public BaseResponse<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public BaseResponse<T> success(@Nullable String msg, T data) {
        if (StringUtils.isEmpty(msg)) {
            return new BaseResponse(ResponseStatus.SUCCESS, data);
        }
        return new BaseResponse(ResponseStatus.SUCCESS, data, msg);
    }

    public BaseResponse<T> success(T data) {
        return new BaseResponse(ResponseStatus.SUCCESS, data);
    }

    public BaseResponse<T> failure(String msg, T data) {
        if (StringUtils.isEmpty(msg)) {
            return new BaseResponse(ResponseStatus.FAILURE, data);
        }
        return new BaseResponse(ResponseStatus.FAILURE, data, msg);
    }

    public BaseResponse<T> failure(String msg, ResponseStatus status, T data) {
        if (StringUtils.isEmpty(msg)) {
            return new BaseResponse(status, data);
        }
        return new BaseResponse(status, data, msg);
    }

    public BaseResponse<T> failure(T data) {
        return new BaseResponse(ResponseStatus.FAILURE, data);
    }

    public BaseResponse<T> unauthorizied(String msg, T data) {
        if (StringUtils.isEmpty(msg)) {
            return new BaseResponse(ResponseStatus.UNAUTHORIZED, data);
        }
        return new BaseResponse(ResponseStatus.UNAUTHORIZED, data, msg);
    }

    public BaseResponse<T> unauthorizied(T data) {
        return new BaseResponse(ResponseStatus.UNAUTHORIZED, data);
    }

}
