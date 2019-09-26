package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_case;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Case;
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
    @Select(" select DISTINCT(c.ssid),c.* from police_case c " +
            " left join police_casetouserinfo cu on cu.casessid=c.ssid " +
            " left join police_userinfo u on u.ssid=cu.userssid "+
            " where 1=1 ${ew.sqlSegment} ")
    List<Case> getCaseList(Page page,@Param("ew") EntityWrapper ew);

    @Select(" select COUNT(DISTINCT(c.ssid)) from police_case c " +
            " left join police_casetouserinfo cu on cu.casessid=c.ssid " +
            " left join police_userinfo u on u.ssid=cu.userssid "+
            " where 1=1 ${ew.sqlSegment}")
    int countgetCaseList( @Param("ew")EntityWrapper ew);


    @Select("select c.* from police_arraignment a " +
            "  left join police_casetoarraignment cr on cr.arraignmentssid=a.ssid " +
            "  left join police_case c on c.ssid=cr.casessid" +
            "  left join police_record r on r.ssid=a.recordssid " +
            "  where 1=1 ${ew.sqlSegment} ")
    Case getCaseByRecordSsid( @Param("ew")EntityWrapper ew);


    @Select(" select DISTINCT(c.ssid),c.* from police_case c " +
            " left join police_casetouserinfo cu on cu.casessid=c.ssid " +
            " left join police_userinfo u on u.ssid=cu.userssid "+
            " where 1=1 ${ew.sqlSegment} ")
    List<Case> getCase(@Param("ew") EntityWrapper ew);

    @Select(" select count(ssid) from police_case  where  ssid in(select cta.casessid from police_casetoarraignment cta) ${ew.sqlSegment}  ")
    Integer getCase_startnum(@Param("ew") EntityWrapper ew);


    @Select(" select count(ssid) from police_case  where  ssid not in(select cta.casessid from police_casetoarraignment cta)  ${ew.sqlSegment}")
    Integer  Getcase_endnum(@Param("ew") EntityWrapper ew);

}
