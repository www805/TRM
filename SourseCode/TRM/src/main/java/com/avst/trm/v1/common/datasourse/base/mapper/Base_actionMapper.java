package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_action;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ActionAndinterfaceAndPage;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * InnoDB free: 90112 kB Mapper 接口
 * </p>
 *
 * @author Admin
 * @since 2019-04-11
 */
public interface Base_actionMapper extends BaseMapper<Base_action> {


    @Select("select a.*,t.typename,i.interfaceurl " +
            "from base_action a inner join base_interface i on a.interfaceid=i.id" +
            "inner join base_type t on a.typeid=t.id" )
    public ActionAndinterfaceAndPage getActionAndinterfaceAndPage(EntityWrapper ew);
}
