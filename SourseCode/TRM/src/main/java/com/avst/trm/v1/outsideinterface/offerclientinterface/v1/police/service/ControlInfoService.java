package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.service;

import com.avst.trm.v1.common.util.StatusCode;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.feignclient.zk.ZkControl;
import com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo.ControlInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class ControlInfoService {

    //获取本服务器名字
    @Value("${spring.application.name}")
    private String SystemName;

    //获取本服务器IP
    @Value("${upload.basepath}")
    private String uploadbasepath;

    /**
     * 获取本服务器信息
     * @return
     */
    public ControlInfoVO getControlInfo(){
        //返回本服务的信息
        ControlInfoVO infoVO = new ControlInfoVO();
        infoVO.setName(SystemName);

        //获取本机ip
        InetAddress ia=null;
        try {
            ia=ia.getLocalHost();
            infoVO.setIp(ia.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        infoVO.setStatus(StatusCode.OK);

        return infoVO;
    }
}
