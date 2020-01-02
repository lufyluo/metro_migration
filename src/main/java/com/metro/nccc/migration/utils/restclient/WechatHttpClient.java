package com.metro.nccc.migration.utils.restclient;

import com.metro.nccc.migration.utils.response.WechatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 下午2:59
 */
@FeignClient(name = "wechat",url = "https://qyapi.weixin.qq.com/")
public interface WechatHttpClient{
    @GetMapping("/cgi-bin/user/simplelist")
    WechatResponse simplelist(String access_token,Long department_id,int fetch_child);
}
