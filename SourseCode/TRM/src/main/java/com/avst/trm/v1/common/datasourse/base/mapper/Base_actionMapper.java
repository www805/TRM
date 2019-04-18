package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_action;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ActionAndinterfaceAndPage;
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
 * @since 2019-04-11
 */
public interface Base_actionMapper extends BaseMapper<Base_action> {


    @Select("select a.*,t.typename,i.interfaceurl,p1.id page_id_c,p2.id page_id_n ,p1.firstpage firstpage" +
            " from base_action a left join base_interface i on a.interfaceid=i.id" +
            " left join base_type t on a.typeid=t.id" +
            " left join base_page p1 on p1.pageid= a.pageid" +
            " left join base_page p2 on p2.pageid= a.nextpageid" +
            " where 1=1 ${ew.sqlSelect}" )
    public List<ActionAndinterfaceAndPage> getActionAndinterfaceAndPage(@Param("ew")EntityWrapper ew);

}
