package com.belle.wechat.feign.wechat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class WeChatFegin {
	private final static Logger logger = LoggerFactory.getLogger(WeChatFegin.class);

	private static WeChatClient client;

	static {
		WeChatClient client = Feign.builder().encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder())
				.target(WeChatClient.class, "https://api.weixin.qq.com");
		setClient(client);
	}

	public static WeChatClient getClient() {
		return client;
	}

	public static void setClient(WeChatClient client) {
		WeChatFegin.client = client;
	}
}
