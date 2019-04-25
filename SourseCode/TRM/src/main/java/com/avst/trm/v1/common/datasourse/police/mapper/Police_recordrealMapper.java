package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordreal;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Recordreal;
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
public interface Police_recordrealMapper extends BaseMapper<Police_recordreal> {

    @Select(" select rr.*,f1.recorddownurl,f1.recordrealurl from police_recordreal rr " +
            " left join base_filesave f1 on f1.datassid=rr.recordreal_filesavessid where 1=1 ${ew.sqlSegment}")
    List<Recordreal> getRecordrealByRecordSsid(@Param("ew") EntityWrapper ew);

}
