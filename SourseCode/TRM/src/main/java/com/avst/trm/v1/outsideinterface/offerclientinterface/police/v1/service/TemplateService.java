package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.service;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.ForClientBaseService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.*;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service("templateService")
public class TemplateService extends ForClientBaseService {
    private Gson gson = new Gson();

    @Autowired
    private Police_templateMapper police_templateMapper;

    @Autowired
    private Police_problemMapper police_problemMapper;

    @Autowired
    private Police_templatetypeMapper police_templatetypeMapper;

    @Autowired
    private Police_problemtypeMapper police_problemtypeMapper;

    @Autowired
    private Police_templatetoproblemMapper police_templatetoproblemMapper;

    public void getTemplates(RResult result, ReqParam param){
        GetTemplatesVO getTemplatesVO=new GetTemplatesVO();

        //请求参数转换
        GetTemplatesParam getTemplatesParam= new GetTemplatesParam();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            getTemplatesParam =gson.fromJson(parameter, GetTemplatesParam.class);
        }
       if (null==getTemplatesParam){
            result.setMessage("参数为空");
            return;
        }

        //分页处理
        int count=police_templateMapper.countgetTemplateList(getTemplatesParam);
        getTemplatesParam.setRecordCount(count);

        Page<Template> page=new Page<Template>(getTemplatesParam.getCurrPage(),getTemplatesParam.getPageSize());
        List<Template> templates=police_templateMapper.getTemplateList(page,getTemplatesParam);
        if (null!=templates&&templates.size()>0){
            //添加题目列表
            for (Template template : templates) {
                GetTemplateToProblemsParam getTemplateToProblemsParam=new GetTemplateToProblemsParam();
                getTemplateToProblemsParam.setTemplateid(template.getId());
                List<TemplateToProblem> templateToProblems=police_templatetoproblemMapper.getTemplateToProblems(getTemplateToProblemsParam);
                if (null!=templateToProblems&&templateToProblems.size()>0){
                    template.setTemplateToProblems(templateToProblems);
                }
            }
        }
        getTemplatesVO.setTemplates(templates);
        getTemplatesVO.setTemplatesParam(getTemplatesParam);
        result.setData(getTemplatesVO);
        changeResultToSuccess(result);
        return;
    }

    /**
     * *****
     * @param result
     * @param param
     */
    public void getProblems(RResult result, ReqParam<GetProblemsParam> param){
        GetProblemsVO getProblemsVO=new GetProblemsVO();
        System.out.println(param.getParam()+"----");
        //请求参数转换

        // GetProblemsParam getProblemsParam=gson.fromJson(String.valueOf(param.getParam()),GetProblemsParam.class);
        GetProblemsParam getProblemsParam=(GetProblemsParam)param.getParam();
        if (null==getProblemsParam){
            result.setMessage("参数为空");
            return;
        }

        //分页处理
        EntityWrapper ew=new EntityWrapper();
        if (null!=getProblemsParam.getProblemtypeid()){
           ew.eq(true,"pp.problemtypeid",getProblemsParam.getProblemtypeid());
        }
        if (StringUtils.isNotBlank(getProblemsParam.getKeyword())){
            ew.like(true,"p.problem",getProblemsParam.getKeyword());
        }
        System.out.println( ew.getSqlSegment());

        int count=police_problemMapper.countgetProblemList(ew);
        getProblemsParam.setRecordCount(count);

        ew.orderBy("p.ordernum",true);
        ew.orderBy("p.createtime",false);
        Page<Problem> page=new Page<>(getProblemsParam.getCurrPage(),getProblemsParam.getPageSize());
        List<Problem> problems=police_problemMapper.getProblemList(page,ew);

        getProblemsVO.setProblems(problems);
        getProblemsVO.setProblemsParam(getProblemsParam);
        result.setData(getProblemsVO);
        changeResultToSuccess(result);
        return;
    }

    public void getTemplateTypes(RResult result){
        GetTemplateTypesVO getTemplateTypesVO=new GetTemplateTypesVO();

        EntityWrapper ew=new EntityWrapper();
        ew.orderBy("ordernum",true);
        ew.orderBy("createtime",false);
        List<Police_templatetype> list=police_templatetypeMapper.selectList(ew);
        List<Templatetype> templatetypes=new ArrayList<>();
        if (null!=list&&list.size()>0){
           templatetypes = gson.fromJson(gson.toJson(list), new TypeToken<List<Templatetype>>(){}.getType());
        }
        getTemplateTypesVO.setTemplatetypes(templatetypes);
        result.setData(getTemplateTypesVO);
        changeResultToSuccess(result);
        return;
    }

    public void  getProblemTypes(RResult result){
        GetProblemTypesVO getProblemTypesVO=new GetProblemTypesVO();

        EntityWrapper ew=new EntityWrapper();
        ew.orderBy("ordernum",true);
        ew.orderBy("createtime",false);
        List<Police_problemtype> list=police_problemtypeMapper.selectList(ew);
        List<Problemtype> problemtypes=new ArrayList<>();
        if (null!=list&&list.size()>0){
            problemtypes = gson.fromJson(gson.toJson(list), new TypeToken<List<Problemtype>>(){}.getType());
        }
        getProblemTypesVO.setProblemtypes(problemtypes);
        result.setData(getProblemTypesVO);
        changeResultToSuccess(result);
        return;
    }


    public void updateTemplate(RResult result,ReqParam param){
        UpdateTemplateVO updateTemplateVO=new UpdateTemplateVO();
        //请求参数转换
        UpdateTemplateParam template=new UpdateTemplateParam();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            template =gson.fromJson(parameter, UpdateTemplateParam.class);
        }
        if (null==template){
            result.setMessage("参数为空");
            return;
        }

        if (null==template.getId()){
            result.setMessage("参数为空");
            return;
        }

        //删除关联题目
        EntityWrapper ew=new EntityWrapper();
        ew.eq("templateid",template.getId());
        int delete_bool = police_templatetoproblemMapper.delete(ew);
        System.out.println("delete_bool__"+delete_bool);
        if (delete_bool>0){

            //添加关联题目
            List<Integer> ids=template.getTemplatetoproblemids();
            if (null!=ids&&ids.size()>0){
                for (Integer id : ids) {
                    Police_templatetoproblem templatetoproblem=new Police_templatetoproblem();
                    templatetoproblem.setCreatetime(new Date());
                    templatetoproblem.setTemplateid(template.getId());
                    templatetoproblem.setProblemid(id);
                    templatetoproblem.setOrdernum(1);
                    templatetoproblem.setSsid(OpenUtil.getUUID_32());
                    int insert_bool = police_templatetoproblemMapper.insert(templatetoproblem);
                    System.out.println("insert_bool__"+insert_bool);
                }
            }
        }

        //修改模板数据
        template.setUpdatetime(new Date());
        int updateById_bool=police_templateMapper.updateById(template);
        System.out.println("updateById_bool__"+updateById_bool);
        updateTemplateVO.setBool(updateById_bool);
        result.setData(updateTemplateVO);
        if (updateById_bool<1){
            result.setMessage("系统异常");
            return;
        }
        result.setNextpageid("updateTemplate_index");
        changeResultToSuccess(result);
        return;
    }

    public void getTemplateById(RResult result,ReqParam param){
        GetTemplateByIdVO getTemplateByIdVO=new GetTemplateByIdVO();

        GetTemplateByIdParam getTemplateByIdParam=new GetTemplateByIdParam();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            getTemplateByIdParam =gson.fromJson(parameter, GetTemplateByIdParam.class);
        }
        if (null==getTemplateByIdParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==getTemplateByIdParam.getTemplatebyid()){
            result.setMessage("参数为空");
            return;
        }

        Template template=police_templateMapper.getTemplateById(getTemplateByIdParam);

        if (null==template){
            result.setMessage("未找到该模板信息");
            return;
        }

        //添加题目列表
        GetTemplateToProblemsParam getTemplateToProblemsParam=new GetTemplateToProblemsParam();
        getTemplateToProblemsParam.setTemplateid(template.getId());
        List<TemplateToProblem> templateToProblems=police_templatetoproblemMapper.getTemplateToProblems(getTemplateToProblemsParam);
        if (null!=templateToProblems&&templateToProblems.size()>0){
            template.setTemplateToProblems(templateToProblems);
        }

        getTemplateByIdVO.setTemplate(template);
        result.setData(getTemplateByIdVO);
        changeResultToSuccess(result);
        return;
    }

    public void addTemplate(RResult result,ReqParam param){
        AddTemplateVO addTemplateVO=new AddTemplateVO();

        AddTemplateParam addTemplateParam=new AddTemplateParam();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            addTemplateParam =gson.fromJson(parameter, AddTemplateParam.class);
        }

        if (null==addTemplateParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==addTemplateParam.getTitle()){
            result.setMessage("参数为空");
            return;
        }

        //添加模板数据
        addTemplateParam.setCreatetime(new Date());
        addTemplateParam.setSsid(OpenUtil.getUUID_32());
        addTemplateParam.setOrdernum(1);
        int insert_bool = police_templateMapper.insert(addTemplateParam);
        System.out.println("insert_bool__"+insert_bool);
        if (insert_bool<0){
            result.setMessage("系统异常");
            return;
        }

        //添加模板题目关联数据
        List<Integer> ids=addTemplateParam.getTemplatetoproblemids();
        if (null!=ids&&ids.size()>0){
            for (Integer id : ids) {
                Police_templatetoproblem templatetoproblem=new Police_templatetoproblem();
                templatetoproblem.setCreatetime(new Date());
                templatetoproblem.setTemplateid(addTemplateParam.getId());
                templatetoproblem.setProblemid(id);
                templatetoproblem.setOrdernum(1);
                templatetoproblem.setSsid(OpenUtil.getUUID_32());
                int police_templatetoprobleminsert_bool = police_templatetoproblemMapper.insert(templatetoproblem);
                System.out.println("police_templatetoprobleminsert_bool"+police_templatetoprobleminsert_bool);
            }
        }

        addTemplateVO.setBool(insert_bool);
        result.setData(addTemplateVO);
        changeResultToSuccess(result);
        return;
    }
