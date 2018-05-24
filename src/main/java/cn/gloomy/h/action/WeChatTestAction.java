package cn.gloomy.h.action;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import cn.gloomy.h.ReqEntity.WechatReqEntity;
import cn.gloomy.h.util.DecryptUtil;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WeChatTestAction extends BaseAction {

  private static final Logger logger = LoggerFactory.getLogger(WeChatTestAction.class);

  @Value("#{configProperties['wechat.token']}")
  String                      token  = "";

  @RequestMapping("/wechat.do")
  public void checkWeChatService(WechatReqEntity entity) {
    // String token = AccessTokenUtil.getAccessToken();
    logger.info("==>>receive request from wechat server.the WechatReqEntity={}", new Gson().toJson(entity));
    // 字典排序数据，进行加盟米
    Map<String, String> map = new TreeMap<String, String>();
    map.put("token", token);
    map.put("timestamp", entity.getTimestamp());
    map.put("nonce", entity.getNonce());
    String signKey = "";
    for (String key : map.keySet()) {
      signKey += map.get(key);
    }
    signKey = DecryptUtil.SHA1Decrypt(signKey);
    if (StringUtils.isNotBlank(entity.getSignature()) && entity.getSignature().equals(signKey)) {
      logger.info("==>>success to connect wechat server...");
      this.sendResponse(entity.getEchostr());
      return;
    }
    logger.info("==>>wechat connection check test is fail.the signKey={},the token={},the wechat signature={}",
        new Object[] { signKey, token, entity.getSignature() });
  }

  @RequestMapping("vpnTest.do")
  public void testVpn() {
    System.out.println("receive a request from " + getIpAddress(getRequest()));
    this.sendResponse("OK");
  }

  public String getIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (logger.isDebugEnabled()) {
      logger.debug("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
        if (logger.isDebugEnabled()) {
          logger.debug("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
        }
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (logger.isDebugEnabled()) {
          logger.debug("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
        }
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (logger.isDebugEnabled()) {
          logger.debug("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
        }
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (logger.isDebugEnabled()) {
          logger.debug("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
        }
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
        if (logger.isDebugEnabled()) {
          logger.debug("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
        }
      }
    } else if (ip.length() > 15) {
      String[] ips = ip.split(",");
      for (int index = 0; index < ips.length; index++) {
        String strIp = (String) ips[index];
        if (!("unknown".equalsIgnoreCase(strIp))) {
          ip = strIp;
          break;
        }
      }
    }
    return ip;
  }

  public static void main(String[] args) {
    WechatReqEntity entity = new WechatReqEntity();
    entity.setNonce("1286692771");
    entity.setTimestamp("1517821554");
    Map<String, String> map = new TreeMap<String, String>();
    map.put("token", "f76bdd9fd14dec7e36a2a22d294c9400");
    map.put("timestamp", entity.getTimestamp());
    map.put("nonce", entity.getNonce());
    String signKey = "";
    for (String key : map.keySet()) {
      signKey += map.get(key);
    }
    signKey = DecryptUtil.SHA1Decrypt(signKey);
    System.out.println(signKey);
    System.out.println("cc660f1ef6865954df543b08b0e7375b94e88027");
  }
}
