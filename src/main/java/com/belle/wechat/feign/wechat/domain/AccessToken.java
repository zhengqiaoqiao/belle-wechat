package com.belle.wechat.feign.wechat.domain;

import java.util.Date;

import com.belle.wechat.feign.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Title: AccessTokenResponse</p>
 * <p>Description: </p>
 *
 * @author: zheng.qq
 * @date: 2016年7月21日
 */
public class AccessToken extends BaseResponse {
	private static final long serialVersionUID = 1L;
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("openid")
	private String openId;
	@JsonProperty("scope")
	private String scope;
	@JsonProperty("expires_in")
	private int expiresIn;
	
	private Date createTime;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
