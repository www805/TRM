package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.cache.param.CheckSQParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_page;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ActionAndinterfaceAndPage;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_actionMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.avst.trm.v1.common.util.sq.SQEntity;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.ActionVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.PageVO;
import com.avst.trm.v1.web.sweb.vo.AdminManage_session;
import com.avst.trm.v1.web.sweb.vo.InitVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import sun.swing.StringUIClientPropertyKey;

import java.util.*;

/**
 * 一些常用的公共的缓存
 */
public class CommonCache {

    public static void main(String[] args) {
        String gns="12|34|65";
        System.out.println(gns.indexOf("|"));

    }

    public static long sysStartTime;//服务器每次的启动时间

    private static String companyname;//公司名称

    private static String companymsg;//公司简介



    /**
     * 本地授权信息授权
     */
    public static SQEntity getSQEntity=null;

    /**
     * 授权客户端的功能列表
     * 客户端的功能列表，暂时只有：record_f、asr_f、tts_f、fd_f、ph_f(笔录管理、语音识别、语音播报、设备控制、测谎仪)
     * 单机版/联机版（s_v、o_v）
     * 分支版本：公安、纪委、监察委（ga_t、jw_t、jcw_t）
     * OEM版本：通用、HK（common_o、hk_o）
     * 客户端版/服务器端版（c_e,s_e）
     * |cname=avst,cmsg:|
     * @return
     */
    public static List<String> gnlist(){
        if(null==getSQEntity){
            getSQEntity=AnalysisSQ.getSQEntity();
        }
        if(null!=getSQEntity){
            String gns=getSQEntity.getGnlist();
            if(StringUtils.isNotEmpty(gns)){
                List<String> list=new ArrayList<String>();
                if(gns.indexOf("|") > -1){
                    String[] arr=gns.split("\\|");
                    for(String s:arr){
                        if(s.indexOf("{comp") != -1){
                            s = s.replace("{", "");
                            s = s.replace("}", "");
                            String[] split = s.split(",");
                            for (String sp : split) {
                                String[] sps = sp.split(":");
                                if (sps.length > 0) {
                                    if("companyname".equals(sps[0])){
                                        companyname = sps[1];
                                    }else if("companymsg".equals(sps[0])){
                                        companymsg = sps[1];
                                    }
                                }
                            }

                        }else{
                            list.add(s.trim());
                        }
                    }
                }else {
                    list.add(gns);
                }

                String gnlists = String.join("|", list);
                getSQEntity.setGnlist(gnlists);
                return list;
            }
        }
        return null;
    }

    public static String getCompanyname() {
        if(StringUtils.isBlank(companyname)){
            gnlist();
        }
        return companyname;
    }

    public static void setCompanyname(String companyname) {
        CommonCache.companyname = companyname;
    }

    public static String getCompanymsg() {
        if(StringUtils.isBlank(companymsg)){
            gnlist();
        }
        return companymsg;
    }

    public static void setCompanymsg(String companymsg) {
        CommonCache.companymsg = companymsg;
    }

    /**
     * 客户端访问时使用的key
     */
    private static String clientkey=null;

    /**
     * 授权的code值
     */
    private static String serverSQCode=null;

    public static String getServerSQCode(){
        if(!clientSQbool){

            //查的时候如果不对再请求一遍授权check

            return null;
        }
        if(StringUtils.isEmpty(serverSQCode)){
            serverSQCode = AnalysisSQ.getServerSQCode();
        }
        return serverSQCode;
    }

    /**
     * 授权是否正常
     * 定时器里面要每天要进行检测
     */
    public static boolean clientSQbool=true;

     public static String getClientKey(){
         if(!clientSQbool){
             return null;
         }
         if(StringUtils.isEmpty(clientkey)){
             clientkey = AnalysisSQ.getClientKey();
         }
         return clientkey;
     }


    /**
     * client客户端base请求地址
     */
    private static String clientbaseurl;

    public static String getClientBaseurl(){
        if(StringUtils.isEmpty(clientbaseurl)){

            initBase_Serverconfig();
        }
        return clientbaseurl;
    }

    /**
     * web客户端base请求地址
     */
    private static String webbaseurl;

    public static String getWebBaseurl(){
        if(StringUtils.isEmpty(webbaseurl)){

            initBase_Serverconfig();
        }
        return webbaseurl;
    }


    /**
     * 当前服务类型
     */
    private static String currentServerType;

    public static String getCurrentServerType(){


        if(StringUtils.isEmpty(currentServerType)){
            initBase_Serverconfig();
        }

        return currentServerType;
    }

    /**
     * 当前服务类型
     */
    private static String currentWebType;

    public static String getCurrentWebType(){


        if(StringUtils.isEmpty(currentWebType)){
            initBase_Serverconfig();
        }

        return currentWebType;
    }


