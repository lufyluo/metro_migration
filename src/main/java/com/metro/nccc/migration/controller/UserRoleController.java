package com.metro.nccc.migration.controller;

import com.metro.nccc.migration.service.auth.AuthUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-4-8 下午8:53
 */
@RestController
@RequestMapping("/user/role")
public class UserRoleController {
    @Autowired
    AuthUserRoleService authUserRoleService;

    @GetMapping("/run")
    public boolean run(){
        return authUserRoleService.run();
    }
}
