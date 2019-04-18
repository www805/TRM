package com.avst.trm.v1.common.cache;

import com.avst.trm.v1.common.datasourse.base.entity.Base_action;
import com.avst.trm.v1.common.datasourse.base.entity.Base_page;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ActionAndinterfaceAndPage;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_actionMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.baseaction.CodeForSQ;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.AnalysisSQ;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.ActionVO;
import com.avst.trm.v1.outsideinterface.offerclientinterface.param.PageVO;
import com.avst.trm.v1.web.vo.InitVO;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一些常用的公共的缓存
 */
public class CommonCache {

    /**
     * 客户端访问时使用的key
     */
    private static String clientkey=null;
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

     private static String clientbaseurl;

    public static String getClientBaseurl(){
        if(StringUtils.isEmpty(clientbaseurl)){

            initBase_Serverconfig();
        }
        return clientbaseurl;
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

        return currentServerType;
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
            clientbaseurl = "http://"+serverip+":"+serverport+ PropertiesListenerConfig.getProperty("pro.baseurl");
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
     * 页面动作初始化
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
            String msg="";
            String code="";
            if(!checkSQ(msg,code)){
                return null;
            }
            InitVO init_web=new InitVO();
            init_web.setBaseUrl(getClientBaseurl());
            init_web.setServiceType(getCurrentWebType());
            List<PageVO> pageList=new ArrayList<PageVO>();
            List<Base_page> pagelist=getPageList(getCurrentWebType());
            if(null!=pagelist&&pagelist.size() > 0){//循环添加该客户端的页面
                for(Base_page page:pagelist){
                    PageVO pageVO=new PageVO();
                    pageVO.setPageid(page.getPageid());

                    List<ActionAndinterfaceAndPage> actionandpagelist= getActionListByPageid(page.getPageid());
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
            }
            init_WEB=init_web;
        }

        return init_WEB;
    }


    /**
     * 页面动作初始化第三方客户端
     */
    private static com.avst.trm.v1.outsideinterface.offerclientinterface.param.InitVO init_CLIENT;

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
            String msg="";
            String code="";
            if(!checkSQ(msg,code)){
                initvo.setMsg(msg);
                initvo.setCode(code);
                return null;
            }
            initvo.setMsg(msg);
            initvo.setCode(code);;
            initvo.setKey(getClientKey());
            initvo.setBaseUrl(getClientBaseurl());
            initvo.setServiceType(getCurrentServerType());
            List<PageVO> pageList=new ArrayList<PageVO>();
            List<Base_page> pagelist=getPageList(getCurrentServerType());
            if(null!=pagelist&&pagelist.size() > 0){//循环添加该客户端的页面
                for(Base_page page:pagelist){
                    PageVO pageVO=new PageVO();
                    pageVO.setPageid(page.getPageid());

                    List<ActionAndinterfaceAndPage> actionandpagelist= getActionListByPageid(page.getPageid());
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
                        initvo.setFirstpageid(page.getPageid());
                    }

                    pageList.add(pageVO);
                }
                initvo.setPageList(pageList);
            }
            init_CLIENT=initvo;
        }

        return init_CLIENT;
    }

    private static boolean checkSQ(String msg,String code){
        //判断是否生成隐性的ini文件
        Base_serverconfigMapper base_serverconfigMapper = SpringUtil.getBean(Base_serverconfigMapper.class);
        Base_serverconfig serverconfig=base_serverconfigMapper.selectById(1);
        String serverip=serverconfig.getServerip();
        String serverport=serverconfig.getServerport();
        if(StringUtils.isEmpty(serverip)||StringUtils.isEmpty(serverport)){
            System.out.println("服务器配置访问IP/端口异常");
            return false;
        }
        int authorizebool=serverconfig.getAuthorizebool();
        if(authorizebool!=1){//还没有生成隐性授权文件
            boolean bool=AnalysisSQ.createClientini(base_serverconfigMapper,serverconfig);
            System.out.println("initClient authorizebool:"+bool);
        }

        int bool=AnalysisSQ.checkUseTime();
        if(bool > -1){
            code=CodeForSQ.TRUE;
            msg="使用正常";
            return true;
        }else{
            if(bool == -100001){
                code=CodeForSQ.ERROR100001;
            }else if(bool == -100002){
                code=CodeForSQ.ERROR100002;
            }else{
                code=CodeForSQ.ERROR100003;
            }
            msg="使用异常";
            return false;
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
    public static synchronized List<ActionAndinterfaceAndPage> getActionListByPageid(String pageid){

        if(null==actionListMap){
            initActionListMap();
        }

        String typename=getCurrentServerType();
        if(null!=actionListMap&&actionListMap.containsKey(typename)){
            List<ActionAndinterfaceAndPage> andPageList=new ArrayList<ActionAndinterfaceAndPage>();

            for(ActionAndinterfaceAndPage action:actionListMap.get(typename)){
                if(action.getPageid().equals(pageid)){
                    andPageList.add(action);
                }
            }
            return andPageList;
        }
        return null;
    }



    public static synchronized boolean initActionListMap(){
        Base_actionMapper base_actionMapper = SpringUtil.getBean(Base_actionMapper.class);

        List<ActionAndinterfaceAndPage> list=base_actionMapper.getActionAndinterfaceAndPage(null);
        if(null!=list&&list.size() > 0){

            actionListMap=new HashMap<String, List<ActionAndinterfaceAndPage>>();

            for(ActionAndinterfaceAndPage action : list){

                String typename=action.getTypename();
                addPageToList(action);

                List<ActionAndinterfaceAndPage> actionlist;
                if(actionListMap.containsKey(typename)){
                    actionlist=new ArrayList<ActionAndinterfaceAndPage>();
                }else{
                    actionlist=actionListMap.get(typename);
                }
                actionlist.add(action);//
                actionListMap.put(typename,actionlist);
            }
            return true;
        }
        return false;
    }


    private static void addPageToList(ActionAndinterfaceAndPage action){

        if(null==pageListMap){
            pageListMap=new HashMap<String,List<Base_page>>();
        }

        String typename=action.getTypename();
        List<Base_page> pageList;
        if(pageListMap.containsKey(typename)){
            pageList=new ArrayList<Base_page>();
        }else{
            pageList=pageListMap.get(typename);
        }
        boolean bool=true;
        if(null!=pageList&&pageList.size() > 0){
            for(Base_page page:pageList){
                if(page.getId().intValue()==action.getPage_id_c()){
                    bool=false;
                    break;
                }
            }
        }

        if(bool){
            Base_page newpage=new Base_page();
            newpage.setFirstpage(action.getFirstpage());
            newpage.setId(action.getPage_id_c());
            newpage.setPageid(action.getPageid());
            newpage.setTypeid(action.getTypeid());
            pageList.add(newpage);
            pageListMap.put(typename,pageList);
        }




    }



    /**
     * 获取缓存中对应的动作对象
     * @param url
     * @return
     */
    public static Base_action getBase_action(String url,String type){


        return null;
    }

}
