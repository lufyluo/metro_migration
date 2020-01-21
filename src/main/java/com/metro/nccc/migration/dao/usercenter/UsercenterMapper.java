package com.metro.nccc.migration.dao.usercenter;

import com.metro.nccc.migration.model.po.usercenter.UserEntity;
import com.metro.nccc.migration.model.vo.usercenter.WechatUserInfo;
import com.metro.nccc.migration.utils.ibatis.ArrayObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-2 上午10:48
 */
@Mapper
public interface UsercenterMapper {
    List<UserEntity> queryByWechatIds(@Param("wechatIds") List<String> wechatIds);

    Integer batchInsert(@Param("userInfos") List<WechatUserInfo> userInfos);

    Integer updateAvatar(@Param("userEntities") List<UserEntity> userEntities);
}
