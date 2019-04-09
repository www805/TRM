package com.avst.trm.v1.web.service;


import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdmintorole;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.param.GetAdminAndAdmintorolelistParam;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CeshiService extends BaseService {

    @Autowired
    private Base_admininfoMapper admininfoMapper;

    /**
     * 条件查询
     * @param result
     * @param username
     */
    public void getadminlist(RResult<List<Base_admininfo>> result, String username){

        if(null==result){
            result=new RResult<List<Base_admininfo>>();
        }
        List<Base_admininfo> list=new ArrayList<Base_admininfo>();
        try {


            EntityWrapper ew=new EntityWrapper();
//            ew.setEntity(new Admininfo());
            ew.eq("id",1);
            list=(List<Base_admininfo>)admininfoMapper.selectList(ew);
            result.setData(list);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            System.out.println("请求结束");
        }
    }

    /**
     * 分页查询
     * @param result
     * @param size
     */
    public void getadminlist2(RResult<List<Base_admininfo>> result, int size){

        if(null==result){
            result=new RResult<List<Base_admininfo>>();
        }
        List<Base_admininfo> list=new ArrayList<Base_admininfo>();
        try {
//分页的条件，基本上都有
            EntityWrapper ew=new EntityWrapper();
//            ew.setEntity(new Admininfo());
            ew.between("id",2,5);

            int count=admininfoMapper.selectCount(ew);
//current 第多少页，size 每页多少条
            Page<Base_admininfo> page=new Page<Base_admininfo>(1,3);
            page.setTotal(count);
//            page.setRecords(list);
                    list=admininfoMapper.selectPage(page,ew );

            System.out.println(page.getSize()+"-----"+page.getCurrent()+"-----"+
                    page.getTotal()+"-----"+page.getPages());

            result.setData(list);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            System.out.println("请求结束");
        }
    }

    /**
     * 多表查询
     * @param result
     */
    public void getadminlist3(RResult<List<AdminAndAdmintorole>> result){

        if(null==result){
            result=new RResult<List<AdminAndAdmintorole>>();
        }
        List<AdminAndAdmintorole> list=new ArrayList<AdminAndAdmintorole>();
        try {

            GetAdminAndAdmintorolelistParam param = new GetAdminAndAdmintorolelistParam();
            param.setAdminid(2);
            param.setPageSize(3);
            param.setCurrPage(1);
            int count = admininfoMapper.getAdminAndAdmintorolecount(param);
            param.setRecordCount(count);
//current 第多少页，size 每页多少条
            Page<AdminAndAdmintorole> page=new Page<AdminAndAdmintorole>(param.getCurrPage(),param.getPageSize());
//            page.setRecords(list);
            list=admininfoMapper.getAdminAndAdmintorolelist(page,param );

            System.out.println(page.getSize()+"-----"+page.getCurrent()+"-----"+
                    page.getTotal()+"-----"+page.getPages());


            result.setData(list);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("请求结束");
        }
    }

}
