package com.avst.trm.v1.common.util.baseaction;

/**
 * 通用的返回结果加说明，code值自己判断
 * @author wb
 *
 */
public class RRParam<T> {

	/**
	 * 返回结果的说明
	 */
	private String message;
	/**
	 * 请求的code值
	 */
	private int code;

	/**
	 * 如果需要返回复杂的结果集用这个泛型
	 */
	private T t;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
}
