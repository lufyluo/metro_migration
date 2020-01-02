package com.metro.nccc.migration.utils.restclient;

import com.metro.nccc.authority.infrastructure.interceptor.AuthException;
import com.metro.nccc.authority.infrastructure.response.BaseResponse;
import com.metro.nccc.authority.infrastructure.response.ResponseStatus;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-11-8 下午4:03
 */
public class RestClientHelper {

    public static <T> T getRestData(BaseResponse<T> response) {
        if (response.getCode().equals(ResponseStatus.SUCCESS.getCode())) {
            return response.getData();
        } else {
            throw new AuthException(ResponseStatus.valueOfCode(response.getCode()), "api调用发生错误： " + response.getMsg());
        }
    }

}
