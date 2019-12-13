package com.avst.trm.v1.web.cweb.service.baseservice;

import com.avst.trm.v1.common.conf.type.SSType;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.feignclient.ec.EquipmentControl;
import com.avst.trm.v1.feignclient.ec.req.GetToOutStorageListParam;
import com.avst.trm.v1.web.cweb.req.basereq.GetFileSpaceListParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileSpaceService {

    @Autowired
    private EquipmentControl equipmentControl;

    public RResult getFileSpaceList(RResult result, GetFileSpaceListParam param) {

        //远程获取
        ReqParam<GetToOutStorageListParam> reqParam = new ReqParam<>();
        GetToOutStorageListParam toOutStorageListParam = new GetToOutStorageListParam();
        toOutStorageListParam.setSsType(SSType.AVST);
        reqParam.setParam(toOutStorageListParam);

        try {
            RResult result1 = equipmentControl.getToOutFileSpaceList(reqParam);
            return result1;
        } catch (Exception e) {
            result.setMessage("磁盘信息请求失败！请查看EC是否正常开启");
            LogUtil.intoLog(4, this.getClass(), "远程请求存储服务列表失败...");
        }

        return result;
    }

    public RResult getFileSpaceByid(RResult result, GetFileSpaceListParam param) {

        //远程获取
        ReqParam<GetToOutStorageListParam> reqParam = new ReqParam<>();
        GetToOutStorageListParam toOutStorageListParam = new GetToOutStorageListParam();
        toOutStorageListParam.setSsType(SSType.AVST);
        reqParam.setParam(toOutStorageListParam);

        try {
            RResult result1 = equipmentControl.getToOutFileSpaceList(reqParam);
            return result1;
        } catch (Exception e) {
            LogUtil.intoLog(4, this.getClass(), "远程请求存储服务列表失败...");
        }

        return result;
    }

    public RResult getFileSpaceByssid(RResult result, GetFileSpaceListParam param) {

        //远程获取
        ReqParam<GetToOutStorageListParam> reqParam = new ReqParam<>();
        GetToOutStorageListParam toOutStorageListParam = new GetToOutStorageListParam();
        toOutStorageListParam.setSsType(SSType.AVST);
        toOutStorageListParam.setSsid(param.getSsid());
        toOutStorageListParam.setPath(param.getPath());
        reqParam.setParam(toOutStorageListParam);

        try {
            RResult result1 = equipmentControl.getToOutFileSpaceByssid(reqParam);
            return result1;
        } catch (Exception e) {
            LogUtil.intoLog(4, this.getClass(), "远程请求存储服务列表失败...");
        }

        return result;
    }

    public RResult getFileSpaceAll(RResult result, GetFileSpaceListParam param) {

        //远程获取
        ReqParam<GetToOutStorageListParam> reqParam = new ReqParam<>();
        GetToOutStorageListParam toOutStorageListParam = new GetToOutStorageListParam();
        toOutStorageListParam.setSsType(SSType.AVST);
        toOutStorageListParam.setSsid(param.getSsid());
        toOutStorageListParam.setPath(param.getPath());
        reqParam.setParam(toOutStorageListParam);

        try {
            RResult result1 = equipmentControl.getToOutFileSpaceAll(reqParam);
            return result1;
        } catch (Exception e) {
            LogUtil.intoLog(4, this.getClass(), "远程请求存储服务列表失败...");
        }

        return result;
    }

    public RResult delFileSpaceAll(RResult result, GetFileSpaceListParam param) {

        //远程获取
        ReqParam<GetToOutStorageListParam> reqParam = new ReqParam<>();
        GetToOutStorageListParam toOutStorageListParam = new GetToOutStorageListParam();
        toOutStorageListParam.setSsType(SSType.AVST);
        toOutStorageListParam.setSsid(param.getSsid());
        toOutStorageListParam.setPath(param.getPath());
        reqParam.setParam(toOutStorageListParam);

        try {
            RResult result1 = equipmentControl.delToOutFileSpaceAll(reqParam);
            return result1;
        } catch (Exception e) {
            LogUtil.intoLog(4, this.getClass(), "远程请求存储服务列表失败...");
        }

        return result;
    }

    public RResult delFileSpaceByPath(RResult result, GetFileSpaceListParam param) {

        //远程获取
        ReqParam<GetToOutStorageListParam> reqParam = new ReqParam<>();
        GetToOutStorageListParam toOutStorageListParam = new GetToOutStorageListParam();
        toOutStorageListParam.setSsType(SSType.AVST);
        toOutStorageListParam.setSsid(param.getSsid());
        toOutStorageListParam.setPath(param.getPath());
        reqParam.setParam(toOutStorageListParam);

        try {
            RResult result1 = equipmentControl.delToOutFileSpaceByPath(reqParam);
            return result1;
        } catch (Exception e) {
            LogUtil.intoLog(4, this.getClass(), "远程请求存储服务列表失败...");
        }

        return result;
    }
}
