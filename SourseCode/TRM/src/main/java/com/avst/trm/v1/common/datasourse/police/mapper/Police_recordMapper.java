package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
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

    @Select(" select r.*,t.typename as recordtypename,f2.recorddownurl as pdfdownurl,f2.recordrealurl as pdfrealurl,f3.recorddownurl as worddownurl,f3.recordrealurl as wordrealurl,f1.recorddownurl as wordheaddownurl,f1.recordrealurl as wordheadrealurl from police_record r " +
            " left join base_filesave f2 on f2.ssid=r.pdf_filesavessid " +
            " left join base_filesave f3 on f3.ssid=r.word_filesavessid " +
            " left join base_filesave f1 on f1.ssid=r.wordhead_filesavessid " +
            " left join police_recordtype t on t.ssid=r.recordtypessid "+
            " where 1=1 ${ew.sqlSegment} ")
    Record getRecordBySsid(@Param("ew") EntityWrapper ew);

    @Select(" select r.*,t.typename as recordtypename,f2.recorddownurl as pdfdownurl,f2.recordrealurl as pdfrealurl ,f3.recorddownurl as worddownurl,f3.recordrealurl as wordrealurl,f1.recorddownurl as wordheaddownurl,f1.recordrealurl as wordheadrealurl from police_record r " +
            " left join base_filesave f2 on f2.ssid=r.pdf_filesavessid "+
            " left join base_filesave f3 on f3.ssid=r.word_filesavessid " +
            " left join base_filesave f1 on f1.ssid=r.wordhead_filesavessid " +
            " left join police_arraignment a on r.ssid=a.recordssid " +
            " left join police_casetoarraignment cr on cr.arraignmentssid=a.ssid " +
            " left join police_recordtype t on t.ssid=r.recordtypessid "+
            " left join police_case c on c.ssid=cr.casessid where 1=1 ${ew.sqlSegment} ")
    List<Record> getRecords(Page page,@Param("ew") EntityWrapper ew);

    @Select(" select count(r.id) from police_record r " +
            " left join base_filesave f2 on f2.ssid=r.pdf_filesavessid "+
            " left join base_filesave f3 on f3.ssid=r.word_filesavessid " +
            " left join base_filesave f1 on f1.ssid=r.wordhead_filesavessid " +
            " left join police_arraignment a on r.ssid=a.recordssid " +
            " left join police_casetoarraignment cr on cr.arraignmentssid=a.ssid " +
            " left join police_recordtype t on t.ssid=r.recordtypessid "+
            " left join police_case c on c.ssid=cr.casessid where 1=1 ${ew.sqlSegment} ")
    int countgetRecords(@Param("ew") EntityWrapper ew);


    @Select(" select a.ssid as arraignmentssid,a.adminssid,a1.username as adminname,a1.workunitssid as workunitssid1,a.otheradminssid,a2.username as otheradminname,a2.workunitssid as workunitssid2,a.recordadminssid,a3.username as recordadminname,a3.workunitssid as workunitssid3,w1.workname as workunitname1,w2.workname as workunitname2,w3.workname as workunitname3,a.userssid,u.username as username from police_arraignment a " +
            " left join police_casetoarraignment ca on a.ssid=ca.arraignmentssid " +
            " left join police_case c on c.ssid=ca.casessid " +
            " left join police_userinfo u on u.ssid=a.userssid " +
            " left join base_admininfo a1 on a1.ssid=a.adminssid " +
            " left join base_admininfo a2 on a2.ssid=a.otheradminssid " +
            " left join base_admininfo a3 on a3.ssid=a.recordadminssid " +
            " left join police_workunit w1 on w1.ssid=a1.workunitssid " +
            " left join police_workunit w2 on w2.ssid=a2.workunitssid " +
            " left join police_workunit w3 on w3.ssid=a3.workunitssid " +
            " where 1=1 ${ew.sqlSegment} ")
    RecordUserInfos getRecordUserInfosByRecordSsid(@Param("ew") EntityWrapper ew);
}