    /**
     * 服务器系统配置缓存
     */
    private static Base_serverconfig serverconfig=null;

    public static Base_serverconfig getServerconfig(){

        if(null==serverconfig){
            initBase_Serverconfig();
        }
        return serverconfig;
    }

    private static void initBase_Serverconfig(){
        Base_serverconfigMapper base_serverconfigMapper= SpringUtil.getBean(Base_serverconfigMapper.class);

        Base_serverconfig base_serverconfig= base_serverconfigMapper.selectById(1);
        serverconfig=base_serverconfig;
        String serverip=serverconfig.getServerip();
        String serverport=serverconfig.getServerport();
        if(StringUtils.isNotEmpty(serverip)&&StringUtils.isNotEmpty(serverport)){
            clientbaseurl = PropertiesListenerConfig.getProperty("pro.XY")+"://"
                    +serverip+":"+serverport+ PropertiesListenerConfig.getProperty("pro.baseurl_client");
            webbaseurl = PropertiesListenerConfig.getProperty("pro.XY")+"://"
//                    +serverip+":"+serverport+ PropertiesListenerConfig.getProperty("pro.baseurl_web");
                    +serverip+":"+serverport;//暂时不用 baseurl_web 这一级
        }
        String type=serverconfig.getType();
        if(StringUtils.isNotEmpty(type)){
            currentServerType=type+"_client";
            currentWebType=type+"_web";
        }
    }




    public static void setCurrentServerType(String serverType){
        if(StringUtils.isEmpty(serverType)){
            return ;
        }
        currentServerType=serverType;
    }


    /**
     * 缓存所有的动作，按分类来做
     */
    private static Map<String, List<ActionAndinterfaceAndPage>> actionListMap;


    /**
     *
     */
    private static Map<String,List<Base_page>> pageListMap;


    /**
     * 页面动作初始化(后台服务器端)
     */
    private static InitVO init_WEB;

    /**
     * 获取web客户端页面动作初始化数据
     * @return
     */
    public static synchronized InitVO getinit_WEB(){

        if(null==init_WEB){

            if(null==pageListMap||null==actionListMap){
                initActionListMap();
            }
            InitVO init_web=new InitVO();
            CheckSQParam checkSQParam=checkSQ();
            init_web.setCode(checkSQParam.getCode());
            init_web.setMsg(checkSQParam.getMsg());
            if(!checkSQParam.isCheckbool()){
                return null;
            }
            init_web.setBaseUrl(getWebBaseurl());
            init_web.setServiceType(getCurrentWebType());
            List<PageVO> pageList=new ArrayList<PageVO>();
            List<Base_page> pagelist=getPageList(getCurrentWebType());
            if(null!=pagelist&&pagelist.size() > 0){//循环添加该客户端的页面
                for(Base_page page:pagelist){
                    PageVO pageVO=new PageVO();
                    pageVO.setPageid(page.getPageid());

                    List<ActionAndinterfaceAndPage> actionandpagelist= getActionListByPageid(page.getPageid(),getCurrentWebType());
                    if(null!=actionandpagelist&&actionandpagelist.size() > 0){//把该页面的动作填入
                        List<ActionVO> actionList = new ArrayList<ActionVO>();
                        for(ActionAndinterfaceAndPage ap:actionandpagelist){
                            ActionVO actionVO=new ActionVO();
                            actionVO.setReqURL(ap.getInterfaceurl());
                            actionVO.setNextPageId(ap.getNextpageid());
                            actionVO.setGotopageOrRefresh(ap.getTopagebool());
                            actionVO.setActionId(ap.getActionid());
                            actionList.add(actionVO);
                        }
                        pageVO.setActionList(actionList);
                    }

                    if(null!=page.getFirstpage()&&page.getFirstpage()==1){//是否首页显示
                        init_web.setFirstpageid(page.getPageid());
                    }
                    pageList.add(pageVO);
                }
                init_web.setPageList(pageList);
                init_WEB=init_web;
            }else{
                init_web.setMsg("没有找到任务一个页面，请联系管理员");
                init_web.setCode(CodeForSQ.ERROR100002);
                return init_web;
            }
        }

        return init_WEB;
    }


    /**
     * 页面动作初始化客户端
     */
    private static com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO init_CLIENT;

    public static boolean initbool=false;//是否是初始化

    /**
     * 强制初始化客户端页面
     * @param bool
     * @return
     */
    public static synchronized com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO getinit_CLIENT(boolean bool){
        if(bool){
            init_CLIENT=null;
            actionListMap=null;
            initbool=true;//本次属于初始化
            LogUtil.intoLog(1,CommonCache.class,"重置客户端页面动作");
        }
        return getinit_CLIENT();
    }