/***/
    public void updateProblem(RResult result,ReqParam param){
        Police_problem problem=new Police_problem();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            problem =gson.fromJson(parameter, Police_problem.class);
        }
    }

    public void getProblemById(RResult result,ReqParam param){
        Police_problem problem=new Police_problem();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            problem =gson.fromJson(parameter, Police_problem.class);
        }
    }

    public void addProblem(RResult result,ReqParam param){
        Police_problem problem=new Police_problem();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            problem =gson.fromJson(parameter, Police_problem.class);
        }
    }

    public void addTemplateType(RResult result,ReqParam param){
        Police_template template=new Police_template();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            template =gson.fromJson(parameter, Police_template.class);
        }
    }

    public void updateTemplateType(RResult result,ReqParam param){
        Police_template template=new Police_template();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            template =gson.fromJson(parameter, Police_template.class);
        }
    }

    public void addProblemType(RResult result,ReqParam param){
        Police_problem problem=new Police_problem();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            problem =gson.fromJson(parameter, Police_problem.class);
        }
    }

    public void updateProblemType(RResult result,ReqParam param){
        Police_problem problem=new Police_problem();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            problem =gson.fromJson(parameter, Police_problem.class);
        }
    }

    public void setDefaultTemplate(RResult result,ReqParam param){
        return;
    }




}
