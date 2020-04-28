package com.metro.nccc.migration.service.auth;

import com.metro.nccc.migration.dao.auth.AuthMapper;
import com.metro.nccc.migration.dao.usercenter.UsercenterMapper;
import com.metro.nccc.migration.model.po.auth.UserRoleEntity;
import com.metro.nccc.migration.model.po.usercenter.UserEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-4-8 下午8:39
 */
@Service
public class AuthUserRoleService {
    @Resource
    UsercenterMapper usercenterMapper;
    @Resource
    AuthMapper authMapper;
    public boolean run(){
        int total = usercenterMapper.max();
        System.out.println("total：" + total);
        for (int j = 1; j < total; j++) {
            int count = usercenterMapper.count(j);
            if(count>1){
                System.out.println("id:"+j+" 重复次数："+count);
            }
            UserEntity userEntity = usercenterMapper.queryById(j);
            if(userEntity == null){
                continue;
            }
            if(userEntity.getId() == 35603){
                System.out.println("----");
            }
            UserRoleEntity userRoleEntity = authMapper.queryUserRole(userEntity.getId());
            if(userRoleEntity == null){
                authMapper.insertUserRole(userEntity.getId(),userEntity.getEmployeeNo());
                System.out.println(String.format("当前用户:%s ,id：%s ---新加",userEntity.getName(),userEntity.getId()));
            }else {
                authMapper.updateUserRole(userRoleEntity.getId(),userEntity.getEmployeeNo());
                System.out.println(String.format("当前用户:%s ,id：%s ---更新",userEntity.getName(),userEntity.getId()));
            }
        }
        return true;
    }
}
