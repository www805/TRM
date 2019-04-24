package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.entity.Police_casetoarraignment;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.ArraignmentAndRecord;
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
public interface Police_casetoarraignmentMapper extends BaseMapper<Police_casetoarraignment> {

    @Select("select a.*,r.recordname,r.recordbool,r.recordtime ,au1.username as adminname ,au2.username as recordadminname,r.ssid as recordssid " +
            " from police_arraignment a " +
            " left join police_casetoarraignment cr on cr.arraignmentssid=a.ssid " +
            " left join base_admininfo au1 on au1.ssid=a.adminssid " +
            " left join base_admininfo au2 on au2.ssid=a.recordadminssid " +
            " left join police_record r on r.ssid=a.recordssid where 1=1 ${ew.sqlSegment}")
    List<ArraignmentAndRecord> getArraignmentByCaseSsid( @Param("ew")EntityWrapper ew);

}
