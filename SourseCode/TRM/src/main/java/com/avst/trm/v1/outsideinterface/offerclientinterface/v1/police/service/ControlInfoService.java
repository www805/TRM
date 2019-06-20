package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

import com.avst.trm.v1.common.util.StatusCode;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoParamVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@Service
public class ControlInfoService {

    /**
     * 获取本服务器信息
     * @return
     */
    public ControlInfoParamVO getControlInfo(){
        //返回本服务的信息
        ControlInfoParamVO infoVO = new ControlInfoParamVO();

        infoVO.setCount(1);//数量
        infoVO.setStatus(StatusCode.OK);//成功状态

        return infoVO;
    }
}
