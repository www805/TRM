package com.avst.trm.v1.web.standaloneweb.req;

import com.avst.trm.v1.feignclient.ec.req.SetFDAudioVolumeParam_out;

import java.util.List;

public class SaveFDAudioVolumeParam {

    private List<SetFDAudioVolumeParam_out> fdAudioVolumeList;

    public List<SetFDAudioVolumeParam_out> getFdAudioVolumeList() {
        return fdAudioVolumeList;
    }

    public void setFdAudioVolumeList(List<SetFDAudioVolumeParam_out> fdAudioVolumeList) {
        this.fdAudioVolumeList = fdAudioVolumeList;
    }
}
