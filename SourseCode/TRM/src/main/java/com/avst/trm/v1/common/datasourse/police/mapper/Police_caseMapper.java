package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
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
public interface Police_caseMapper extends BaseMapper<Police_case> {
    @Select("select c.*,u.username,u.ssid as userssid from police_case c left join police_userinfo u on u.ssid=c.userssid where 1=1 ${ew.sqlSegment}")
    List<CaseAndUserInfo> getArraignmentList(Page page, @Param("ew") EntityWrapper ew);


    @Select("select count(c.ssid) from police_case c left join police_userinfo u on u.ssid=c.userssid where 1=1 ${ew.sqlSegment}")
    int countgetArraignmentList( @Param("ew")EntityWrapper ew);

    @Select("select c.*,u.username,u.ssid as userssid from police_arraignment a " +
            "  left join police_casetoarraignment cr on cr.arraignmentssid=a.ssid " +
            "  left join police_case c on c.ssid=cr.casessid" +
            "  left join police_record r on r.ssid=a.recordssid " +
            "  left join police_userinfo u on u.ssid=c.userssid" +
            "  where 1=1 ${ew.sqlSegment} ")
    CaseAndUserInfo getCaseByRecordSsid( @Param("ew")EntityWrapper ew);

    @Select("select c.*,u.username,u.ssid as userssid from police_case c left join police_userinfo u on u.ssid=c.userssid where 1=1 ${ew.sqlSegment}")
    List<CaseAndUserInfo> getCaseByUserSsid(@Param("ew") EntityWrapper ew);

}
