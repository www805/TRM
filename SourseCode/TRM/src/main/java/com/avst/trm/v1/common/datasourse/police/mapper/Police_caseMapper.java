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


    @Select(" select case_monthnum_y as 'case_monthnum_y' from (" +
            "    select count(ssid) as case_monthnum_y  from police_case  where date_format(occurrencetime,'%m')='01' and date_format(occurrencetime,'%Y')=#{years} union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='02' and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='03' and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='04' and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='05' and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='06' and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='07' and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='08' and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='09'  and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='10' and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='11' and date_format(occurrencetime,'%Y')=#{years}  union all  " +
            "    select count(ssid)  from police_case  where date_format(occurrencetime,'%m')='12' and date_format(occurrencetime,'%Y')=#{years}  " +
            ") u1" )
    List<Integer> getCase_monthnum_y(@Param("years") String years);

    @Select(" select count(c.ssid) from police_case c where  date_format(c.occurrencetime,'%Y')=#{years} and c.ssid in(select cta.casessid from police_casetoarraignment cta) ")
    Integer getCase_startnum(@Param("years") String years);

    @Select(" select count(c.ssid) from police_case c where  date_format(c.occurrencetime,'%Y')=#{years} and  c.ssid not in(select cta.casessid from police_casetoarraignment cta) ")
    Integer  Getcase_endnum(@Param("years") String years);

}
