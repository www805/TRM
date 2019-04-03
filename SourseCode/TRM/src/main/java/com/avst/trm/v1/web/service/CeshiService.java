package com.avst.trm.v1.web.service;


import com.avst.trm.v1.common.datasourse.mysql.entity.Admininfo;
import com.avst.trm.v1.common.datasourse.mysql.mapper.AdmininfoMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

@Service
public class CeshiService extends BaseService {

    @Autowired
    private AdmininfoMapper admininfoMapper;

    public void getadminlist(RResult<List<Admininfo>> result, String username){

        if(null==result){
            result=new RResult<List<Admininfo>>();
        }
        List<Admininfo> list=new ArrayList<Admininfo>();
        try {

            EntityWrapper ew=new EntityWrapper();
//            ew.setEntity(new Admininfo());
            ew.eq("id",1);
            list=(List<Admininfo>)admininfoMapper.selectList(ew);
            result.setData(list);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            System.out.println("请求结束");
        }
    }

}
