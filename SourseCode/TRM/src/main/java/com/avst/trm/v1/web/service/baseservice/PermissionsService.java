package com.avst.trm.v1.web.service.baseservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_permissions;
import com.avst.trm.v1.common.datasourse.base.entity.Base_roletopermissions;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_permissionsMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roletopermissionsMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.UpdateRoleToPermissionsParam;
import com.avst.trm.v1.web.vo.basevo.GetPermissionsVO;
import com.avst.trm.v1.web.vo.basevo.param.GetPermissionsVOParam;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("permissionsService")
public class PermissionsService extends BaseService {
    private Gson gson = new Gson();
    @Autowired
    private Base_permissionsMapper base_permissionsMapper;

    @Autowired
    private Base_roletopermissionsMapper base_roletopermissionsMapper;



    public void getPermissions(RResult result){
        GetPermissionsVO menuBarVO=new GetPermissionsVO();

        EntityWrapper ew=new EntityWrapper();
        ew.orderBy("ordernum",true);
        ew.orderBy("createtime",false);
        List<Base_permissions> list=base_permissionsMapper.selectList(ew);
        //转换
        List<GetPermissionsVOParam> permissions=gson.fromJson(gson.toJson(list), new TypeToken<List<GetPermissionsVOParam>>(){}.getType());
        for (int i = 0; i < permissions.size(); i++) {
            for (int j = 0; j < permissions.size(); j++) {
                if(permissions.get(i).getId()==permissions.get(j).getPid()){
                    GetPermissionsVOParam m= permissions.get(i);
                    m.getPermissionsList().add(permissions.get(j));
                }
            }
        }
        List<GetPermissionsVOParam> permissionsVOParams=new ArrayList<GetPermissionsVOParam>();
        for (int i = 0; i < permissions.size(); i++) {
            if(permissions.get(i).getPid()==0){
                permissionsVOParams.add(permissions.get(i));
            }
        }
        menuBarVO.setPermissionsVOParams(permissionsVOParams);
        result.setData(menuBarVO);
        changeResultToSuccess(result);

         return;
    }

    public void getPermissionsByRoleSsid(RResult result,Integer rolessid){
        EntityWrapper ew=new EntityWrapper();
        ew.eq("rolessid",rolessid);
          List<Base_roletopermissions> roletopermissions = base_roletopermissionsMapper.selectList(ew);
          if (null!=roletopermissions&&roletopermissions.size()>0){
              result.setData(roletopermissions);
          }
          changeResultToSuccess(result);
        return;
    }

    public void updateRoleToPermissions(RResult result, UpdateRoleToPermissionsParam param){
        String rolessid=param.getRolessid();
        if (StringUtils.isBlank(rolessid)){
            result.setMessage("参数为空");
            return;
        }
        EntityWrapper deleteParam=new EntityWrapper();
        deleteParam.eq("rolessid",rolessid);
        int delete_bool=base_roletopermissionsMapper.delete(deleteParam);
        System.out.println("delete_bool__"+delete_bool);

        List<Base_permissions> permissions=param.getPermissions();
        if (null!=permissions&&permissions.size()>0){
            for (Base_permissions permission : permissions) {
                Base_roletopermissions roletopermissions=new Base_roletopermissions();
                roletopermissions.setRolessid(rolessid);
                roletopermissions.setCreatetime(new Date());
                roletopermissions.setSsid(OpenUtil.getUUID_32());
                roletopermissions.setPermissionsssid(permission.getSsid());
                int insert_bool = base_roletopermissionsMapper.insert(roletopermissions);
                System.out.println("insert_bool__"+insert_bool);
            }
        }
        result.setData(1);
        changeResultToSuccess(result);
        return;
    }


}
