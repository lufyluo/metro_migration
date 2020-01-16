package com.metro.nccc.migration.service.usercenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 下午5:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SyncUserServiceTest {
    @Autowired
    SyncUserService syncUserService;

    @Test
    public void sync() {
        try {
            syncUserService.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}