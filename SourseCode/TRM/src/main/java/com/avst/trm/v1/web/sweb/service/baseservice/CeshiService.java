package com.avst.trm.v1.web.sweb.service.baseservice;


import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ActionAndinterfaceAndPage;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdmintorole;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.sweb.req.basereq.GotolistParam;
import com.avst.trm.v1.web.sweb.vo.basevo.Getlist3VO;
import com.avst.trm.v1.web.sweb.vo.basevo.param.Getlist3VOParam;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
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
     * @param username  13 4
     */
    public void getadminlist(RResult<Integer> result, String username){

        if(null==result){
            result=new RResult<Integer>();
        }
        try {
            EntityWrapper ew=new EntityWrapper();
//            ew.setEntity(new Admininfo());
              ew.eq("a.id",1);
            LogUtil.intoLog(this.getClass(),ew.getSqlSegment()+"---------");
            int count=admininfoMapper.getAdminAndAdmintorolecount(ew);
            result.setData(count);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LogUtil.intoLog(this.getClass(),"请求结束");
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

            LogUtil.intoLog(this.getClass(),page.getSize()+"-----"+page.getCurrent()+"-----"+
                    page.getTotal()+"-----"+page.getPages());

            result.setData(list);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            LogUtil.intoLog(this.getClass(),"请求结束");
        }
    }

    /**
     * 多表查询
     * @param result
     */
    public void getadminlist3(RResult<Getlist3VO> result, Getlist3Param param){

        if(null==result){
            result=new RResult<Getlist3VO>();
        }
        List<AdminAndAdmintorole> list=new ArrayList<AdminAndAdmintorole>();
        try {
            Getlist3VO getlist3VO=new Getlist3VO();
            EntityWrapper ew=new EntityWrapper();
            if(null!=param){
                if(StringUtils.isNotEmpty(param.getName())){

                    ew.like("username",param.getName());
                }
            }
            int count = admininfoMapper.getAdminAndAdmintorolecount(ew);
            param.setRecordCount(count);
            getlist3VO.setPageparam(param);
//current 第多少页，size 每页多少条
            Page<AdminAndAdmintorole> page=new Page<AdminAndAdmintorole>(param.getCurrPage(),param.getPageSize());
//            page.setRecords(list);
            list=admininfoMapper.getAdminAndAdmintorolelist(page,ew );

            LogUtil.intoLog(this.getClass(),page.getSize()+"-----"+page.getCurrent()+"-----"+
                    page.getTotal()+"-----"+page.getPages());
            if(null!=list&&list.size() > 0){
                List<Getlist3VOParam> getlist3VOParamList=new ArrayList<Getlist3VOParam>();
                for(AdminAndAdmintorole admin:list){
                    Getlist3VOParam vo=new Getlist3VOParam();
                    vo.setName(admin.getUsername());
                    vo.setId(admin.getId());
                    vo.setAge(admin.getRoleid());
                    getlist3VOParamList.add(vo);
                }
                getlist3VO.setPagelist(getlist3VOParamList);
            }

            result.setData(getlist3VO);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LogUtil.intoLog(this.getClass(),"请求结束");
        }
    }



    public void gotolist_base(RResult rResult, GotolistParam reqParam, ActionAndinterfaceAndPage actionAndinterfaceAndPage){

        //获取



        //pageid

//        rResult.setNextpageid(actionAndinterfaceAndPage.getNextpageid());
        rResult.setNextpageid("list");
    }




}
