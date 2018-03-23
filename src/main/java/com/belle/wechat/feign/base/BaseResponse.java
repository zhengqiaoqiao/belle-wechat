package com.belle.wechat.feign.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <p>Title: BaseResponse</p>
 * <p>Description: </p>
 *
 * @author: zheng.qq
 * @date: 2016年7月21日
 */
public class BaseResponse implements Serializable {
	private static final long serialVersionUID = -5810311816472658758L;

	/**
	 * 错误编号
	 */
	@JsonProperty("errcode")
	private int errCode;

	/**
	 * 错误信息
	 */
	@JsonProperty("errmsg")
	private String errMsg;

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
