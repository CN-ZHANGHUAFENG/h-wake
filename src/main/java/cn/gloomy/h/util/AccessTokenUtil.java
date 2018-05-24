package cn.gloomy.h.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import cn.gloomy.h.entity.AccessTokenEntity;

public class AccessTokenUtil {
	private static final Logger logger = LoggerFactory.getLogger(AccessTokenUtil.class);

	private static AccessTokenEntity token = null;

	private static final String appid = "wx888c5ac40563fcae";

	private static final String secret = "f76bdd9fd14dec7e36a2a22d294c9400";

	private static final String grant_type = "client_credential";

	private static final String req_token_url = "https://api.weixin.qq.com/cgi-bin/token?";

	public static String getAccessToken() {
		if (token != null
				&& DateUtil.toAdd(token.getCreateTime(), token.getExpiresIn(), TimeUnit.SECONDS).after(new Date())) {
			return token.getAccessToken();
		}
		HttpClient client = new DefaultHttpClient();
		String url = "";
		Date date = new Date();
		try {
			url = req_token_url + "appid=" + appid + "&secret=" + secret + "&grant_type=" + grant_type;
			HttpGet get = new HttpGet(url);
			HttpResponse resp = client.execute(get);
			String content = EntityUtils.toString(resp.getEntity(), "UTF-8");
			AccessTokenEntity entity = new Gson().fromJson(content, AccessTokenEntity.class);
			if(StringUtils.isNotBlank(entity.getErrcode())&&!"0".equals(entity.getErrcode())) {
				logger.info("==>>req wechat server had happened a error.the error info :{}[{}]",entity.getErrcode(),entity.getErrmsg());
				return "";
			}
			entity.setCreateTime(date);
			token = entity;
			return token.getAccessToken();
		} catch (Exception e) {
			logger.warn("==>>Fail to get access token from wechat server.the url={},the time={}", url,
					DateUtil.defaultFormat(date));
			return null;
		}
	}

}
