package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datasynchroni_downserver;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.GetSynchronizedDataSheet_DownServer;
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
public interface Base_datasynchroni_downserverMapper extends BaseMapper<Base_datasynchroni_downserver> {


    @Select("select dt.* ,dd.lastuploadtime lastuploadtime,dd.uploadcount uploadcount,dd.upserverip upserverip" +
            " from base_datasheet_downserver dt " +
            " left join base_datasynchroni_downserver dd on dt.downserverssid = dd.ssid" +
            " where 1=1 ${ew.sqlSelect}")
    public List<GetSynchronizedDataSheet_DownServer> getSynchronizedDataSheet_DownServer(EntityWrapper ew);

    @Select("select upserverip from base_datasynchroni_downserver order by lastuploadtime desc,id desc LIMIT 1")
    String getLastIpByTime();

}
