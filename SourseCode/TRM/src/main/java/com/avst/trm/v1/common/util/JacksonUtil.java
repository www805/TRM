package com.avst.trm.v1.common.util;

import java.util.List;
import java.util.Map;

import com.avst.trm.v1.common.util.log.LogUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
	
	
	public static String objebtToString(Object object){
		
		ObjectMapper mapper = new ObjectMapper();
		 try {
				String json = mapper.writeValueAsString(object);  
				LogUtil.intoLog(JacksonUtil.class,json);
				return json;
			} catch (Exception e) {
				e.printStackTrace();
			}
		 return null;
	}
	
	/**
	 * 一层结构的对象 User
	 * @param json
	 * @param cclass
	 * 公用的
	 * 用了第一层的转换，第二层就可以强转了
	 * @return
	 */
	public static Object stringToObjebt_1(String json,Class cclass ){
		
		if(null==json||json.trim().equals("")){
			return null;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		 try {
			 Object obj = mapper.readValue(json, cclass);  
				return obj;
			} catch (Exception e) {
				LogUtil.intoLog(JacksonUtil.class,"stringToObjebt_1转换出错json："+json);
			}
		 return null;
	}


	/**
	 * 专门把string直接转成list
	 * @param json
	 * 公用的
	 * 用了第一层的转换，第二层就可以强转了
	 * @return
	 */
	public static List stringToObjebt_2(String json,Object t ){

		if(null==json||json.trim().equals("")){
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();

		try {
			List<Object> lendReco = mapper.readValue(json,new TypeReference<List<Object>>() { });
			return lendReco;
		} catch (Exception e) {
			LogUtil.intoLog(JacksonUtil.class,"stringToObjebt_1转换出错json："+json);
		}
		return null;
	}
	
	/**
	 * 解析成map
	 * @param json
	 * @param cclass
	 * @return
	 */
	public static Map<String, Object> stringToObjebt_3(String json,Class cclass ){
		
		if(null==json||json.trim().equals("")){
			return null;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		 try {
			 Map<String, Object> map = mapper.readValue(json, Map.class);  
				return map;
			} catch (Exception e) {
				LogUtil.intoLog(JacksonUtil.class,"stringToObjebt_1转换出错json："+json);
			}
		 return null;
	}
	
	
	/**
	 * 2层结构的对象 ReqSets
	 * 转一层后其中的对象变成map的可以转第二层的object为指定的class对象
	 * @param obj
	 * @param cclass
	 *  
	 * @return
	 */
	public static Object stringToObjebt_2(Object obj,Class cclass ){
		
		ObjectMapper mapper = new ObjectMapper();
		 try {
			 Object obj2=mapper.readValue(objebtToString(obj), cclass); 
				return obj2;
			} catch (Exception e) {
				LogUtil.intoLog(JacksonUtil.class,"stringToObjebt_2转换出错json："+obj);
			}
		 return null;
	}
	
	
	public static JavaType  getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {   
		ObjectMapper mapper = new ObjectMapper();
         return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
     } 
	
	

	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
	  String json="[{\"clientip\":\"http://linuxidc/hls_play.servlet\",\"clientcode\":\"705c2a3c8d4541f295911b4ff0b1865a_bj2_78782c1a0dfe4541b991c2b500c2f3d3\",\"pushname\":\"bj2\",\"starttime\":\"2017-02-28 10:52:13\",\"updatetime\":\"2017-02-28 11:52:24\",\"uuid\":\"78782c1a0dfe4541b991c2b500c2f3d3\"}]";
	  
	}
	
}
