package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.SysYmlCache;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.cache.param.SysYmlParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.police.entity.*;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.*;
import com.avst.trm.v1.common.datasourse.police.mapper.*;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.service.baseservice.MainService;
import com.avst.trm.v1.web.cweb.vo.policevo.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private Police_problemtotypeMapper police_problemtotypeMapper;

    @Autowired
    private MainService mainService;

    public void getTemplates(RResult result, ReqParam<GetTemplatesParam>  param){
        GetTemplatesVO getTemplatesVO=new GetTemplatesVO();

        //请求参数转换
        GetTemplatesParam getTemplatesParam = param.getParam();
       if (null==getTemplatesParam){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper ew = new EntityWrapper();
        ew.orderBy("t.createtime", false);

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
            result.setMessage("参数不能为空");
            return;
        }

        if (null==template.getId()){
            result.setMessage("id不能为空");
            return;
        }

        //判断该模板是否已经存在
        EntityWrapper<Police_template> ewww = new EntityWrapper<>();
        ewww.eq("title", template.getTitle());
        ewww.ne("id", template.getId());
        List<Police_template> selectList = police_templateMapper.selectList(ewww);

        if (null != selectList) {
            for (Police_template police_template : selectList) {
                Police_templatetotype templatetotype1 = new Police_templatetotype();
                templatetotype1.setTemplatessid(police_template.getId() + "");
                templatetotype1.setTemplatetypessid(template.getTemplatetypeid() + "");

                Police_templatetotype police_templatetotype = police_templatetotypeMapper.selectOne(templatetotype1);
                if (null != police_templatetotype) {
                    result.setMessage("该模板名已经存在，是否继续修改？");
                    return;
                }
            }
        }

        //删除关联题目
        EntityWrapper ew=new EntityWrapper();
        ew.eq("templatessid",template.getId());
        int delete_bool = police_templatetoproblemMapper.delete(ew);
        LogUtil.intoLog(this.getClass(),"delete_bool__"+delete_bool);

        //取出授权，如果是单机版的，就自动选择谈话
        String problemtypessid = "1";//获取数据库问题类型ssid为1的类型
        SysYmlParam ymlParam = SysYmlCache.getSysYmlParam();
        String gnlist = ymlParam.getGnlist();
        if (gnlist.indexOf("hk_o") != -1) {
            Police_problemtype problemtype = new Police_problemtype();
            problemtype.setTypename("谈话");
            Police_problemtype selectOne = police_problemtypeMapper.selectOne(problemtype);
            problemtypessid = selectOne.getSsid();
        }else{
            List<Police_problemtype> police_problemtypes = police_problemtypeMapper.selectList(null);
            if (police_problemtypes.size() > 0) {
                Police_problemtype police_problemtype = police_problemtypes.get(0);
                problemtypessid = police_problemtype.getSsid();
            }
        }

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
                            problemtotype.setProblemssid(problem.getSsid());
//                        problemtotype.setProblemtypessid(police_problemtype.getId()+"");
                            problemtotype.setProblemtypessid(problemtypessid);
                            problemtotype.setSsid(OpenUtil.getUUID_32());
                            problemtotype.setCreatetime(new Date());
                            police_problemtotypeMapper.insert(problemtotype);//问题类型中间表
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

        //判断该模板是否已经存在
        EntityWrapper<Police_template> ewww = new EntityWrapper<>();
        ewww.eq("title", addTemplateParam.getTitle());
        List<Police_template> selectList = police_templateMapper.selectList(ewww);

        if (null != selectList) {
            for (Police_template police_template : selectList) {
                Police_templatetotype templatetotype1 = new Police_templatetotype();
                templatetotype1.setTemplatessid(police_template.getId() + "");
                templatetotype1.setTemplatetypessid(addTemplateParam.getTemplatetypeid() + "");

                Police_templatetotype police_templatetotype = police_templatetotypeMapper.selectOne(templatetotype1);
                if (null != police_templatetotype) {
                    result.setMessage("该模板名已经存在，是否继续新增？");
                    return;
                }
            }
        }
        //如果新增就改个名字，如果是覆盖就直接覆盖了

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


        //取出授权，如果是单机版的，就自动选择谈话
        String problemtypessid = "1";//获取数据库问题类型ssid为1的类型
        SysYmlParam ymlParam = SysYmlCache.getSysYmlParam();
        String gnlist = ymlParam.getGnlist();
        if (gnlist.indexOf("hk_o") != -1) {
            Police_problemtype problemtype = new Police_problemtype();
            problemtype.setTypename("谈话");
            Police_problemtype selectOne = police_problemtypeMapper.selectOne(problemtype);
            problemtypessid = selectOne.getSsid();
        }else{
            List<Police_problemtype> police_problemtypes = police_problemtypeMapper.selectList(null);
            if (police_problemtypes.size() > 0) {
                Police_problemtype police_problemtype = police_problemtypes.get(0);
                problemtypessid = police_problemtype.getSsid();
            }
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

                    String referanswer = problem.getReferanswer();
                    Integer ordernum = problem.getOrdernum();
                    problem.setOrdernum(null);
                    problem.setReferanswer(null);
                    Police_problem selectOne = police_problemMapper.selectOne(problem);
                    problem.setOrdernum(ordernum);

                    if(null == problem.getId() || null == selectOne){
                        problem.setSsid(OpenUtil.getUUID_32());
                        problem.setCreatetime(new Date());
                        problem.setOrdernum(0);
                        problem.setId(null);
                        problem.setReferanswer(referanswer);
                        if(StringUtils.isBlank(problem.getReferanswer())){
                            problem.setReferanswer("");
                        }

                        Integer insert = police_problemMapper.insert(problem);

                        templatetoproblem.setProblemssid(problem.getId() + "");

                        problem.setId(problem.getId());

                        Police_problemtotype problemtotype = new Police_problemtotype();
                        problemtotype.setProblemssid(problem.getSsid());
                        problemtotype.setProblemtypessid(problemtypessid);
                        problemtotype.setSsid(OpenUtil.getUUID_32());
                        problemtotype.setCreatetime(new Date());
                        police_problemtotypeMapper.insert(problemtotype);//问题类型中间表

                    }else{
                        EntityWrapper ewProblem=new EntityWrapper();
                        ewProblem.eq("ssid",problem.getSsid());
                        problem.setReferanswer(referanswer);
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
            result.setMessage("参数不能为空");
            return;
        }

        if (StringUtils.isBlank(addTemplatetypeParam.getTypename())){
            result.setMessage("模板类型名称不能为空");
            return;
        }

        if (null == addTemplatetypeParam.getOrdernum()){
            result.setMessage("排序不能为空");
            return;
        }

        /**判断一下是否重名**/
        Police_templatetype police_problemtype = new Police_templatetype();
        police_problemtype.setTypename(addTemplatetypeParam.getTypename());
        Police_templatetype templatetype = police_templatetypeMapper.selectOne(police_problemtype);
        if (null != templatetype) {
            result.setMessage("模板类型的名称已存在，请重新设置");
            return;
        }

        /**判断顺序是否已经存在**/
        if (addTemplatetypeParam.getOrdernum() > 0) {
            Police_templatetype templatetype1 = new Police_templatetype();

            templatetype1.setOrdernum(addTemplatetypeParam.getOrdernum());
            Police_templatetype selectOne = police_templatetypeMapper.selectOne(templatetype1);
            if (null != selectOne) {
                result.setMessage("排序不能重复，请重新排序");
                return;
            }
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
            result.setMessage("参数不能为空");
            return;
        }

        if (null == updateTemplateTypeParam.getSsid()) {
            result.setMessage("ssid不能为空");
            return;
        }

        if(StringUtils.isBlank(updateTemplateTypeParam.getTypename())){
            result.setMessage("修改的模板类型名不能为空");
            return;
        }

        if (null == updateTemplateTypeParam.getOrdernum()) {
            result.setMessage("排序不能为空");
            return;
        }

        Police_templatetype police_problemtype = new Police_templatetype();
        police_problemtype.setTypename(updateTemplateTypeParam.getTypename());
        police_problemtype.setOrdernum(updateTemplateTypeParam.getOrdernum());
        police_problemtype.setSsid(updateTemplateTypeParam.getSsid());
        Police_templatetype selectOne1 = police_templatetypeMapper.selectOne(police_problemtype);

        if (null == selectOne1) {
            /**判断一下是否重名**/
            EntityWrapper<Police_templatetype> ew1 = new EntityWrapper<>();
            ew1.eq("typename", updateTemplateTypeParam.getTypename());
            ew1.ne("ssid", updateTemplateTypeParam.getSsid());
            List<Police_templatetype> police_templatetypes = police_templatetypeMapper.selectList(ew1);

            if (null != police_templatetypes && police_templatetypes.size() > 0) {
                result.setMessage("模板类型的名称已存在，请重新设置");
                return;
            }

            /**判断顺序是否已经存在**/
            if (updateTemplateTypeParam.getOrdernum() > 0) {
                EntityWrapper<Police_templatetype> ew2 = new EntityWrapper<>();
                ew2.eq("ordernum", updateTemplateTypeParam.getOrdernum());
                ew2.ne("ssid", updateTemplateTypeParam.getSsid());
                List<Police_templatetype> templatetypes = police_templatetypeMapper.selectList(ew2);

                if (null != templatetypes && templatetypes.size() > 0) {
                    result.setMessage("排序不能重复，请重新排序");
                    return;
                }
            }
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
        if (StringUtils.isNotBlank(getProblemsParam.getProblemtypeid())){
            ew.eq(true,"pp.problemtypessid",getProblemsParam.getProblemtypeid());
        }
        if (StringUtils.isNotBlank(getProblemsParam.getKeyword())){
            ew.like(true,"p.problem",getProblemsParam.getKeyword());
        }

        int count=police_problemMapper.countgetProblemList(ew);
        getProblemsParam.setRecordCount(count);

        ew.orderBy("p.createtime",false);
        ew.orderBy("p.ordernum",false);
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
        if (StringUtils.isBlank(updateProblemParam.getProblem())){
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

        EntityWrapper<Police_problem> wrapper = new EntityWrapper<Police_problem>();
        wrapper.eq("problem", updateProblemParam.getProblem());
        wrapper.ne("ssid", updateProblemParam.getSsid());
        List<Police_problem> police_problems = police_problemMapper.selectList(wrapper);

        for (Police_problem police_problem : police_problems) {
            Police_problemtotype problemtotype1 = new Police_problemtotype();
            problemtotype1.setProblemssid(police_problem.getSsid());
            problemtotype1.setProblemtypessid(updateProblemParam.getProblemtypessid());

            Police_problemtotype problemtotype2 = police_problemtotypeMapper.selectOne(problemtotype1);
            if (null != problemtotype2) {
                result.setMessage("该问题已经存在，是否继续修改？");
                return;
            }
        }


        EntityWrapper ew=new EntityWrapper();
        if (null!=updateProblemParam.getSsid()){
            ew.eq("ssid",updateProblemParam.getSsid());
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
            problemtotype.setProblemssid(updateProblemParam.getSsid());

            //problemtypessidV
            Police_problemtotype problemtotype1 = police_problemtotypeMapper.selectOne(problemtotype);
            if(null != problemtotype1 && null != problemtotype1.getSsid()){
                problemtotype1.setProblemtypessid(updateProblemParam.getProblemtypessid());

                int updateType_bool = police_problemtotypeMapper.updateById(problemtotype1);
                LogUtil.intoLog(this.getClass(),"updateType_bool "+updateType_bool);
            }else{
                //根据原版如果有就修改，没有就新增，新增的如果有了就不添加到关联表
                problemtotype.setProblemtypessid(updateProblemParam.getProblemtypessid());
                problemtotype1 = police_problemtotypeMapper.selectOne(problemtotype);
                if(null == problemtotype1){
                    problemtotype.setCreatetime(new Date());
                    problemtotype.setSsid(OpenUtil.getUUID_32());
                    police_problemtotypeMapper.insert(problemtotype);
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

        if (null==getProblemsByIdParam.getSsid()){
            result.setMessage("参数为空");
            return;
        }

        Police_problem problem = new Police_problem();
        problem.setSsid(getProblemsByIdParam.getSsid());

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

        //问题和当前类型，如果不存在，就可以添加
        EntityWrapper<Police_problem> wrapper = new EntityWrapper<Police_problem>();
        wrapper.eq("problem", addProblemParam.getProblem());
        List<Police_problem> police_problems = police_problemMapper.selectList(wrapper);
        //因为一个问题可以是多个类型，所以批量判断
        for (Police_problem police_problem : police_problems) {
            Police_problemtotype problemtotype1 = new Police_problemtotype();
            problemtotype1.setProblemssid(police_problem.getSsid());
            problemtotype1.setProblemtypessid(addProblemParam.getProblemtypessid());

            Police_problemtotype problemtotype2 = police_problemtotypeMapper.selectOne(problemtotype1);
            if (null != problemtotype2) {
                result.setMessage("该问题已经存在，是否继续添加？");
                return;
            }
        }


        //添加问题
        Police_problem addProblem = new Police_problem();
        addProblem.setProblem(addProblemParam.getProblem());
        addProblem.setReferanswer(addProblemParam.getReferanswer());
        addProblem.setOrdernum(0);
        addProblem.setCreatetime(new Date());
        addProblem.setSsid(OpenUtil.getUUID_32());
        int insert_bool = police_problemMapper.insert(addProblem);
        //添加问题类型
        problemtotype.setProblemssid(addProblem.getSsid());
        problemtotype.setProblemtypessid(addProblemParam.getProblemtypessid());
        problemtotype.setSsid(OpenUtil.getUUID_32());
        problemtotype.setCreatetime(new Date());
        int insert_totype = police_problemtotypeMapper.insert(problemtotype);
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
            result.setMessage("参数不能为空");
            return;
        }

        if (StringUtils.isBlank(addProblemtypeParam.getTypename())){
            result.setMessage("问题类型名称不能为空");
            return;
        }

        if (null == addProblemtypeParam.getOrdernum()){
            result.setMessage("排序不能为空");
            return;
        }

        /**判断一下是否重名**/
        Police_problemtype police_problemtype = new Police_problemtype();
        police_problemtype.setTypename(addProblemtypeParam.getTypename());
        Police_problemtype problemtype = police_problemtypeMapper.selectOne(police_problemtype);

        if (null != problemtype) {
            result.setMessage("问题类型已存在，请重新设置");
            return;
        }

        /**判断排序是否重复**/
        if (addProblemtypeParam.getOrdernum() > 0) {
            Police_problemtype policeProblemtype = new Police_problemtype();
            policeProblemtype.setOrdernum(addProblemtypeParam.getOrdernum());
            Police_problemtype selectOne = police_problemtypeMapper.selectOne(policeProblemtype);
            if (null != selectOne) {
                result.setMessage("排序不能重复，请重新排序");
                return;
            }
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
            result.setMessage("参数不能为空");
            return;
        }

        if (null==updateProblemtypeParam.getSsid()){
            result.setMessage("ssid参数不能为空");
            return;
        }

        if (StringUtils.isBlank(updateProblemtypeParam.getTypename())) {
            result.setMessage("问题类型名称不能为空");
            return;
        }

        if (null==updateProblemtypeParam.getOrdernum()){
            result.setMessage("排序参数不能为空");
            return;
        }

        Police_problemtype problemtype1 = new Police_problemtype();
        problemtype1.setTypename(updateProblemtypeParam.getTypename());
        problemtype1.setOrdernum(updateProblemtypeParam.getOrdernum());
        problemtype1.setSsid(updateProblemtypeParam.getSsid());
        Police_problemtype selectOne1 = police_problemtypeMapper.selectOne(problemtype1);

        if (null == selectOne1) {
            /**判断一下是否重名**/

            EntityWrapper<Police_problemtype> ew = new EntityWrapper<>();
            ew.eq("typename", updateProblemtypeParam.getTypename());
            ew.ne("ssid", updateProblemtypeParam.getSsid());
            List<Police_problemtype> problemtypes = police_problemtypeMapper.selectList(ew);

            if (null != problemtypes && problemtypes.size() > 0) {
                result.setMessage("问题类型已存在，请重新设置");
                return;
            }

            /**判断排序是否重复**/
            if (updateProblemtypeParam.getOrdernum() > 0) {

                EntityWrapper<Police_problemtype> ew2 = new EntityWrapper<>();
                ew2.eq("ordernum", updateProblemtypeParam.getOrdernum());
                ew2.ne("ssid", updateProblemtypeParam.getSsid());
                List<Police_problemtype> policeProblemtypes = police_problemtypeMapper.selectList(ew2);

                if (null != policeProblemtypes && policeProblemtypes.size() > 0) {
                    result.setMessage("排序不能重复，请重新排序");
                    return;
                }
            }
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

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            String format = df.format(new Date());// new Date()为获取当前系统时间

            String filename=template.getTitle()==null?"笔录模板":template.getTitle();
            String path = filePathNew + filename + format + ".doc";

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"), 10240);
            templateDamo.process(dataMap, out);
            out.flush();
            out.close();

            String uploadpath= OpenUtil.strMinusBasePath(PropertiesListenerConfig.getProperty("file.qg"),path);
            String uploadbasepath = NetTool.getMyIP();
            result.setData("http://" + uploadbasepath + uploadpath);

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
            String uploadbasepath=NetTool.getMyIP();
            result.setData("http://" + uploadbasepath + uploadpath);

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

        //取出授权，如果是单机版的，就自动选择谈话
        String problemtypessid = "1";//获取数据库问题类型ssid为1的类型
        SysYmlParam ymlParam = SysYmlCache.getSysYmlParam();
        String gnlist = ymlParam.getGnlist();
        if (gnlist.indexOf("hk_o") != -1) {
            Police_problemtype problemtype = new Police_problemtype();
            problemtype.setTypename("谈话");
            Police_problemtype selectOne = police_problemtypeMapper.selectOne(problemtype);
            problemtypessid = selectOne.getSsid();
        }else {
            List<Police_problemtype> police_problemtypes = police_problemtypeMapper.selectList(null);
            if (police_problemtypes.size() > 0) {
                Police_problemtype police_problemtype = police_problemtypes.get(0);
                problemtypessid = police_problemtype.getSsid();
            }
        }

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
                        //添加问题
                        Police_problem police_problem = new Police_problem();
                        police_problem.setProblem(problem);
                        police_problem.setCreatetime(new Date());
                        police_problem.setSsid(OpenUtil.getUUID_32());
                        police_problem.setOrdernum(0);
                        police_problem.setReferanswer("");
                        Integer insert = police_problemMapper.insert(police_problem);
                        addProblemParam = police_problem;
                        //添加问题类型
//                        if (insert >= 1) {
//                            Police_problemtotype problemtotype = new Police_problemtotype();
//                            problemtotype.setProblemssid(police_problem.getSsid());
//                            problemtotype.setProblemtypessid(problemtypessid);
//                            police_problemtotypeMapper.insert(problemtotype);
//                        }
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

    public void getSQGnlist(RResult result, ReqParam<GetSQGnlistParam> params) {

        AppCacheParam param = AppCache.getAppCacheParam();
        if (null == param.getData() || param.getData().size() == 0) {
            mainService.getNavList(result);
            param = AppCache.getAppCacheParam();
        }

        this.changeResultToSuccess(result);
        result.setData(param);
    }


/***/














}
