package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
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
 * @since 2019-04-23
 */
public interface Base_filesaveMapper extends BaseMapper<Base_filesave> {

    @Select("select f.* from base_filesave f where 1=1 ${ew.sqlSegment}")
    List<Base_filesave> getfilesavePage(Page page, @Param("ew") EntityWrapper ew);

    @Select("select count(f.ssid) from base_filesave f where 1=1 ${ew.sqlSegment}")
    int countgetfilesavePage(@Param("ew")EntityWrapper ew);

}
