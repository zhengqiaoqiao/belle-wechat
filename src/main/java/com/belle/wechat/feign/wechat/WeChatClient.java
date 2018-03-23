package com.belle.wechat.feign.wechat;

import com.belle.wechat.feign.wechat.domain.AccessToken;

import feign.Param;
import feign.RequestLine;

/**
 * <p>Title: Snippet</p>
 * <p>Description: </p>
 *
 * @author: zheng.qq
 * @date: 2016年7月25日
 */
public interface WeChatClient {
	/**
	 * 获取微信基础的授权用的AccessToken
	 *
	 * @param appId
	 * @param secret
	 * @return
	 */
	@RequestLine("GET /cgi-bin/token?grant_type=client_credential&appid={appId}&secret={secret}")
	AccessToken getAccessToken(@Param("appId") String appId, @Param("secret") String secret);

}

