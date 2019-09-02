package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfoAndCard;
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

    @Select("select u.*,t.typename as cardtypename,t.ssid as cardtypessid,ut.cardnum as cardnum,ut.ssid as usertotypessid  from police_userinfo u " +
            "left join police_userinfototype ut on ut.userssid=u.ssid " +
            "left join `police_cardtype` t on t.ssid=ut.cardtypessid " +
            " where 1=1 ${ew.sqlSegment}")
    List<UserInfo> getUserByCard(@Param("ew") EntityWrapper ew);


    @Select(" select DISTINCT(u.ssid),u.*,ctu.usertotypessid as usertotypessid from police_userinfo u  " +
            " left join police_casetouserinfo ctu on ctu.userssid=u.ssid " +
            " left join police_case c on c.ssid=ctu.casessid "+
            " where 1=1 ${ew.sqlSegment}")
    List<UserInfo> getUserByCase(@Param("ew") EntityWrapper ew);

    //单纯获取用户全部的证件以及证件号码
    @Select(" select ut.*,t.typename as cardtypename   from police_userinfototype ut " +
            " left join police_userinfo u on ut.userssid=u.ssid " +
            " left join police_cardtype t on t.ssid=ut.cardtypessid " +
            " where 1=1 ${ew.sqlSegment}")
    List<UserInfoAndCard> getCardByUser(@Param("ew") EntityWrapper ew);



}
