package com.avst.trm.v1.report.toupserver;

import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;

/**
 * 请求上级服务器的处理
 */
public class ToUpServerBaseDealClass {


    public static boolean updateUser(){



        System.out.println(PropertiesListenerConfig.getProperty("re.basepath"));



//        String XY=OutsideDataRead.getproperty(OutsideDataRead.server_pro, "XY");
//        if(StringUtils.isEmpty(XY)){
//            System.out.println(XY+"XY,请求分服务器的协议为空，请检查-------------");
//            return false;
//        }
//        String serverip=OutsideDataRead.getproperty(OutsideDataRead.server_pro, "ip");
//        String clientip=OutsideDataRead.getproperty(OutsideDataRead.server_pro, "baseip");
//        if(StringUtils.isEmpty(serverip)){
//            System.out.println(serverip+"serverip,请求分服务器的ip为空，请检查-------------");
//            return false;
//        }
//        String getAllServerStateurl=OutsideDataRead.getproperty(OutsideDataRead.server_pro, "getAllServerStateurl");
//        if(StringUtils.isEmpty(getAllServerStateurl)){
//            System.out.println(getAllServerStateurl+"getAllServerStateurl，请求分服务器的路径为空，请检查-------------");
//            return false;
//        }
//
//        String basepath=OutsideDataRead.getproperty(OutsideDataRead.server_pro, "basepath");
//        if(StringUtils.isEmpty(basepath)){
//            System.out.println(basepath+"basepath，请求分服务器的唯一路劲标示为空，请检查-------------");
//            return false;
//        }
//
//
//        try {
//            String url=XY+serverip+getAllServerStateurl;
//
//            String paramstr="";
//            if(null!=userstate){
//                paramstr="userstate="+userstate;
//            }
//            if(null!=bindingbool){
//                if(StringUtils.isNotEmpty(paramstr)){
//                    paramstr+="&";
//                }
//                paramstr+="bindingbool="+bindingbool;
//            }
//            if(null!=openid){
//                if(StringUtils.isNotEmpty(paramstr)){
//                    paramstr+="&";
//                }
//                paramstr+="openid="+openid;
//            }
//            if(null!=clientip){
//                if(StringUtils.isNotEmpty(paramstr)){
//                    paramstr+="&";
//                }
//                paramstr+="serverip="+clientip;
//            }
//            if(null!=basepath){
//                if(StringUtils.isNotEmpty(paramstr)){
//                    paramstr+="&";
//                }
//                paramstr+="basepath="+basepath;
//            }
//            if(null!=username){
//                if(StringUtils.isNotEmpty(paramstr)){
//                    paramstr+="&";
//                }
//                paramstr+="username="+username+"&updateusername=1";
//            }
//
//
//
//
//
//            String rr=HttpRequest.readContentFromGet(url, paramstr);
//            if(StringUtils.isNotEmpty(rr)){
//
//                RResult result=(RResult)JacksonUtil.stringToObjebt_1(rr, RResult.class);
//                System.out.println(result.getActioncode()+":result.getActioncode()");
//                if(null!=result && result.getActioncode().equals(Code.SUCCESS.toString())){
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



        return false;
    }



}