    /**
     * 获取第三方客户端页面动作初始化数据
     * @return
     */
    public static synchronized com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO getinit_CLIENT(){

        if(null==init_CLIENT){
            if(null==pageListMap||null==actionListMap){
                initActionListMap();
            }
            com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO initvo=
                    new com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO();
            CheckSQParam checkSQParam=checkSQ();
            initvo.setMsg(checkSQParam.getMsg());
            initvo.setCode(checkSQParam.getCode());
            if(!checkSQParam.isCheckbool()){
                return initvo;
            }
            initvo.setKey(getClientKey());
            initvo.setBaseUrl(getClientBaseurl());
            initvo.setServiceType(getCurrentServerType());
            List<PageVO> pageList=new ArrayList<PageVO>();
            List<Base_page> pagelist=getPageList(getCurrentServerType());
            if(null!=pagelist&&pagelist.size() > 0){//循环添加该客户端的页面
                for(Base_page page:pagelist){
                    PageVO pageVO=new PageVO();
                    pageVO.setPageid(page.getPageid());

                    List<ActionAndinterfaceAndPage> actionandpagelist= getActionListByPageid(page.getPageid(),getCurrentServerType());
                    if(null!=actionandpagelist&&actionandpagelist.size() > 0){//把该页面的动作填入
                        List<ActionVO> actionList = new ArrayList<ActionVO>();
                        for(ActionAndinterfaceAndPage ap:actionandpagelist){
                            ActionVO actionVO=new ActionVO();
                            actionVO.setReqURL(ap.getInterfaceurl());
                            actionVO.setNextPageId(ap.getNextpageid());
                            actionVO.setGotopageOrRefresh(ap.getTopagebool());
                            actionVO.setActionId(ap.getActionid());
                            if(null!=ap.getFirstaction()&&ap.getFirstaction()==1){
                                initvo.setFirstactionid(ap.getActionid());
                                initvo.setFirstinterface(ap.getInterfaceurl());
                            }
                            actionList.add(actionVO);
                        }
                        pageVO.setActionList(actionList);
                    }

                    if(null!=page.getFirstpage()&&page.getFirstpage()==1){//是否首页显示
                        initvo.setFirstpageid(page.getPageid());
                    }

                    pageList.add(pageVO);
                }
                initvo.setPageList(pageList);
                init_CLIENT=initvo;
            }else{
                initvo.setCode(CodeForSQ.ERROR100002);
                initvo.setMsg("该类型页面未找到，请联系管理员");
                return initvo;
            }
        }

        return init_CLIENT;
    }

    public static CheckSQParam checkSQ(){

        CheckSQParam checkSQParam=new CheckSQParam();
        //判断是否生成隐性的ini文件
        Base_serverconfigMapper base_serverconfigMapper = SpringUtil.getBean(Base_serverconfigMapper.class);
        Base_serverconfig serverconfig=base_serverconfigMapper.selectById(1);
        String serverip=serverconfig.getServerip();
        String serverport=serverconfig.getServerport();
        if(StringUtils.isEmpty(serverip)||StringUtils.isEmpty(serverport)){
            checkSQParam.setCode(CodeForSQ.ERROR100002);
            checkSQParam.setMsg("服务器配置访问IP/端口异常");
            return checkSQParam;
        }
        Integer authorizebool=serverconfig.getAuthorizebool();
        if(null==authorizebool||authorizebool!=1){//还没有生成隐性授权文件
            boolean bool=AnalysisSQ.createClientini(serverconfig);
            if(!bool){
                checkSQParam.setCode(CodeForSQ.ERROR100002);
                checkSQParam.setMsg("服务器授权异常");
                return checkSQParam;
            }
            LogUtil.intoLog(CommonCache.class,"initClient authorizebool:"+bool);
        }

        int bool=AnalysisSQ.checkUseTime();
        if(bool > -1){
            checkSQParam.setCheckbool(true);
            checkSQParam.setCode(CodeForSQ.TRUE);
            checkSQParam.setMsg("使用正常");
            return checkSQParam;
        }else{
            if(bool == -100001){
                checkSQParam.setCode(CodeForSQ.ERROR100001);

                //重新授权
                AnalysisSQ.createClientini(serverconfig);

            }else if(bool == -100002){
                checkSQParam.setCode(CodeForSQ.ERROR100002);
            }else{
                checkSQParam.setCode(CodeForSQ.ERROR100003);
            }
            checkSQParam.setMsg("使用异常");
            return checkSQParam;
        }
    }


    /**
     * 获取 那一类型的所有动作
     * @return
     */
    public static synchronized List<Base_page> getPageList(String typename){

        if(null==pageListMap){
            initActionListMap();
        }
        if(null!=pageListMap&&pageListMap.containsKey(typename)){
            return pageListMap.get(typename);
        }
        return null;
    }


