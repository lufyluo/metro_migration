package com.metro.nccc.migration.utils.restclient;

import com.metro.nccc.migration.utils.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-11-8 下午3:57
 */
@FeignClient(name = "basic",url = "10.253.100.11:32414/")
public interface BasicdataService {
    @GetMapping("/basic_access_token")
    public BaseResponse<String> accessToken(String agentId,String appId,String key);
}
