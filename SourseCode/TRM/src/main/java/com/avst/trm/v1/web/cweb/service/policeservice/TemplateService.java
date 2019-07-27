package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_arraignmentCount;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring5.context.SpringContextUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@Service
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

    @Autowired
    private Police_problemtotypeMapper policeProblemtotypeMapper;


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
            ew.like("t.title", getTemplatesParam.getKeyword().trim());
        }
        if (null != getTemplatesParam.getTemplatetypeid()) {
            if(getTemplatesParam.getTemplatetypeid() == -1){
//                String topF = police_templatetypeMapper.getTemplateTypeTopId();
//                ew.eq("l.templatetypessid", topF);
            }else{
                ew.eq("l.templatetypessid", getTemplatesParam.getTemplatetypeid());
            }
        }

        //分页处理
        int count = police_templateMapper.countgetTemplateList(ew);
        getTemplatesParam.setRecordCount(count);

        Page<Template> page=new Page<Template>(getTemplatesParam.getCurrPage(),getTemplatesParam.getPageSize());
        List<Template> templates = police_templateMapper.getTemplateList(page, ew);
        if (null!=templates&&templates.size()>0){
            //添加题目列表
            for (Template template : templates) {
                EntityWrapper ew2 = new EntityWrapper();

                if (null != template.getId()) {
                    ew2.eq("t.id", template.getId());
                }

                ew2.orderBy("p.ordernum", true); //fasle大到小   true小到大

                List<TemplateToProblem> templateToProblems=police_templatetoproblemMapper.getTemplateToProblems(ew2);
                if (null!=templateToProblems&&templateToProblems.size()>0){
                    template.setTemplateToProblems(templateToProblems);
                }

                //获取模板类型
//                EntityWrapper ew3 = new EntityWrapper();
//                ew3.eq("t.id", template.getId());
//                ew3.eq("p.id", getTemplatesParam.getTemplatetypeid());
//
//                String templatetype = police_templateMapper.getTemplatetype(ew3);
//                if(null != templatetype){
//                    template.setTemplatetype(templatetype);
//                }

            }
        }
        getTemplatesVO.setPagelist(templates);
        getTemplatesVO.setPageparam(getTemplatesParam);
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

//        Police_template template1 = new Police_template();
//        template1.setTitle(template.getTitle());
//        Police_template one = police_templateMapper.selectOne(template1);
//        if(null != one){
//            result.setMessage("模板名称不能重复");
//            return;
//        }

        //删除关联题目
        EntityWrapper ew=new EntityWrapper();
        ew.eq("templatessid",template.getId());
        int delete_bool = police_templatetoproblemMapper.delete(ew);
        LogUtil.intoLog(this.getClass(),"delete_bool__"+delete_bool);

        //添加关联题目
        List<Police_problem> ids=template.getTemplatetoproblemids();
        if (null!=ids&&ids.size()>0){
            for (Police_problem problem : ids) {
                Police_templatetoproblem templatetoproblem=new Police_templatetoproblem();
                templatetoproblem.setCreatetime(new Date());
                templatetoproblem.setTemplatessid(template.getId() + "");//模板id
                templatetoproblem.setProblemssid(problem.getId() + "");//题目id
                templatetoproblem.setOrdernum(problem.getOrdernum());

                //修改问题与参考答案，循环修改，如果存在的就修改，不存在的问题就新增
                if(StringUtils.isNotEmpty(problem.getProblem())){

                    EntityWrapper<Police_problem> eww = new EntityWrapper<>();
                    if(null != problem.getId()){
                        eww.eq("id", problem.getId());
                    }
                    eww.eq("problem", problem.getProblem());

                    Problem selectOne = police_problemMapper.getProblemByEw(eww);

                    if(null == problem.getId() || null == selectOne){
                        //看自定义类型是否存在，如果不存在就新增自定义问题类型 && !problem.getProblem().equals(selectOne.getProblem())
//                        Police_problemtype problemtype = new Police_problemtype();
//                        problemtype.setTypename("自定义");
//                        Police_problemtype police_problemtype = police_problemtypeMapper.selectOne(problemtype);
//                        if(null == police_problemtype){
//                            problemtype.setOrdernum(0);
//                            problemtype.setUpdatetime(new Date());
//                            problemtype.setSsid(OpenUtil.getUUID_32());
//                            int insert_bool2 = police_problemtypeMapper.insert(problemtype);
//                            //新增完自定义问题类型，获取问题类型id
//                            police_problemtype = police_problemtypeMapper.selectOne(problemtype);
//                        }
                        problem.setSsid(OpenUtil.getUUID_32());
                        problem.setCreatetime(new Date());
                        problem.setId(null);

                        Integer insert = police_problemMapper.insert(problem);

//                        Police_problem one = police_problemMapper.selectOne(problem);
                        if (insert > 0 && null != problem.getId()) {

                            templatetoproblem.setProblemssid(problem.getId() + "");

                            problem.setId(problem.getId());

                            Police_problemtotype problemtotype = new Police_problemtotype();
                            problemtotype.setProblemssid(problem.getId()+"");
//                        problemtotype.setProblemtypessid(police_problemtype.getId()+"");
                            problemtotype.setProblemtypessid("1");
                            problemtotype.setSsid(OpenUtil.getUUID_32());
                            problemtotype.setCreatetime(new Date());
                            policeProblemtotypeMapper.insert(problemtotype);//问题类型中间表
                        }

                    }else{
                        EntityWrapper ewProblem=new EntityWrapper();
                        ewProblem.eq("id",problem.getId());
                        if(StringUtils.isEmpty(problem.getReferanswer())){
                            problem.setReferanswer(selectOne.getReferanswer());
                        }
                        problem.setOrdernum(selectOne.getOrdernum());
                        Integer update = police_problemMapper.update(problem, ewProblem);//新增问题
                    }

                    templatetoproblem.setSsid(OpenUtil.getUUID_32());
                    int insert_bool = police_templatetoproblemMapper.insert(templatetoproblem);
                    LogUtil.intoLog(this.getClass(),"insert_bool__"+insert_bool);

                }

            }
        }

        EntityWrapper ewbao = new EntityWrapper();
        ewbao.eq("templatessid", template.getId());

        Police_templatetotype templatetotype = new Police_templatetotype();
