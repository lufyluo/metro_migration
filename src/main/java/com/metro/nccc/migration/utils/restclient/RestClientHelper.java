package com.metro.nccc.migration.utils.restclient;


import com.metro.nccc.migration.utils.response.BaseResponse;
import com.metro.nccc.migration.utils.response.ResponseStatus;

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
            return null;
        }
    }

}
