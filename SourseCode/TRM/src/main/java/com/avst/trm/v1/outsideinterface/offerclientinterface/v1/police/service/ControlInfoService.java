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
    public ControlInfoParamVO getControlInfo(){
        //返回本服务的信息
        ControlInfoParamVO infoVO = new ControlInfoParamVO();
        infoVO.setName(SystemName);

        //获取本机ip
        InetAddress ia=null;
        try {
            ia=ia.getLocalHost();
            infoVO.setIp(ia.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        infoVO.setType("业务系统");
        infoVO.setStatus(StatusCode.OK);

        return infoVO;
    }
}