//        templatetotype.setId(template.getId());
//        templatetotype.setTemplatessid(addTemplateParam.getId() + "");
//        templatetotype.setTemplatebool(-1);
        templatetotype.setTemplatetypessid(template.getTemplatetypeid() + "");
//        templatetotype.setCreatetime(new Date());

        int insert_type = police_templatetotypeMapper.update(templatetotype, ewbao);
//        LogUtil.intoLog(this.getClass(),"insert_type "+insert_type);

        //修改模板数据
        template.setUpdatetime(new Date());
        int updateById_bool=police_templateMapper.updateById(template);
        LogUtil.intoLog(this.getClass(),"updateById_bool__"+updateById_bool);
        updateTemplateVO.setBool(updateById_bool);
        result.setData(updateTemplateVO);
        if (updateById_bool<1){
            result.setMessage("系统异常");
            return;
        }

        //修改问题与参考答案，循环修改，如果存在的就修改，不存在的问题就新增
//        template.getTemplatetoproblemids()

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

        if (null==getTemplateByIdParam.getSsid()){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew = new EntityWrapper();
        ew.eq("ssid", getTemplateByIdParam.getSsid());

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

        if (null==addTemplateParam.getTitle() || null==addTemplateParam.getTemplatetoproblemids()){
            result.setMessage("参数为空");
            return;
        }

        //添加模板数据
        addTemplateParam.setCreatetime(new Date());
        addTemplateParam.setSsid(OpenUtil.getUUID_32());
//        addTemplateParam.setOrdernum(1);
        int insert_bool = police_templateMapper.insert(addTemplateParam);
        LogUtil.intoLog(this.getClass(),"insert_bool__"+insert_bool);
        if (insert_bool<0){
            result.setMessage("系统异常");
            return;
        }

        //添加模板题目关联数据p
        List<Police_problem> ids=addTemplateParam.getTemplatetoproblemids();
        if (null!=ids&&ids.size()>0){
            for (Police_problem problem : ids) {
                Police_templatetoproblem templatetoproblem=new Police_templatetoproblem();
                templatetoproblem.setCreatetime(new Date());

                templatetoproblem.setTemplatessid(addTemplateParam.getId() + "");//模板id
                templatetoproblem.setOrdernum(problem.getOrdernum());

                //修改问题与参考答案，循环修改，如果存在的就修改，不存在的问题就新增
                if(StringUtils.isNotEmpty(problem.getProblem())){

                    Police_problem selectOne = police_problemMapper.selectOne(problem);

                    if(null == problem.getId() || null == selectOne){
                        problem.setSsid(OpenUtil.getUUID_32());
                        problem.setCreatetime(new Date());
                        problem.setId(null);

                        Integer insert = police_problemMapper.insert(problem);

                        Police_problem one = police_problemMapper.selectOne(problem);
                        templatetoproblem.setProblemssid(one.getId() + "");

                        problem.setId(one.getId());

                        Police_problemtotype problemtotype = new Police_problemtotype();
                        problemtotype.setProblemssid(problem.getId()+"");
                        problemtotype.setProblemtypessid("1");
                        problemtotype.setSsid(OpenUtil.getUUID_32());
                        problemtotype.setCreatetime(new Date());
                        policeProblemtotypeMapper.insert(problemtotype);//问题类型中间表

                    }else{
                        EntityWrapper ewProblem=new EntityWrapper();
                        ewProblem.eq("id",problem.getId());
                        Integer update = police_problemMapper.update(problem, ewProblem);//修改问题
                    }

                    templatetoproblem.setProblemssid(problem.getId() + "");//题目id
                    templatetoproblem.setSsid(OpenUtil.getUUID_32());
                    int police_templatetoprobleminsert_bool = police_templatetoproblemMapper.insert(templatetoproblem);
                    LogUtil.intoLog(this.getClass(),"police_templatetoprobleminsert_bool"+police_templatetoprobleminsert_bool);

                }
            }
        }

        Police_templatetotype templatetotype = new Police_templatetotype();
        templatetotype.setTemplatessid(addTemplateParam.getId() + "");
        templatetotype.setTemplatebool(-1);
        templatetotype.setTemplatetypessid(addTemplateParam.getTemplatetypeid() + "");
        templatetotype.setCreatetime(new Date());

        int insert_type = police_templatetotypeMapper.insert(templatetotype);
        LogUtil.intoLog(this.getClass(),"insert_type "+insert_type);

        addTemplateVO.setBool(insert_bool);
        result.setData(addTemplateVO);
        changeResultToSuccess(result);
        return;
    }

    public void getTemplateTypes(RResult result,ReqParam<ProblemtypeParam> param){
        GetTemplateTypesVO getTemplateTypesVO=new GetTemplateTypesVO();

        ProblemtypeParam paramParam = param.getParam();

        EntityWrapper ew=new EntityWrapper();
        ew.orderBy("ordernum",false);
        ew.orderBy("createtime",false);

        if (StringUtils.isNotBlank(paramParam.getTypename())){
            ew.like(true,"typename",paramParam.getTypename());
        }

        int count=police_templatetypeMapper.selectCount(ew);
        paramParam.setRecordCount(count);

        Page<Problem> page=new Page<>(paramParam.getCurrPage(),paramParam.getPageSize());
        List<Police_problemtype> list=police_templatetypeMapper.selectPage(page,ew);

        List<Templatetype> templatetypes=new ArrayList<>();
        if (null!=list&&list.size()>0){
            templatetypes = gson.fromJson(gson.toJson(list), new TypeToken<List<Templatetype>>(){}.getType());
        }
        getTemplateTypesVO.setPagelist(templatetypes);
        getTemplateTypesVO.setPageparam(paramParam);

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

        //添加模板类型
        addTemplatetypeParam.setCreatetime(new Date());
        addTemplatetypeParam.setSsid(OpenUtil.getUUID_32());
        int insert_bool = police_templatetypeMapper.insert(addTemplatetypeParam);
        LogUtil.intoLog(this.getClass(),"insert_bool__"+insert_bool);
        if (insert_bool<0){
            result.setMessage("系统异常");
            return;
        }

        addTemplateTypeVO.setBool(insert_bool);
        result.setData(addTemplateTypeVO);
        changeResultToSuccess(result);
        return;
    }

    public void updateTemplateType(RResult result,ReqParam<UpdateTemplateTypeParam> param){
        DefaultTemplateVO defaultTemplateVO=new DefaultTemplateVO();

        UpdateTemplateTypeParam updateTemplateTypeParam = param.getParam();
        if (null==updateTemplateTypeParam){
            result.setMessage("参数为空");
            return;
        }

        if (null == updateTemplateTypeParam.getSsid() || null == updateTemplateTypeParam.getTypename()) {
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew = new EntityWrapper();
        if (null != updateTemplateTypeParam.getSsid()) {
            ew.eq("ssid", updateTemplateTypeParam.getSsid());
        }

        Integer update_bool = police_templatetypeMapper.update(updateTemplateTypeParam, ew);

        LogUtil.intoLog(this.getClass(),"update_bool__"+update_bool);
        if (update_bool<0){
            result.setMessage("系统异常");
            return;
        }

        defaultTemplateVO.setBool(update_bool);
        result.setData(defaultTemplateVO);
        changeResultToSuccess(result);
        return;
    }

    public void setDefaultTemplate(RResult result,ReqParam<DefaultTemplateParam> param){
        DefaultTemplateVO defaultTemplateVO=new DefaultTemplateVO();

        DefaultTemplateParam defaultTemplateParam = param.getParam();
        if (null==defaultTemplateParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==defaultTemplateParam.getTemplatebool() || null==defaultTemplateParam.getTemplatessid() || null==defaultTemplateParam.getTemplatetypessid()){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew = new EntityWrapper();
        if (null != defaultTemplateParam.getTemplatessid()) {
            ew.eq("templatessid", defaultTemplateParam.getTemplatessid());
        }
        if (null != defaultTemplateParam.getTemplatetypessid()) {
            ew.eq("templatetypessid", defaultTemplateParam.getTemplatetypessid());
        }

        //修改模板为默认模板
        int update_bool = police_templatetotypeMapper.update(defaultTemplateParam, ew);
        LogUtil.intoLog(this.getClass(),"update_bool__"+update_bool);
        if (update_bool<0){
            result.setMessage("系统异常");
            return;
        }else if(update_bool==0){
            result.setMessage("没有找到该模板");
            return;
        }

        defaultTemplateVO.setBool(update_bool);
        result.setData(defaultTemplateVO);
        changeResultToSuccess(result);
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

        getProblemsVO.setPagelist(problems);
        getProblemsVO.setPageparam(getProblemsParam);
        result.setData(getProblemsVO);
        changeResultToSuccess(result);
        return;
    }

    public void updateProblem(RResult result,ReqParam<UpdateProblemParam> param){

        UpdateProblemVO updateProblemVO=new UpdateProblemVO();

        UpdateProblemParam updateProblemParam = param.getParam();
        if (null==updateProblemParam){
            result.setMessage("参数为空");
            return;
        }
        if (StringUtils.isEmpty(updateProblemParam.getProblem().trim())){
            result.setMessage("问题不能为空");
            return;
        }



        if (null==updateProblemParam.getId()){
            result.setMessage("参数为空");
            return;
        }
        if (null == updateProblemParam.getProblem() && null == updateProblemParam.getReferanswer()) {
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew=new EntityWrapper();
        if (null!=updateProblemParam.getId()){
            ew.eq("id",updateProblemParam.getId());
        }

        //修改问题
        int update_bool = police_problemMapper.update(updateProblemParam, ew);

        EntityWrapper ew2=new EntityWrapper();
        if (null!=updateProblemParam.getProblemtypessid()){
            ew2.eq("problemtypessid",updateProblemParam.getProblemtypessid());
        }
        if (null!=updateProblemParam.getProblemssid()){
            ew2.eq("problemssid",updateProblemParam.getProblemssid());
        }

        if (null!=updateProblemParam.getProblemtypessid()){

            Police_problemtotype problemtotype = new Police_problemtotype();
            problemtotype.setProblemtypessid(updateProblemParam.getProblemtypessidV());
            problemtotype.setProblemssid(updateProblemParam.getId() + "");

            //problemtypessidV
            Police_problemtotype problemtotype1 = policeProblemtotypeMapper.selectOne(problemtotype);
            if(null != problemtotype1 && null != problemtotype1.getId()){
                problemtotype1.setProblemtypessid(updateProblemParam.getProblemtypessid());

                int updateType_bool = policeProblemtotypeMapper.updateById(problemtotype1);
                LogUtil.intoLog(this.getClass(),"updateType_bool "+updateType_bool);
            }else{
                //根据原版如果有就修改，没有就新增，新增的如果有了就不添加到关联表
                problemtotype.setProblemtypessid(updateProblemParam.getProblemtypessid());
                problemtotype1 = policeProblemtotypeMapper.selectOne(problemtotype);
                if(null == problemtotype1){
                    problemtotype.setCreatetime(new Date());
                    problemtotype.setSsid(OpenUtil.getUUID_32());
                    policeProblemtotypeMapper.insert(problemtotype);
                }
            }

        }

        LogUtil.intoLog(this.getClass(),"update_bool "+update_bool);
        if (update_bool<0){
            result.setMessage("系统异常");
            return;
        }

        updateProblemVO.setBool(update_bool);
        result.setData(updateProblemVO);
        changeResultToSuccess(result);
        return;
    }

    public void getProblemById(RResult result,ReqParam<GetProblemsByIdParam> param){
        ProblemByIdVO problemByIdVO=new ProblemByIdVO();

        GetProblemsByIdParam getProblemsByIdParam = param.getParam();
        if (null==getProblemsByIdParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==getProblemsByIdParam.getId()){
            result.setMessage("参数为空");
            return;
        }

        Police_problem problem = new Police_problem();
        problem.setSsid(getProblemsByIdParam.getId());

        //查询一个问题
        Police_problem ProblemById = police_problemMapper.selectOne(problem);

        if (null==ProblemById){
            result.setMessage("未找到该问题信息");
            return;
        }

        problemByIdVO.setProblem(ProblemById);
        result.setData(problemByIdVO);
        changeResultToSuccess(result);
        return;
    }

    public void addProblem(RResult result,ReqParam<AddProblemParam> param){

        AddProblemVO addProblemVO=new AddProblemVO();
        Police_problemtotype problemtotype = new Police_problemtotype();

        AddProblemParam addProblemParam = param.getParam();
        if (null==addProblemParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==addProblemParam.getProblem() || null==addProblemParam.getReferanswer() || null==addProblemParam.getProblemtypessid()){
            result.setMessage("参数为空");
            return;
        }

        //添加问题类型
        addProblemParam.setCreatetime(new Date());
        addProblemParam.setSsid(OpenUtil.getUUID_32());
        int insert_bool = police_problemMapper.insert(addProblemParam);

        Police_problem police_problem = police_problemMapper.selectOne(addProblemParam);
        problemtotype.setProblemssid(police_problem.getId() + "");
        problemtotype.setProblemtypessid(addProblemParam.getProblemtypessid());
        problemtotype.setSsid(OpenUtil.getUUID_32());
        problemtotype.setCreatetime(new Date());

        int insert_totype = policeProblemtotypeMapper.insert(problemtotype);
        LogUtil.intoLog(this.getClass(),"insert_bool__"+insert_bool);
        if (insert_bool<0){
            result.setMessage("系统异常");
            return;
        }

        addProblemVO.setBool(insert_bool);
        result.setData(addProblemVO);
        changeResultToSuccess(result);
        return;
    }

    public void  getProblemTypes(RResult result,ReqParam<ProblemtypeParam> param){
        GetProblemTypesVO getProblemTypesVO=new GetProblemTypesVO();

        ProblemtypeParam paramReqParam = param.getParam();

        EntityWrapper ew=new EntityWrapper();
        ew.orderBy("ordernum",false);
        ew.orderBy("createtime",false);

        if (StringUtils.isNotBlank(paramReqParam.getTypename())){
            ew.like(true,"typename",paramReqParam.getTypename());
        }

        int count=police_problemtypeMapper.selectCount(ew);
        paramReqParam.setRecordCount(count);

        Page<Problem> page=new Page<>(paramReqParam.getCurrPage(),paramReqParam.getPageSize());
        List<Police_problemtype> list=police_problemtypeMapper.selectPage(page,ew);
        List<Problemtype> problemtypes=new ArrayList<>();
        if (null!=list&&list.size()>0){
            problemtypes = gson.fromJson(gson.toJson(list), new TypeToken<List<Problemtype>>(){}.getType());
        }

        getProblemTypesVO.setPagelist(problemtypes);
        getProblemTypesVO.setPageparam(paramReqParam);
        result.setData(getProblemTypesVO);
        changeResultToSuccess(result);
        return;
    }

    /**
     * 添加问题类型
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
        LogUtil.intoLog(this.getClass(),"insert_bool__"+insert_bool);
        if (insert_bool<0){
            result.setMessage("系统异常");
            return;
        }

        addProblemTypeVO.setBool(insert_bool);
        result.setData(addProblemTypeVO);
        changeResultToSuccess(result);
        return;
    }

    public void updateProblemType(RResult result,ReqParam<UpdateProblemtypeParam> param){

        UpdateProblemTypeVO updateProblemTypeVO=new UpdateProblemTypeVO();

        UpdateProblemtypeParam updateProblemtypeParam = param.getParam();
        if (null==updateProblemtypeParam){
            result.setMessage("参数为空");
            return;
        }

        if (null==updateProblemtypeParam.getTypename() || null==updateProblemtypeParam.getOrdernum() || null==updateProblemtypeParam.getSsid()){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew=new EntityWrapper();
        if (null!=updateProblemtypeParam.getSsid()){
            ew.eq("ssid",updateProblemtypeParam.getSsid());
        }

        //添加问题类型
        int update_bool = police_problemtypeMapper.update(updateProblemtypeParam, ew);
        LogUtil.intoLog(this.getClass(),"update_bool__"+update_bool);
        if (update_bool<0){
            result.setMessage("系统异常");
            return;
        }

        updateProblemTypeVO.setBool(update_bool);
        result.setData(updateProblemTypeVO);
        changeResultToSuccess(result);
        return;
    }

    public void addOrupdateTemplateIndex(RResult result,ReqParam param){
        return;
    }

    /**
     * 查询单个模板类型
     * @param result
     * @param param
     */
    public void getTemplateTypeById(RResult result,ReqParam<Police_templatetype> param){

        Police_templatetype police_templatetype=param.getParam();
        if (null==police_templatetype){
            result.setMessage("参数为空");
            return;
        }

        Police_templatetype templatetype=police_templatetypeMapper.selectOne(police_templatetype);

        result.setData(templatetype);
        changeResultToSuccess(result);
        return;
    }

    /**
     * 问题类型
     * @param result
     * @param param
     */
    public void getProblemTypeById(RResult result,ReqParam<Police_problemtype> param){

        Police_problemtype getProblemTypeByIdParam=param.getParam();
        if (null==getProblemTypeByIdParam){
            result.setMessage("参数为空");
            return;
        }

        Police_problemtype problemtypes=police_problemtypeMapper.selectOne(getProblemTypeByIdParam);

        result.setData(problemtypes);
        changeResultToSuccess(result);
        return;
    }

    public void templateIndex(RResult result,ReqParam param){
        return;
    }

    /**
     * 生成word文件
     * @param result
     * @param param
     */
    public void templateWord(RResult result, ReqParam<TemplateWordParam> param) {

        TemplateWordParam wordParam = param.getParam();

        //查出数据放这里
        EntityWrapper ew = new EntityWrapper();
        ew.eq("ssid", wordParam.getTemplatessid());

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

        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("title",template.getTitle()==null?"":template.getTitle());

        List<ProblemVO> arrayList = new ArrayList<>();

        for (TemplateToProblem templateToProblem : templateToProblems) {
            ProblemVO vo = new ProblemVO();
            vo.setWen("问：" + templateToProblem.getProblem());
            vo.setDa("答：" + templateToProblem.getReferanswer());
            arrayList.add(vo);
        }

        dataMap.put("arrayList",arrayList);

        try {

            /*template*/
            Configuration configuration = new Configuration(new Version("2.3.23"));
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassForTemplateLoading(RecordService.class, "/config");

            //以utf-8的编码读取ftl文件
            freemarker.template.Template templateDamo = configuration.getTemplate("template_word.ftl","UTF-8");
            String filePath=PropertiesListenerConfig.getProperty("file.template");
            String filePathNew = OpenUtil.createpath_fileByBasepath(filePath);

            File fileMkdir = new File(filePathNew);
            if (!fileMkdir.exists()) {
                //如果不存在，就创建该目录
                fileMkdir.mkdirs();
            }
//            String filename=record.getRecordname();
            String filename=template.getTitle()==null?"笔录模板":template.getTitle();
            String path = filePathNew +filename+".doc";

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"), 10240);
            templateDamo.process(dataMap, out);
            out.flush();
            out.close();

            String uploadpath= OpenUtil.strMinusBasePath(PropertiesListenerConfig.getProperty("file.qg"),path);
            String uploadbasepath=PropertiesListenerConfig.getProperty("upload.basepath");
            result.setData(uploadbasepath + uploadpath);

            changeResultToSuccess(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成Ecxcel文件
     * @param result
     * @param param
     */
    public void templateExcel(RResult result, ReqParam<TemplateWordParam> param) {

        TemplateWordParam wordParam = param.getParam();

        //查出数据放这里
        EntityWrapper ew = new EntityWrapper();
        ew.eq("ssid", wordParam.getTemplatessid());

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

        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("title",template.getTitle()==null?"":template.getTitle());

        List<ProblemVO> arrayList = new ArrayList<>();

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(template.getTitle() == null ? "笔录模板" : template.getTitle());
        sheet.setColumnWidth(0, 50000);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(template.getTitle() == null ? "笔录模板" : template.getTitle());
        cell.setCellStyle(style);

        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font=wb.createFont();
        font.setColor(HSSFColor.RED.index);
        style2.setFont(font);

        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font2=wb.createFont();
        font2.setColor(HSSFColor.BLUE.index);
        style3.setFont(font2);

        int i = 1;
        for (TemplateToProblem templateToProblem : templateToProblems) {
            row = sheet.createRow(i++);
            HSSFCell cell2 = row.createCell((short) 0);
            cell2.setCellValue("问：" + templateToProblem.getProblem()); // 问题
            cell2.setCellStyle(style2);
//            row = sheet.createRow(i++);
//            HSSFCell cell3 = row.createCell((short) 0);
//            cell3.setCellValue("答：" + templateToProblem.getReferanswer()); // 参考答案
//            cell3.setCellStyle(style3);
        }

        try {
            //String zipspath = OutsideDataRead.getproperty(OutsideDataRead.sys_pro, "zipspath");
            // 创建目录
            String filePath=PropertiesListenerConfig.getProperty("file.template");
            String filePathNew = OpenUtil.createpath_fileByBasepath(filePath);
            File fileMkdir = new File(filePathNew);
            if (!fileMkdir.exists()) {
                //如果不存在，就创建该目录
                fileMkdir.mkdirs();
            }

            String path = filePathNew + (template.getTitle() == null ? "模板" : template.getTitle() + ".xls");
            FileOutputStream fout = new FileOutputStream(path);
            wb.write(fout);
            fout.close();

            String uploadpath= OpenUtil.strMinusBasePath(PropertiesListenerConfig.getProperty("file.qg"),path);
            String uploadbasepath=PropertiesListenerConfig.getProperty("upload.basepath");
            result.setData(uploadbasepath + uploadpath);

            this.changeResultToSuccess(result);
            result.setMessage("Excel导出成功，请稍后...");

        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("获取下载案件失败");
        }

    }

    /**
     * 导入模板
     * @param result
     * @param file  上传文件
     * @param tmplateTypeId  模板类型id
     * @param repeatStatus  是否同意覆盖，不为空就是同意
     */
    public void uploadFile(RResult result, MultipartFile file, String tmplateTypeId, String repeatStatus) {
        if (file.isEmpty()) {
            result.setMessage("上传失败，请选择文件");
        }

        List<String> list = new ArrayList<>();
        InputStream fis = null;

        try {
            fis = file.getInputStream();

//            String fileHeader = getFileHeader(fis);
//            LogUtil.intoLog(this.getClass(),"fileHeader : " + fileHeader);

            String textFileName=file.getOriginalFilename();
            if(textFileName.endsWith(".doc")) { //判断文件格式
                LogUtil.intoLog(this.getClass(),".doc");
                result.setMessage("文件格式错误，请使用xls，xlsx格式上传");//doc，docx，
                return;
//                WordExtractor wordExtractor = new WordExtractor(fis);//使用HWPF组件中WordExtractor类从Word文档中提取文本或段落
//                for(String words : wordExtractor.getParagraphText()){//获取段落内容
//                    words = words.trim();
//                    if (!"".equals(words.trim())) {
//                        list.add(words);
//                    }
////                    LogUtil.intoLog(this.getClass(),words);
//                }

            }else if(textFileName.endsWith(".docx")){
                LogUtil.intoLog(this.getClass(),".docx");
                result.setMessage("文件格式错误，请使用xls，xlsx格式上传");//doc，docx，
                return;

//                XWPFDocument xwpfDocument = new XWPFDocument(fis);
//                Iterator<XWPFParagraph> iterator = xwpfDocument.getParagraphsIterator();
//                XWPFParagraph next;
//                while (iterator.hasNext()){
//                    next = iterator.next();
//                    String text = next.getText().trim();
//                    if (!"".equals(text)) {
//                        list.add(text);
//                    }
////                    LogUtil.intoLog(this.getClass(),next.getText());
//                }

            }else if(textFileName.endsWith(".xls")){
                LogUtil.intoLog(this.getClass(),".xlsx  .xls");

                Workbook workbook = new HSSFWorkbook(file.getInputStream());
                //获取所有的工作表的的数量
                int numOfSheet = workbook.getNumberOfSheets();

                //遍历这个这些表
                for (int i = 0; i < numOfSheet; i++) {
                    //获取一个sheet也就是一个工作簿
                    Sheet sheet = workbook.getSheetAt(i);
                    int lastRowNum = sheet.getLastRowNum();
                    //从第一行开始第一行一般是标题
                    for (int j = 0; j <= lastRowNum; j++) {
                        Row row = sheet.getRow(j);
                        //获取第一条单元格
                        if (null != row && null != row.getCell(0)) {
                            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                            String longName = row.getCell(0).getStringCellValue();

                            String text = longName.trim();
                            if (!"".equals(text)) {
                                list.add(text);
                            }
//                            LogUtil.intoLog(this.getClass(),longName);
                        }
                    }
                }

            }else{
                result.setMessage("文件格式错误，请使用xls格式上传");//doc，docx，
                return;
            }
        } catch (IOException e) {
            result.setMessage("文件格式错误，请使用xls格式上传");//doc，docx，
            e.printStackTrace();
            return;
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.intoLog(this.getClass(),list);

        //插入模板表
        if(null != list && list.size() > 0){
            Template selectOne = null; //保存覆盖的模板
            //先判断文件是否已经存在，如果存在就返回一个参数，，让前端再上传一次，但是带参数的
            //参数再次点击
            if(null == repeatStatus || "null".equalsIgnoreCase(repeatStatus)){

                String fileTitle = list.get(0);

                EntityWrapper ewt = new EntityWrapper();
                ewt.eq("t.title", fileTitle);
                ewt.eq("e.id", tmplateTypeId);
                List<Template> templateLists = police_templateMapper.getTemplateLists(ewt);

                if (null != templateLists && templateLists.size() > 0) {
                    selectOne = templateLists.get(0);
                    result.setData(504);
                    result.setMessage("模板已经存在，是否覆盖该模板");
                    return;
                }
            }

            String title = "";
            String problem = "";
            int templateID = 0; //模板id
            for(int i=0;i<list.size();i++){
                if(i==0){
                    title = list.get(0);
                    Police_template police_template = new Police_template();
                    police_template.setTitle(title);

                    //如果本来就有的，就在名字后面加一个副本，如果没有的，就直接新增
                    int num = 0;
                    while (true) {
                        if (num > 0) {
                            title += " 副本";
                        }
                        EntityWrapper ewt = new EntityWrapper();
                        ewt.eq("t.title", title);
                        ewt.eq("e.id", tmplateTypeId);
                        List<Template> templateLists = police_templateMapper.getTemplateLists(ewt);

                        //不存在
                        if (null == templateLists || templateLists.size() == 0) {
                            police_template.setTitle(title);
                            break;
                        }else {
                            //存在，并且选择的是覆盖，就进来
                            //如果本来有就不新增了
                            if (null != repeatStatus && "1".equalsIgnoreCase(repeatStatus)) {
                                //把覆盖的模板复制出去
                                selectOne = templateLists.get(0);
                                break;
                            }
                        }
                        num++;
                    }


                    //覆盖就是不新增
                    //否则全部新增
                    if(null == repeatStatus || "null".equalsIgnoreCase(repeatStatus) || "2".equalsIgnoreCase(repeatStatus)){
                        police_template.setCreatetime(new Date());
                        police_template.setSsid(OpenUtil.getUUID_32());
                        police_template.setOrdernum(0);
                        police_templateMapper.insert(police_template);
                        selectOne = new Template(police_template);

                        //添加模板类型
                        Police_templatetotype templatetotype = new Police_templatetotype();
                        templatetotype.setTemplatessid(police_template.getId() + "");
                        templatetotype.setTemplatebool(-1);
                        //如何为空，获取排序第一的id
                        if(null == tmplateTypeId){
                            tmplateTypeId = police_templatetypeMapper.getTemplateTypeTopId();
                        }
                        templatetotype.setTemplatetypessid(tmplateTypeId);
                        templatetotype.setCreatetime(new Date());
                        police_templatetotypeMapper.insert(templatetotype);
                    }

                    templateID = selectOne.getId();

                    EntityWrapper ew = new EntityWrapper();
                    if(templateID != 0){
                        ew.eq("templatessid", templateID);
                        police_templatetoproblemMapper.delete(ew);
                    }
                    continue;
                }else{
                    problem = list.get(i);
                    String wen = "";
                    if(problem.indexOf("问：") != -1){
                        wen = "问：";
                    }else if(problem.indexOf("问:") != -1){
                        wen = "问:";
                    }
                    String replace = problem.replace(wen, "");
                    if(StringUtils.isNotEmpty(replace)){
                        problem = replace;
                    }

                }
                //如果判断模板是否存在，如果存在，就修改题目
                //不存在就添加模板，然后添加关联题目

                if(StringUtils.isNotEmpty(problem)){
                    EntityWrapper ewp = new EntityWrapper();
                    ewp.eq("problem", problem);
                    //问题可能有多个同样的，所以要获取全部，然后获取第一个问题
                    List<Police_problem> problemList = police_problemMapper.selectList(ewp);
                    Police_problem addProblemParam = null;

                    if(null == problemList || problemList.size()==0){
                        //添加问题类型
                        Police_problem police_problem = new Police_problem();
                        police_problem.setProblem(problem);
                        police_problem.setCreatetime(new Date());
                        police_problem.setSsid(OpenUtil.getUUID_32());
                        police_problem.setOrdernum(0);
                        police_problem.setReferanswer("");
                        Integer insert = police_problemMapper.insert(police_problem);
                        addProblemParam = police_problem;
//                        addProblemParam = police_problemMapper.selectOne(police_problem);
                    }else{
                        addProblemParam = problemList.get(0);
                    }

                    //添加进关联表里
                    Police_templatetoproblem templatetoproblem=new Police_templatetoproblem();
                    templatetoproblem.setCreatetime(new Date());

                    templatetoproblem.setTemplatessid(templateID + "");//模板id
                    templatetoproblem.setProblemssid(addProblemParam.getId() + "");//题目id
                    templatetoproblem.setOrdernum(0);
                    templatetoproblem.setSsid(OpenUtil.getUUID_32());
                    police_templatetoproblemMapper.insert(templatetoproblem);
                }
            }


            //插入问题表
            //请求成功，展示出来
            this.changeResultToSuccess(result);
            result.setMessage("模板导入成功，请稍后...");
        }
    }



/***/














}
