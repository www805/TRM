package com.avst.trm.v1.web.service.baseservice;


import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roleMapper;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.ChangeboolRoleParam;
import com.avst.trm.v1.web.req.basereq.GetRoleListParam;
import com.avst.trm.v1.web.vo.basevo.GetRoleListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("roleService")
public class RoleService extends BaseService {

    @Autowired
    private Base_roleMapper roleMapper;


    public void getRoleList(RResult<GetRoleListVO> result, GetRoleListParam param){
        GetRoleListVO getRoleListVO=new GetRoleListVO();
        if(null==result){
            result=new RResult<GetRoleListVO>();
        }
        try {
            EntityWrapper ew=new EntityWrapper();
            if (StringUtils.isNotBlank(param.getRolename())){
                ew.like("rolename",param.getRolename());
            }
            if (null!=param.getRolebool()) {
                ew.eq("rolebool", param.getRolebool());
            }
            ew.ne("rolebool",-1);
            int count=roleMapper.selectCount(ew);
            param.setRecordCount(count);

            ew.orderBy("ordernum",true);
            ew.orderBy("createtime",false);
            Page<Base_role> page=new Page<Base_role>(param.getCurrPage(),param.getPageSize());
            List<Base_role> list=roleMapper.selectPage(page,ew);
            getRoleListVO.setPageparam(param);

            if(null!=list&&list.size() > 0){
                getRoleListVO.setPagelist(list);
            }
            result.setData(getRoleListVO);
            changeResultToSuccess(result);
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            System.out.println("请求结束");
        }
        return;
    }

    public void getRoles(RResult result){
        try {

            EntityWrapper ew=new EntityWrapper();
            ew.ne("rolebool",-1);
            List<Base_role> list= roleMapper.selectList(ew);
            if (null!=list&&list.size()>0){
                result.setData(list);
            }
            changeResultToSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;

    }

    public void changeboolRole(RResult result, ChangeboolRoleParam param){
        String ssid=param.getSsid();
        Integer rolebool=param.getRolebool();
        if (StringUtils.isBlank(ssid)||null==rolebool){
            result.setMessage("参数为空");
            return;
        }
        try {
            EntityWrapper ew=new EntityWrapper();
            ew.eq("ssid",ssid);
            Base_role role=new Base_role();
            role.setRolebool(rolebool);
            int delete_bool= roleMapper.update(role,ew);
            System.out.println("delete_bool__"+delete_bool);
            if (delete_bool<1){
                result.setMessage("系统异常");
                return;
            }
            result.setData(delete_bool);
            changeResultToSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void getRoleBySsid(RResult result,String ssid){
        EntityWrapper ew=new EntityWrapper();
        ew.eq(true,"ssid",ssid);
       List<Base_role> list = roleMapper.selectList(ew);
       if (null==list||list.size()<1){
           result.setMessage("未找到该角色");
           return;
       }

       if (null!=list&&list.size()>0){
           if (list.size()==1){
               Base_role role=list.get(0);
               result.setData(role);
               changeResultToSuccess(result);
           }else{
               System.out.println("系统异常：多个角色");
               result.setMessage("系统异常");
               return;
           }
       }
        return;
    }

    public void updateRole(RResult result,Base_role param){
        try {
            EntityWrapper ew=new EntityWrapper();
            ew.eq(true,"ssid",param.getSsid());
            int update_bool=roleMapper.update(param,ew);
            System.out.println("update_bool--"+update_bool);
            if (update_bool<1){
                result.setMessage("系统异常");
                return;
            }
            result.setData(update_bool);
            changeResultToSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void addRole(RResult result,Base_role param){
        try {
            param.setCreatetime(new Date());
            param.setSsid(OpenUtil.getUUID_32());
            int insert_bool=roleMapper.insert(param);
            System.out.println("insert_bool--"+insert_bool);
            if (insert_bool<1){
                result.setMessage("系统异常");
                return;
            }
            result.setData(insert_bool);
            changeResultToSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }


}
