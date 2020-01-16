package com.metro.nccc.migration.controller;

import com.metro.nccc.migration.service.usercenter.SyncUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-7 下午5:21
 */
@RestController
@RequestMapping("/migration")
public class StartController {
    @Autowired
    private SyncUserService syncUserService;
    @GetMapping("/sync")
    public void sync() throws InterruptedException {
        syncUserService.sync();
    }
    @GetMapping("/async/{index}/{count}")
    public void async(@PathVariable int index,@PathVariable int count) {
        syncUserService.async(index,count);
    }
}
