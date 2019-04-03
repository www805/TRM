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
