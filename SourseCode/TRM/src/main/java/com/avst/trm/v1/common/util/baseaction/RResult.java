package com.avst.trm.v1.common.util.baseaction;

import com.avst.trm.v1.common.util.DateUtil;

/**
 * 返回的参数集合
 * @author wb
 *
 */
public class RResult<T> {

	
	private String version ;
	
	private String actioncode;
	
	private T data;
	
	private String endtime;
	
	private String message;

	/**
	 * 下个页面的id，可能不变，与传过来的pageid一样
	 */
	private String nextpageid;

	public String getNextpageid() {
		return nextpageid;
	}

	public void setNextpageid(String nextpageid) {
		this.nextpageid = nextpageid;
	}

	public RResult(){
		endtime= DateUtil.getDateAndMinute();
		message="请求失败";
		actioncode=Code.FAIL.toString();
	}

	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getActioncode() {
		return actioncode;
	}

	public void setActioncode(String actioncode) {
		this.actioncode = actioncode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



	
}