    /**
     * 获取 那一类型的所有动作
     * @return
     */
    public static synchronized List<ActionAndinterfaceAndPage> getActionList(){

        String typename=getCurrentServerType();
        if(null==actionListMap){
            initActionListMap();
        }

        if(null!=actionListMap&&actionListMap.containsKey(typename)){
            return actionListMap.get(typename);
        }
        return null;
    }

    /**
     * 获取 那一类型的所有动作
     * @return
     */
    public static synchronized List<ActionAndinterfaceAndPage> getActionListByPageid(String pageid,String type){

        if(null==actionListMap){
            initActionListMap();
        }

        if(null!=actionListMap&&actionListMap.containsKey(type)){
            List<ActionAndinterfaceAndPage> andPageList=new ArrayList<ActionAndinterfaceAndPage>();

            List<ActionAndinterfaceAndPage> pagelist=actionListMap.get(type);
            for(ActionAndinterfaceAndPage action:pagelist){
                if(action.getPageid().equals(pageid)){
                    if(andPageList.size() > 0){
                        boolean bool= false;
                        for(ActionAndinterfaceAndPage action2 : andPageList){
                            if(action2.getActionid().equals(action.getActionid())){
                                bool=true;
                                break;
                            }
                        }
                        if(!bool){
                            andPageList.add(action);
                        }
                    }else{
                        andPageList.add(action);
                    }
                }
            }
            return andPageList;
        }
        return null;
    }



    public static synchronized boolean initActionListMap(){
        Base_actionMapper base_actionMapper = SpringUtil.getBean(Base_actionMapper.class);

        EntityWrapper ew=new EntityWrapper();
        List<ActionAndinterfaceAndPage> list=base_actionMapper.getActionAndinterfaceAndPage(ew);
        if(null!=list&&list.size() > 0){

            actionListMap=new HashMap<String, List<ActionAndinterfaceAndPage>>();

            for(ActionAndinterfaceAndPage action : list){

                String type=action.getType();
                addPageToList(action);

                List<ActionAndinterfaceAndPage> actionlist;
                if(!actionListMap.containsKey(type)){
                    actionlist=new ArrayList<ActionAndinterfaceAndPage>();
                }else{
                    actionlist=actionListMap.get(type);
                }
                if(null==actionlist){
                    actionlist=new ArrayList<ActionAndinterfaceAndPage>();
                }
                if(actionlist.size() > 0){
                    boolean bool=false;
                    for(ActionAndinterfaceAndPage action2:actionlist){
                        if(action2.getActionid().equals(action.getActionid())){
                            bool=true;
                            break;
                        }
                    }
                    if(!bool){
                        actionlist.add(action);//
                    }
                }else{
                    actionlist.add(action);//
                }
                actionListMap.put(type,actionlist);
            }
            return true;
        }
        return false;
    }


    private static void addPageToList(ActionAndinterfaceAndPage action){

        if(null==pageListMap){
            pageListMap=new HashMap<String,List<Base_page>>();
        }

        String type=action.getType();
        List<Base_page> pageList;
        if(!pageListMap.containsKey(type)){
            pageList=new ArrayList<Base_page>();
        }else{
            pageList=pageListMap.get(type);
        }
        boolean bool=true;
        if(null!=pageList&&pageList.size() > 0){
            for(Base_page page:pageList){
                try {
                    Integer pageid=page.getId();
                        if((null!=pageid&&null!=action.getPage_id_c()&&pageid.intValue()==action.getPage_id_c().intValue())
                                ||(null!=page.getPageid()&&page.getPageid().equals(action.getPageid()))){//这里的判断要注意
                            bool=false;
                            break;
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            pageList=new ArrayList<Base_page>();
        }

        if(bool){
            Base_page newpage=new Base_page();
            newpage.setFirstpage(action.getFirstpage());
            newpage.setId(action.getPage_id_c());
            newpage.setPageid(action.getPageid());
            newpage.setTypessid(action.getTypessid());

            pageList.add(newpage);
            pageListMap.put(type,pageList);
        }

    }


    /**
     * 用户在session中的缓存(客户端还是服务端都需要缓存这个用户，永远只有一个用户，登录之后就改过来)
     */
    private static  AdminManage_session adminManage_session;

    public static AdminManage_session getAdminManage_session(){


        return adminManage_session;
    }

    public static void setAdminManage_session(AdminManage_session admin){

        adminManage_session=admin;

    }

    /**
     * 心跳提供的账号缓存
     */
    private static Base_admininfo admininfo;

    public static Base_admininfo getAdmininfo(){
        return admininfo;
    }

    public static void setAdmininfo(Base_admininfo base_admininfo){
        admininfo = base_admininfo;
    }


    /**
     * 点播打包线程是否有人占用
     */
    public static int gzvodthreadnum=0;

}
