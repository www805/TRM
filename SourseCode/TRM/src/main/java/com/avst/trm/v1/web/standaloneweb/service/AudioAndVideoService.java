package com.avst.trm.v1.web.standaloneweb.service;

import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetFDAudioConfParam_out;
import com.avst.trm.v1.feignclient.ec.req.SetFDAudioVolumeParam_out;
import com.avst.trm.v1.web.standaloneweb.req.SaveFDAudioVolumeParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AudioAndVideoService extends BaseService {


    @Autowired
    private EquipmentControl equipmentControl;



    public void getFDAudioConf(RResult result, GetFDAudioConfParam_out param) {
        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())){
            result.setMessage("设备ssid不能为空");
            return;
        }
        if (StringUtils.isBlank(param.getFdType())){
            result.setMessage("设备类型不能为空");
            return;
        }

        RResult fdAudioConf = equipmentControl.getFDAudioConf(param);

        result.setActioncode(fdAudioConf.getActioncode());
        result.setNextpageid(fdAudioConf.getNextpageid());
        result.setData(fdAudioConf.getData());
        result.setMessage(fdAudioConf.getMessage());
        result.setEndtime(fdAudioConf.getEndtime());
        result.setVersion(fdAudioConf.getVersion());

    }

    public void setFDAudioVolume(RResult result, SetFDAudioVolumeParam_out param) {

        if (StringUtils.isBlank(param.getFlushbonadingetinfossid())){
            result.setMessage("设备ssid不能为空");
            return;
        }
        if (StringUtils.isBlank(param.getFdType())){
            result.setMessage("设备类型不能为空");
            return;
        }

        RResult fdAudioConf = equipmentControl.setFDAudioVolume(param);

        result.setActioncode(fdAudioConf.getActioncode());
        result.setNextpageid(fdAudioConf.getNextpageid());
        result.setData(fdAudioConf.getData());
        result.setMessage(fdAudioConf.getMessage());
        result.setEndtime(fdAudioConf.getEndtime());
        result.setVersion(fdAudioConf.getVersion());
    }


    public void saveFDAudioVolume(RResult result, SaveFDAudioVolumeParam param) {

        List<SetFDAudioVolumeParam_out> fdAudioVolumeList = param.getFdAudioVolumeList();

        String states = "SUCCESS";

        if (null == fdAudioVolumeList || fdAudioVolumeList.size() == 0) {
            result.setMessage("音频通道参数不能为空");
            return;
        }

        for (SetFDAudioVolumeParam_out paramOut : fdAudioVolumeList) {
            RResult fdAudioResult = equipmentControl.setFDAudioVolume(paramOut);
            if(!"SUCCESS".equalsIgnoreCase(fdAudioResult.getActioncode())){
                states = "error";
                break;
            }
        }

        if(!"error".equalsIgnoreCase(states)){
            this.changeResultToSuccess(result);
        }

    }

}
