package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.DatainfoAndType;
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
public interface Base_datainfoMapper extends BaseMapper<Base_datainfo> {

    @Select(" select d.*,t.typename from base_datainfo d " +
            " left join base_type t on t.ssid = d.typessid  where 1=1 ${ew.sqlSegment} ")
    List<DatainfoAndType> getdataInfos(Page page,@Param("ew") EntityWrapper ew);

    @Select(" select count(d.ssid) from base_datainfo d " +
            " left join base_type t on t.ssid = d.typessid  where 1=1 ${ew.sqlSegment} ")
    int countgetdataInfos(@Param("ew") EntityWrapper ew);
}
