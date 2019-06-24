package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * InnoDB free: 90112 kB Mapper 接口
 * </p>
 *
 * @author Admin
 * @since 2019-04-22
 */
public interface Police_userinfoMapper extends BaseMapper<Police_userinfo> {

    @Select("select u.*,t.typename as cardtypename,ut.cardnum as cardnum  from police_userinfo u " +
            "left join police_userinfototype ut on ut.userssid=u.ssid " +
            "left join `police_cardtype` t on t.ssid=ut.cardtypessid " +
            " where 1=1 ${ew.sqlSegment}")
    List<UserInfo> getUserByCard(@Param("ew") EntityWrapper ew);



}
