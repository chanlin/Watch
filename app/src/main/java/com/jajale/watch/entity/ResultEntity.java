package com.jajale.watch.entity;

/**
 * data返回实体类
 *
 */
public class ResultEntity {


	private String user_id;//用户名ID
	private int result;
	private int watch_bind;//是否绑定手表
	private int is_manage;//是否为管理员

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getWatch_bind() {
		return watch_bind;
	}

	public void setWatch_bind(int watch_bind) {
		this.watch_bind = watch_bind;
	}

	public int getIs_manage() {
		return is_manage;
	}

	public void setIs_manage(int is_manage) {
		this.is_manage = is_manage;
	}
	public boolean  isSuccess()
	{
		return getResult()==1;
	}
}
