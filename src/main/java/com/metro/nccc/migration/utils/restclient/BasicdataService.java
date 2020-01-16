package com.metro.nccc.migration.utils.restclient;

import com.metro.nccc.migration.utils.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-11-8 下午3:57
 */
@FeignClient(name = "basic", url = "http://10.253.100.11:32414/")
public interface BasicdataService {
    @RequestMapping("/basic_access_token")
    BaseResponse<String> accessToken(@RequestParam String agentId, @RequestParam String appId, @RequestParam String key);
}
