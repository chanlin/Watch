package com.jajale.watch.entityno;

/**
 * 请求返回的基准对象
 * 接口标准
 *
 */
public class BaseResult {

	private String code;
//	private String stime;
	private String message;
	private String result;
	private String other ;
	private String apver ;
	private String ret ;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getApver() {
		return apver;
	}

	public void setApver(String apver) {
		this.apver = apver;
	}

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}
}
