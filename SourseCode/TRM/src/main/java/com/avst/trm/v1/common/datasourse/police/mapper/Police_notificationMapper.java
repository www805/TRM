package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_notification;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Notification;
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
public interface Police_notificationMapper extends BaseMapper<Police_notification> {

    @Select("select count(id) from police_notification where 1=1 ${ew.sqlSegment} ")
    int countgetNotifications(@Param("ew") EntityWrapper ew);

    @Select("select * from police_notification where 1=1 ${ew.sqlSegment} ")
    List<Notification> getNotifications(Page<Notification> page, @Param("ew") EntityWrapper ew);

}
