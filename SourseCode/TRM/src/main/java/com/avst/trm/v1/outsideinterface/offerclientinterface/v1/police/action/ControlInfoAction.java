package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.action;

import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service.ControlInfoService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ControlInfoAction extends BaseAction {

    @Autowired
    private ControlInfoService controlInfoService;

    @Autowired
    private ZkControl zkControl;

    /**
     * 提供本服务器相关信息
     * @return
     */
    @RequestMapping("/getControlInfo")
    @ResponseBody
    public ControlInfoParamVO getControlInfo(){
        return controlInfoService.getControlInfo();
    }

    /**
     * 获取总控所有注册了的服务器信息列表
     * @return
     */
    @RequestMapping("/zk/getControlInfoAll")
    @ResponseBody
    public RResult getControlInfoAll(){
        return zkControl.getControlInfo(); //直接获取
    }

}
