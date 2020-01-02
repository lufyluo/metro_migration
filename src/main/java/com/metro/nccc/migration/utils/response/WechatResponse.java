package com.metro.nccc.migration.utils.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 下午2:57
 */
public class WechatResponse<T> implements Serializable {
    protected Integer errcode;
    protected String errmsg;
    protected T data;
    @JsonIgnore
    protected ResponseStatus status;

    public Boolean isSuccessed(){
        return errcode==0&& ObjectUtils.nullSafeEquals(errcode,"ok");
    }

    public T getData() {
        return data;
    }
}
