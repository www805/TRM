package com.avst.trm.v1.web.service;


import com.avst.trm.v1.common.datasourse.mysql.entity.Admininfo;
import com.avst.trm.v1.common.datasourse.mysql.mapper.AdmininfoMapper;
import com.avst.trm.v1.common.util.baseaction.RResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CeshiService {

    @Autowired
    private AdmininfoMapper admininfoMapper;

    public void getadminlist(RResult<List<Admininfo>> result, String username){

        if(null==result){
            result=new RResult<List<Admininfo>>();
        }
        List<Admininfo> list=new ArrayList<Admininfo>();
        try {

            list=(List<Admininfo>)admininfoMapper.selectList(null);
            result.setData(list);
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            System.out.println("请求结束");
        }
    }

}
