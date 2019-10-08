package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ServerconfigAndFilesave;
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
public interface Base_serverconfigMapper extends BaseMapper<Base_serverconfig> {

    @Select(" select b.*,f.recorddownurl as client_downurl,f.recordrealurl as client_realurl,f1.recorddownurl as syslogo_downurl,f1.recordrealurl as syslogo_realurl,f2.recorddownurl as runbook_downurl,f2.recordrealurl as runbook_realurl from base_serverconfig b " +
            " left join base_filesave f on f.ssid=b.client_filesavessid " +
            " left join base_filesave f1 on f1.ssid=b.syslogo_filesavessid " +
            " left join base_filesave f2 on f2.ssid=b.runbook_filesavessid " +
            " where 1=1 ${ew.sqlSelect} ")
    List<ServerconfigAndFilesave> getServerconfig(@Param("ew") EntityWrapper ew);

}
