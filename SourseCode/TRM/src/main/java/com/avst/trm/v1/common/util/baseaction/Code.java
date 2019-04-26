package com.avst.trm.v1.common.util.baseaction;

/**
 * 通用返回json对象的状态判断值
 */
public enum Code {

	//请求的返回判断值
	/**
	 * 请求成功
	 */
	SUCCESS,
	/**
	 * 请求失败
	 */
	FAIL,
	
	/**
	 * 登录失败
	 */
	LOGINFAIL,

	/**
	 * 请求成功，但是没有数据
	 */
	SUCCESS_NOTHINGTODO
}
