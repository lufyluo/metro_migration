package com.metro.nccc.migration.utils.restclient;

import com.metro.nccc.migration.utils.response.WechatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 下午2:59
 */
@FeignClient(name = "wechat", url = "https://qyapi.weixin.qq.com/")
public interface WechatHttpClient {
    @RequestMapping("/cgi-bin/user/simplelist")
    WechatResponse simplelist(@RequestParam String access_token, @RequestParam Long department_id, @RequestParam int fetch_child);
    @RequestMapping("/cgi-bin/user/list")
    WechatResponse list(@RequestParam String access_token, @RequestParam Long department_id, @RequestParam int fetch_child);
}
