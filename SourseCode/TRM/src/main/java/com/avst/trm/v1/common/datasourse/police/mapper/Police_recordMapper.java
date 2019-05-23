package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.CaseAndUserInfo;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Record;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordUserInfos;
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
public interface Police_recordMapper extends BaseMapper<Police_record> {

    @Select(" select r.*,f1.recorddownurl as recorddownurl ,f1.recordrealurl as recordrealurl,f2.recorddownurl as pdfdownurl,f2.recordrealurl as pdfrealurl  from police_record r " +
            " left join base_filesave f1 on f1.datassid=r.record_filesavessid" +
            " left join base_filesave f2 on f2.datassid=r.pdf_filesavessid where 1=1 ${ew.sqlSegment}")
    Record getRecordBySsid(@Param("ew") EntityWrapper ew);

    @Select(" select r.*,f1.recorddownurl as recorddownurl ,f1.recordrealurl as recordrealurl,f2.recorddownurl as pdfdownurl,f2.recordrealurl as pdfrealurl  from police_record r " +
            " left join base_filesave f1 on f1.datassid=r.record_filesavessid" +
            " left join base_filesave f2 on f2.datassid=r.pdf_filesavessid where 1=1 ${ew.sqlSegment}")
    List<Record> getRecords(Page page,@Param("ew") EntityWrapper ew);

    @Select(" select count(r.id) from police_record r " +
            " left join base_filesave f1 on f1.datassid=r.record_filesavessid" +
            " left join base_filesave f2 on f2.datassid=r.pdf_filesavessid where 1=1 ${ew.sqlSegment}")
    int countgetRecords(@Param("ew") EntityWrapper ew);

    @Select(" select a.adminssid,a1.username as adminname,a.otheradminssid,a2.username as otheradminname,a.recordadminssid,a3.username as recordadminname,c.userssid,u.username as username from police_arraignment a " +
            " left join police_casetoarraignment ca on a.ssid=ca.arraignmentssid " +
            " left join police_case c on c.ssid=ca.casessid " +
            " LEFT JOIN police_userinfo u on u.ssid=c.userssid " +
            " left JOIN base_admininfo a1 on a1.ssid=a.adminssid " +
            " LEFT JOIN base_admininfo a2 on a2.ssid=a.otheradminssid " +
            " LEFT JOIN base_admininfo a3 on a3.ssid=a.recordadminssid " +
            " where 1=1 ${ew.sqlSegment} ")
    RecordUserInfos getRecordUserInfosByRecordSsid(@Param("ew") EntityWrapper ew);



}
