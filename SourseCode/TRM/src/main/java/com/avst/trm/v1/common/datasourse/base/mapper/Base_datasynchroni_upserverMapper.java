package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datasynchroni_upserver;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.GetSynchronizedDataSheet_UpServer;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
public interface Base_datasynchroni_upserverMapper extends BaseMapper<Base_datasynchroni_upserver> {

    @Select("select dt.* ,dd.lastuploadtime lastuploadtime,dd.uploadcount uploadcount,dd.unitsort unitsort" +
            " from base_datasheet_upserver dt " +
            " left join base_datasynchroni_upserver dd on dt.upserverssid = dd.id" +
            " where 1=1 ${ew.sqlSelect}")
    public List<GetSynchronizedDataSheet_UpServer> getSynchronizedDataSheet_UpServer(EntityWrapper ew);

}
