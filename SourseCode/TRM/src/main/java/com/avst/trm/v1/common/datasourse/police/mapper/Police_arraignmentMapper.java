package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * InnoDB free: 90112 kB Mapper 接口
 * </p>
 *
 * @author Admin
 * @since 2019-04-22
 */
public interface Police_arraignmentMapper extends BaseMapper<Police_arraignment> {

    /**
     * 用于案件统计
     * @param ew
     * @return
     */
    @Select(" select count(r.ssid) from police_record r  " +
            " left join police_arraignment a on r.ssid=a.recordssid  " +
            " left join police_casetoarraignment cr on cr.arraignmentssid=a.ssid " +
            " left join police_case c on c.ssid=cr.casessid  where 1=1 ${ew.sqlSegment} ")
    Integer getArraignmentCount(@Param("ew") EntityWrapper ew);

}
