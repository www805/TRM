package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.service;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;
import com.avst.trm.v1.common.datasourse.police.entity.Police_problemtype;
import com.avst.trm.v1.common.datasourse.police.entity.Police_template;
import com.avst.trm.v1.common.datasourse.police.entity.Police_templatetype;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problemtype;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Template;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Templatetype;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_problemMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_problemtypeMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_templateMapper;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_templatetypeMapper;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.ForClientBaseService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetProblemsParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetTemplatesParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.GetProblemTypesVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.GetProblemsVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.GetTemplateTypesVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.GetTemplatesVO;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public void getTemplates(RResult result, ReqParam param){
        GetTemplatesVO getTemplatesVO=new GetTemplatesVO();
        GetTemplatesParam getTemplatesParam= new GetTemplatesParam();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            getTemplatesParam =gson.fromJson(parameter, GetTemplatesParam.class);
        }
       if (null==getTemplatesParam){
            result.setMessage("参数为空");
            return;
        }
        int count=police_templateMapper.countgetTemplateList(getTemplatesParam);
        getTemplatesParam.setRecordCount(count);

        Page<Police_template> page=new Page<Police_template>(getTemplatesParam.getCurrPage(),getTemplatesParam.getPageSize());
        List<Police_template> list=police_templateMapper.getTemplateList(page,getTemplatesParam);
        List<Template> templates=new ArrayList<>();
        if (null!=list&&list.size()>0){
            templates = gson.fromJson(gson.toJson(list), new TypeToken<List<Template>>(){}.getType());
        }
        getTemplatesVO.setTemplates(templates);
        getTemplatesVO.setTemplatesParam(getTemplatesParam);
        result.setData(getTemplatesVO);
        changeResultToSuccess(result);
        return;
    }

    public void getProblems(RResult result, ReqParam param){
        GetProblemsVO getProblemsVO=new GetProblemsVO();
        GetProblemsParam getProblemsParam=new GetProblemsParam();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            getProblemsParam =gson.fromJson(parameter, GetProblemsParam.class);
        }
        if (null==getProblemsParam){
            result.setMessage("参数为空");
            return;
        }

        int count=police_problemMapper.countgetProblemList(getProblemsParam);
        getProblemsParam.setRecordCount(count);

        Page<Police_problem> page=new Page<>(getProblemsParam.getCurrPage(),getProblemsParam.getPageSize());
        List<Police_problem> list=police_problemMapper.getProblemList(page,getProblemsParam);
        List<Problem> problems=new ArrayList<>();
        if (null!=list&&list.size()>0){
            problems = gson.fromJson(gson.toJson(list), new TypeToken<List<Problem>>(){}.getType());
        }
        getProblemsVO.setProblems(problems);
        getProblemsVO.setProblemsParam(getProblemsParam);
        result.setData(getProblemsVO);
        changeResultToSuccess(result);
        return;
    }

    public void getTemplateTypes(RResult result){
        GetTemplateTypesVO getTemplateTypesVO=new GetTemplateTypesVO();
        List<Police_templatetype> list=police_templatetypeMapper.selectList(null);
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
        List<Police_problemtype> list=police_problemtypeMapper.selectList(null);
        List<Problemtype> problemtypes=new ArrayList<>();
        if (null!=list&&list.size()>0){
            problemtypes = gson.fromJson(gson.toJson(list), new TypeToken<List<Problemtype>>(){}.getType());
        }
        getProblemTypesVO.setProblemtypes(problemtypes);
        result.setData(getProblemTypesVO);
        changeResultToSuccess(result);
        return;
    }

    public void setTemplate(RResult result,ReqParam param){
        Police_template template=new Police_template();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            template =gson.fromJson(parameter, Police_template.class);
        }
    }

    public void addTemplate(RResult result,ReqParam param){
        Police_template template=new Police_template();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            template =gson.fromJson(parameter, Police_template.class);
        }
    }

    public void setProblem(RResult result,ReqParam param){
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




}
