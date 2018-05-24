package cn.gloomy.h.action;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.gloomy.h.enumeration.WechatErrCode;
import cn.gloomy.h.service.impl.WechatHttpServiceImpl;
import cn.gloomy.h.util.AccessTokenUtil;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequestMapping("/wechatTool")
public class WeChatToolsAction extends BaseAction{
	public static final Logger logger = LoggerFactory.getLogger(WeChatTestAction.class);
	
	@Resource(name = "wechatHttpService")
	private WechatHttpServiceImpl wechatService;
	
	//获取wechat的accessToken
	@RequestMapping("/tokenGet.do")
	public void acquireWechatAccessToke(){
		this.sendResponse(AccessTokenUtil.getAccessToken());
	}
	
	//获取可允许访问微信的IPs
	@RequestMapping("/ipsGet.do")
	public void acquireVisitWechatIps() {
		String ipsGetUrl = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token={1}";
		String token = AccessTokenUtil.getAccessToken();
		String respStr = wechatService.sendHttpByGet(ipsGetUrl, token);
		if(StringUtils.isNotBlank(respStr)) {
		  this.sendResponse(respStr);
		}else {
		  this.sendResponse(getResJson(WechatErrCode.FAIL));
		}
	}
	
	
	
}
