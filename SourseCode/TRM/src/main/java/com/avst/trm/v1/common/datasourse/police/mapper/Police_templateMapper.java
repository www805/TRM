package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_template;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Template;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Templatetype;
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

    @Select("select DISTINCT(t.id) idssa,t.*,l.templatetypessid from police_template t " +
            " left join police_templatetotype l on t.id = l.templatessid " +
            " where 1=1 ${ew.sqlSegment} ")
    List<Template> getTemplateList(Page page,@Param("ew") EntityWrapper ew);

    @Select("select count(DISTINCT(t.id)) idssa from police_template t " +
            " left join police_templatetotype l on t.id = l.templatessid " +
            " where 1=1 ${ew.sqlSegment} ")
    int countgetTemplateList(@Param("ew") EntityWrapper ew);

    @Select("select * from police_template " +
            " where 1=1 ${ew.sqlSegment} ")
    Template getTemplateById(@Param("ew") EntityWrapper ew);

    @Select("select p.typename from police_template t left join police_templatetotype z on t.id = z.templatessid left join police_templatetype p on z.templatetypessid = p.id " +
            " where 1=1 ${ew.sqlSegment} ")
    String getTemplatetype(@Param("ew") EntityWrapper ew);
}
