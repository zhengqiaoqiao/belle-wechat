package com.belle.wechat.framework.scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.belle.wechat.feign.wechat.WeChatFegin;
import com.belle.wechat.feign.wechat.domain.AccessToken;
import com.belle.wechat.feign.wechat.domain.WechatApp;
/*access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token。开发者需要进行妥善保存。access_token的存储至少要保留512个字符空间。access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。

公众平台的API调用所需的access_token的使用及生成方式说明：

1、建议公众号开发者使用中控服务器统一获取和刷新Access_token，其他业务逻辑服务器所使用的access_token均来自于该中控服务器，不应该各自去刷新，否则容易造成冲突，导致access_token覆盖而影响业务；

2、目前Access_token的有效期通过返回的expire_in来传达，目前是7200秒之内的值。中控服务器需要根据这个有效时间提前去刷新新access_token。在刷新过程中，中控服务器可对外继续输出的老access_token，此时公众平台后台会保证在5分钟内，新老access_token都可用，这保证了第三方业务的平滑过渡；

3、Access_token的有效时间可能会在未来有调整，所以中控服务器不仅需要内部定时主动刷新，还需要提供被动刷新access_token的接口，这样便于业务服务器在API调用获知access_token已超时的情况下，可以触发access_token的刷新流程。

公众号可以使用AppID和AppSecret调用本接口来获取access_token。AppID和AppSecret可在“微信公众平台-开发-基本配置”页中获得（需要已经成为开发者，且帐号没有异常状态）。调用接口时，请登录“微信公众平台-开发-基本配置”提前将服务器IP地址添加到IP白名单中，点击查看设置方法，否则将无法调用成功。*/
@Component
public class ScheduledTasks {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	@Resource
	private ThreadPoolTaskExecutor taskExecutor;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	private static String WECHAT_TOKEN_PREFIX = "wechat:token:";
	
	private static List<WechatApp> appList = new ArrayList<WechatApp>();
	static{
		WechatApp app = new WechatApp();
		app.setAppName("思加图小程序");
		app.setAppId("wxa0a82d1cf039d6f4");
		app.setAppSecret("3fe1edf58bbc29e91a8c8869b83b067d");
		appList.add(app);
	}
	
	private void refreshToken(WechatApp app){
		try{
			log.info("开始获取access_token！app_name:{}, app_id:{}, app_secret:{}", app.getAppName(), app.getAppId(), app.getAppSecret());
			AccessToken token = WeChatFegin.getClient().getAccessToken(app.getAppId(), app.getAppSecret());
			if(token.getErrCode() != 0){
				log.error("获取access_token失败！app_name:{}, app_id:{}, app_secret:{}", app.getAppName(), app.getAppId(), app.getAppSecret());
			}else{
				String key = WECHAT_TOKEN_PREFIX + app.getAppId();
				token.setCreateTime(new Date());
				AccessToken oldToken = (AccessToken) redisTemplate.opsForValue().get(key);
				redisTemplate.opsForValue().set(key, token, 1, TimeUnit.DAYS);
				log.info("刷新access_token成功！app_name:{}, app_id:{}, app_secret:{}, access_token:{}, old_access_token:{}", 
						app.getAppName(), app.getAppId(), app.getAppSecret(), token.getAccessToken(), oldToken.getAccessToken());
			}
		}catch(Exception e){
			log.error("刷新access_token异常！app_name:{}, app_id:{}, app_secret:{}", app.getAppName(), app.getAppId(), app.getAppSecret(), e);
		}
	}

	/**
	 * 每隔1.5小时刷新token
	 */
    @Scheduled(fixedRate = (int)(1.5 * 60 * 60 * 60))
    public void refreshToken() {
    	log.info("开始刷新access_token！");
    	for(WechatApp app : appList){
    		taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					refreshToken(app);
				}
			});
    	}
    }
    
    /**
	 * 每隔4分钟校验token的有效性
	 */
    @Scheduled(fixedRate = 4 * 60 * 60)
    public void verifyToken() {
    	log.info("开始校验access_token！");
    	for(WechatApp app : appList){
    		taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try{
						String key = WECHAT_TOKEN_PREFIX + app.getAppId();
						Object value = redisTemplate.opsForValue().get(key);
						if(value == null){
							log.info("redis中不存在token!app_name:{}, app_id:{}, app_secret:{}", app.getAppName(), app.getAppId(), app.getAppSecret());
							refreshToken(app);
						}else{
							AccessToken token = (AccessToken) redisTemplate.opsForValue().get(key);
							DateTime dt1 = new DateTime(token.getCreateTime());
							// redis中的token已无效
							if(dt1.plusSeconds(token.getExpiresIn()).isBeforeNow()){
								log.info("redis中的token已无效!app_name:{}, app_id:{}, app_secret:{}", app.getAppName(), app.getAppId(), app.getAppSecret());
								refreshToken(app);
							}
						}
					}catch(Exception e){
						log.error("校验access_token异常！app_name:{}, app_id:{}, app_secret:{}", app.getAppName(), app.getAppId(), app.getAppSecret(), e);
					}
				}
			});
    	}
    }
}
