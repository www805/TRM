package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_template;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Template;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetTemplateByIdParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetTemplatesParam;
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
 * @since 2019-04-09
 */
public interface Police_templateMapper extends BaseMapper<Police_template> {

    @Select("select * from police_templatetype t " +
            " left join police_templatetotype l on t.id = l.templatetypessid " +
            " left join police_template te on l.templatessid = te.id " +
            " where 1=1 ${ew.sqlSegment} ")
    List<Template> getTemplateList(Page page,@Param("ew") EntityWrapper ew);

    @Select("select count(t.id) from police_templatetype t " +
            " left join police_templatetotype l on t.id = l.templatetypessid " +
            " left join police_template te on l.templatessid = te.id " +
            " where 1=1 ${ew.sqlSegment} ")
    int countgetTemplateList(@Param("ew") EntityWrapper ew);

    @Select("select * from police_template " +
            " where 1=1 ${ew.sqlSegment} ")
    Template getTemplateById(@Param("ew") EntityWrapper ew);

}
