package com.avst.trm.v1.web.service.policeservice;


import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roleMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.GetRoleListParam;
import com.avst.trm.v1.web.vo.basevo.GetRoleListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
            int count=roleMapper.selectCount(ew);
            param.setRecordCount(count);

            ew.orderBy("ordernum",true);
            ew.orderBy("createtime",false);
            Page<Base_role> page=new Page<Base_role>(param.getCurrPage(),param.getPageSize());
            List<Base_role> list=roleMapper.selectPage(page,ew);
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




}
