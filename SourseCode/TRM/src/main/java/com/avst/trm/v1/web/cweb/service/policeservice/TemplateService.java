package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("templateService")
public class TemplateService extends BaseService {
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

    @Autowired
    private Police_templatetotypeMapper police_templatetotypeMapper;

    public void getTemplates(RResult result, ReqParam<GetTemplatesParam>  param){
        GetTemplatesVO getTemplatesVO=new GetTemplatesVO();

        //请求参数转换
        GetTemplatesParam getTemplatesParam = param.getParam();
       if (null==getTemplatesParam){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew = new EntityWrapper();

        if (StringUtils.isNotBlank(getTemplatesParam.getKeyword())){
            ew.like("te.title", getTemplatesParam.getKeyword().trim());
        }
        if (null != getTemplatesParam.getTemplatetypeid()) {
            ew.eq("t.id", getTemplatesParam.getTemplatetypeid());
        }

        //分页处理
        int count = police_templateMapper.countgetTemplateList(ew);
        getTemplatesParam.setRecordCount(count);

        Page<Template> page=new Page<Template>(getTemplatesParam.getCurrPage(),getTemplatesParam.getPageSize());
        List<Template> templates = police_templateMapper.getTemplateList(page, ew);
        if (null!=templates&&templates.size()>0){
            //添加题目列表
            for (Template template : templates) {
                GetTemplateToProblemsParam getTemplateToProblemsParam=new GetTemplateToProblemsParam();
                getTemplateToProblemsParam.setTemplateid(template.getId());
                EntityWrapper ew2 = new EntityWrapper();

                if (null != getTemplateToProblemsParam.getTemplateid()) {
                    ew2.eq("t.id", getTemplateToProblemsParam.getTemplateid());
                }

                ew2.orderBy("b.ordernum", true); //fasle大到小   true小到大

                List<TemplateToProblem> templateToProblems=police_templatetoproblemMapper.getTemplateToProblems(ew2);
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

    public void updateTemplate(RResult result,ReqParam<UpdateTemplateParam> param){
        UpdateTemplateVO updateTemplateVO=new UpdateTemplateVO();

        //请求参数转换
        UpdateTemplateParam template=param.getParam();
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
        ew.eq("templatessid",template.getId());
        int delete_bool = police_templatetoproblemMapper.delete(ew);
        System.out.println("delete_bool__"+delete_bool);
        if (delete_bool>0){

            //添加关联题目
            List<Integer> ids=template.getTemplatetoproblemids();
            if (null!=ids&&ids.size()>0){
                for (Integer id : ids) {
                    Police_templatetoproblem templatetoproblem=new Police_templatetoproblem();
                    templatetoproblem.setCreatetime(new Date());

                    templatetoproblem.setTemplatessid(template.getId() + "");//模板id
                    templatetoproblem.setProblemssid(id + "");//题目id

                    templatetoproblem.setOrdernum(1);
                    templatetoproblem.setSsid(OpenUtil.getUUID_32());
                    int insert_bool = police_templatetoproblemMapper.insert(templatetoproblem);
                    System.out.println("insert_bool__"+insert_bool);
                }
            }
        }

//        Police_templatetotype templatetotype = new Police_templatetotype();
//        templatetotype.setId(template.getId());
//        templatetotype.setTemplatessid(addTemplateParam.getId() + "");
//        templatetotype.setTemplatebool(-1);
//        templatetotype.setTemplatetypessid(addTemplateParam.getTemplatetypeid() + "");
//        templatetotype.setCreatetime(new Date());

//        int insert_type = police_templatetotypeMapper.insert(templatetotype);
//        System.out.println("insert_type "+insert_type);

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

    public void getTemplateById(RResult result,ReqParam<GetTemplateByIdParam> param){
        GetTemplateByIdVO getTemplateByIdVO=new GetTemplateByIdVO();

        GetTemplateByIdParam getTemplateByIdParam=param.getParam();
        if (null==getTemplateByIdParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==getTemplateByIdParam.getTemplatebyid()){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew = new EntityWrapper();
        ew.eq("id", getTemplateByIdParam.getTemplatebyid());

        Template template = police_templateMapper.getTemplateById(ew);

        if (null==template){
            result.setMessage("未找到该模板信息");
            return;
        }

        //添加题目列表
        GetTemplateToProblemsParam getTemplateToProblemsParam=new GetTemplateToProblemsParam();
        getTemplateToProblemsParam.setTemplateid(template.getId());

        EntityWrapper ew2 = new EntityWrapper();
        if (null != getTemplateToProblemsParam.getTemplateid()) {
            ew2.eq("t.id", getTemplateToProblemsParam.getTemplateid());
        }

        List<TemplateToProblem> templateToProblems=police_templatetoproblemMapper.getTemplateToProblems(ew2);
        if (null!=templateToProblems&&templateToProblems.size()>0){
            template.setTemplateToProblems(templateToProblems);
        }

        getTemplateByIdVO.setTemplate(template);
        result.setData(getTemplateByIdVO);
        changeResultToSuccess(result);
        return;
    }

    public void addTemplate(RResult result,ReqParam<AddTemplateParam> param){
        AddTemplateVO addTemplateVO=new AddTemplateVO();

        AddTemplateParam addTemplateParam=param.getParam();
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
                templatetoproblem.setTemplatessid(addTemplateParam.getId() + "");//模板id
                templatetoproblem.setProblemssid(id + "");//题目id
                templatetoproblem.setOrdernum(1);
                templatetoproblem.setSsid(OpenUtil.getUUID_32());
                int police_templatetoprobleminsert_bool = police_templatetoproblemMapper.insert(templatetoproblem);
                System.out.println("police_templatetoprobleminsert_bool"+police_templatetoprobleminsert_bool);
            }
        }

        Police_templatetotype templatetotype = new Police_templatetotype();
        templatetotype.setTemplatessid(addTemplateParam.getId() + "");
        templatetotype.setTemplatebool(-1);
        templatetotype.setTemplatetypessid(addTemplateParam.getTemplatetypeid() + "");
        templatetotype.setCreatetime(new Date());

        int insert_type = police_templatetotypeMapper.insert(templatetotype);
        System.out.println("insert_type "+insert_type);

        addTemplateVO.setBool(insert_bool);
        result.setData(addTemplateVO);
        changeResultToSuccess(result);
        return;
    }

    public void getTemplateTypes(RResult result,ReqParam param){
        GetTemplateTypesVO getTemplateTypesVO=new GetTemplateTypesVO();

        EntityWrapper ew=new EntityWrapper();
        ew.orderBy("ordernum",false);
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

    public void addTemplateType(RResult result,ReqParam<AddTemplatetypeParam> param){

        AddTemplateTypeVO addTemplateTypeVO=new AddTemplateTypeVO();

        AddTemplatetypeParam addTemplatetypeParam = param.getParam();
        if (null==addTemplatetypeParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==addTemplatetypeParam.getTypename()){
            result.setMessage("参数为空");
            return;
        }

        //添加问题类型
        addTemplatetypeParam.setCreatetime(new Date());
        addTemplatetypeParam.setSsid(OpenUtil.getUUID_32());
        int insert_bool = police_templatetypeMapper.insert(addTemplatetypeParam);
        System.out.println("insert_bool__"+insert_bool);
        if (insert_bool<0){
            result.setMessage("系统异常");
            return;
        }

        addTemplateTypeVO.setBool(insert_bool);
        result.setData(addTemplateTypeVO);
        changeResultToSuccess(result);
        return;
    }

    public void updateTemplateType(RResult result,ReqParam param){
        return;
    }

    public void setDefaultTemplate(RResult result,ReqParam param){
        return;
    }

    public  void getProblems(RResult result, ReqParam<GetProblemsParam> param){
        GetProblemsVO getProblemsVO=new GetProblemsVO();

        GetProblemsParam getProblemsParam=param.getParam();
        if (null==getProblemsParam){
            result.setMessage("参数为空");
            return;
        }

        //分页处理
        EntityWrapper ew=new EntityWrapper();
        if (null!=getProblemsParam.getProblemtypeid()){
            ew.eq(true,"pp.problemtypessid",getProblemsParam.getProblemtypeid());
        }
        if (StringUtils.isNotBlank(getProblemsParam.getKeyword())){
            ew.like(true,"p.problem",getProblemsParam.getKeyword());
        }

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

    public void updateProblem(RResult result,ReqParam param){
        return;
    }

    public void getProblemById(RResult result,ReqParam param){
        return;
    }

    public void addProblem(RResult result,ReqParam param){
        return;
    }

    public void  getProblemTypes(RResult result,ReqParam param){
        GetProblemTypesVO getProblemTypesVO=new GetProblemTypesVO();

        EntityWrapper ew=new EntityWrapper();
        ew.orderBy("ordernum",false);
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

    /**
     * 添加问题
     * @param result
     * @param param
     */
    public void addProblemType(RResult result,ReqParam<AddProblemtypeParam> param){

        AddProblemTypeVO addProblemTypeVO=new AddProblemTypeVO();

        AddProblemtypeParam addProblemtypeParam = param.getParam();
        if (null==addProblemtypeParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==addProblemtypeParam.getTypename()){
            result.setMessage("参数为空");
            return;
        }

        //添加问题类型
        addProblemtypeParam.setCreatetime(new Date());
        addProblemtypeParam.setSsid(OpenUtil.getUUID_32());
        int insert_bool = police_problemtypeMapper.insert(addProblemtypeParam);
        System.out.println("insert_bool__"+insert_bool);
        if (insert_bool<0){
            result.setMessage("系统异常");
            return;
        }

        addProblemTypeVO.setBool(insert_bool);
        result.setData(addProblemTypeVO);
        changeResultToSuccess(result);
        return;
    }

    public void updateProblemType(RResult result,ReqParam param){
        return;
    }

    public void addOrupdateTemplateIndex(RResult result,ReqParam param){
        return;
    }

    /**
     * 模板类型
     * @param result
     * @param param
     */
    public void getTemplateTypeById(RResult result,ReqParam<GetTemplateByIdParam> param){

        GetTemplateTypesVO getTemplateTypesVO=new GetTemplateTypesVO();

        GetTemplateByIdParam getTemplateByIdParam=param.getParam();
        if (null==getTemplateByIdParam){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew=new EntityWrapper();
        if (null!=getTemplateByIdParam.getTemplatebyid()){
            ew.eq("p.templatessid",getTemplateByIdParam.getTemplatebyid());
        }

        ew.orderBy("p2.ordernum",true);
        ew.orderBy("p2.createtime",false);
        List<Templatetype> templatetypes=police_templatetotypeMapper.getTemplateTypeById(ew);

        getTemplateTypesVO.setTemplatetypes(templatetypes);
        result.setData(getTemplateTypesVO);
        changeResultToSuccess(result);
        return;
    }

    /**
     * 问题类型
     * @param result
     * @param param
     */
    public void getProblemTypeById(RResult result,ReqParam<GetProblemTypeByIdParam> param){

        GetProblemTypesVO getProblemTypesVO=new GetProblemTypesVO();

        GetProblemTypeByIdParam getProblemTypeByIdParam=param.getParam();
        if (null==getProblemTypeByIdParam){
            result.setMessage("参数为空");
            return;
        }

        //分页处理
        EntityWrapper ew=new EntityWrapper();
        if (null!=getProblemTypeByIdParam.getId()){
            ew.eq("p.id",getProblemTypeByIdParam.getId());
        }

        int count=police_problemtypeMapper.countgetProblemTypeById(ew);
        getProblemTypeByIdParam.setRecordCount(count);

        ew.orderBy("p.ordernum",true);
        ew.orderBy("p.createtime",false);
        Page<Problemtype> page=new Page<>(getProblemTypeByIdParam.getCurrPage(),getProblemTypeByIdParam.getPageSize());
        List<Problemtype> problemtypes=police_problemtypeMapper.getProblemTypeById(page,ew);

        getProblemTypesVO.setProblemtypes(problemtypes);
        result.setData(getProblemTypesVO);
        changeResultToSuccess(result);
        return;
    }

    public void templateIndex(RResult result,ReqParam param){
        return;
    }



























/***/



















}
