package com.avst.trm.v1.web.sweb.service.baseservice;

import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ActionAndinterfaceAndPage;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_actionMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 关于动作action的处理
 */
@Service
public class ActionService {

    @Autowired
    private Base_actionMapper base_actionMapper;

    public ActionAndinterfaceAndPage getAction(String interfaceurl,String pageid ){

        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("i.interfaceurl",interfaceurl);
        entityWrapper.eq("a.pageid",pageid);

        ActionAndinterfaceAndPage actionAndinterfaceAndPage=base_actionMapper.getActionAndinterfaceAndPage(entityWrapper).get(0);
        if(null!=actionAndinterfaceAndPage){


            return actionAndinterfaceAndPage;
        }
        return null;
    }
}
