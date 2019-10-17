package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_userinfograde;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Userinfograde;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Admin
 * @since 2019-10-11
 */
public interface Police_userinfogradeMapper extends BaseMapper<Police_userinfograde> {

    @Select(" select * from police_userinfograde  where 1=1 ${ew.sqlSegment} ")
    List<Userinfograde> getUserinfogradePage(Page page, @Param("ew") EntityWrapper ew);

    @Select(" select count(ssid) from police_userinfograde where 1=1 ${ew.sqlSegment} ")
    int countgetUserinfogradePage(@Param("ew") EntityWrapper ew);



}
