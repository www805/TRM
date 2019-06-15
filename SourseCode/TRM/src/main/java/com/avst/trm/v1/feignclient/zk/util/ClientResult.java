package com.avst.trm.v1.feignclient.zk.util;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.StatusCode;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoVO;
import org.springframework.stereotype.Component;

@Component
public class ClientResult extends BaseAction implements ZkControl {

    @Override
    public RResult getControlInfo() {
        RResult result=this.createNewResultOfFail();
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


}
