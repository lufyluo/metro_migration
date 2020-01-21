package com.metro.nccc.migration.controller;

import com.metro.nccc.migration.service.SyncStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-19 下午4:33
 */
@RestController
@RequestMapping("/migration/sync/station")
public class SyncStationController {
    @Autowired
    SyncStationService syncStationService;
    @GetMapping("/code")
    public Boolean syncCode(){
        return syncStationService.syncCode();
    }
}
