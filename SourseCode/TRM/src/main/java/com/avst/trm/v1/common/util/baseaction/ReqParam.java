package com.avst.trm.v1.common.util.baseaction;

import com.avst.trm.v1.common.util.DateUtil;

/**
 * 请求的参数集合
 * @author wb
 *
 */
public class ReqParam<T> {


	private String version ;

	/**
	 * 请求的参数
	 */
	private T param;

	/**
	 * 请求时间
	 */
	private String reqtime;

	private String message;

	/**
	 * 请求的当前页面
	 */
	private String pageid;

	/**
	 * 请求的当前动作
	 */
	private  String actionid;

	/**
	 * 请求的加密码
	 */
	private String token;



	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public T getParam() {
		return param;
	}

	public void setParam(T param) {
		this.param = param;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getReqtime() {
		return reqtime;
	}

	public void setReqtime(String reqtime) {
		this.reqtime = reqtime;
	}

	public String getPageid() {
		return pageid;
	}

	public void setPageid(String pageid) {
		this.pageid = pageid;
	}

	public String getActionid() {
		return actionid;
	}

	public void setActionid(String actionid) {
		this.actionid = actionid;
	}
}
