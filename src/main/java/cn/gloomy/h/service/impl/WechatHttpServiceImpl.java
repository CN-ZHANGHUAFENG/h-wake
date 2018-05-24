package cn.gloomy.h.service.impl;

import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.gloomy.h.util.SystemConfigure;

@Service("wechatHttpService")
public class WechatHttpServiceImpl {

  private final static Logger logger = LoggerFactory.getLogger(WechatHttpServiceImpl.class);

  private HttpClient          client = null;

  public WechatHttpServiceImpl() {
    client = new DefaultHttpClient();
    client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, SystemConfigure.TIMEOUTSECOND * 1000);
    client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SystemConfigure.TIMEOUTSECOND * 1000);
  }

  /**
   * send http to wechat service
   * 
   * @param url
   *          format: ...{},{}...;{} is param
   * @param params
   * @return
   */
  public String sendHttpByGet(String url, Object... params) {
    String httpUrl = MessageFormat.format(url, params);
    HttpGet get = new HttpGet(httpUrl);
    try {
      HttpResponse resp = client.execute(get);
      if(resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        logger.warn(MessageFormat.format("==>>There is a trouble when Access wechat service,the response status is {1}.the url={2},the params={3}",resp.getStatusLine().getStatusCode(),url,params.toString()));
        return null;
      }
      HttpEntity entity = resp.getEntity();
      return EntityUtils.toString(entity, SystemConfigure.ENCODE);
    } catch (Exception e) {
      logger.error("==>>Fail to req wechat service.the url="+url+",the reqParams={}",params);
    }
    return null;
  }
}
