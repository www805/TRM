package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datasheet_downserver;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.DownserverAndDatainfo;
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
public interface Base_datasheet_downserverMapper extends BaseMapper<Base_datasheet_downserver> {

        @Select(" select dd.*, " +
                " dsd.lastuploadtime,dsd.uploadcount,dsd.upserverip,dsd.id as dsdssid, " +
                " d.dataname_cn,d.mappername,d.id as dssid," +
                " t.typename from base_datasheet_downserver dd " +
                " left join base_datasynchroni_downserver dsd on dsd.id=dd.downserverssid " +
                " left join base_datainfo d on dd.dataname=d.dataname " +
                " left join base_type t on t.id=d.typessid where 1=1 ${ew.sqlSegment} ")
        List<DownserverAndDatainfo> getdownServers(Page page, @Param("ew") EntityWrapper ew);

        @Select(" select COUNT(dd.id) from base_datasheet_downserver dd " +
                " left join base_datasynchroni_downserver dsd on dsd.id=dd.downserverssid " +
                " left join base_datainfo d on dd.dataname=d.dataname " +
                " left join base_type t on t.id=d.typessid where 1=1 ${ew.sqlSegment} ")
        int countgetdownServers(@Param("ew") EntityWrapper ew);

}
