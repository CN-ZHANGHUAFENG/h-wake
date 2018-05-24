package cn.gloomy.h.action;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import cn.gloomy.h.enumeration.WechatErrCode;


@Controller
public class BaseAction {

  private Logger              logger          = LoggerFactory.getLogger(BaseAction.class);

  private String              errorMessage;

  private long                contentLength;

  private InputStream         inputStream;

  private String              filename;

  private HttpServletRequest  request;

  private HttpServletResponse response;

  private HttpSession         session;

  @ModelAttribute
  public void setReqAndResp(HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
    this.session = request.getSession(true);
  }

  public Map<String, Object> converMVCReturnMap(String[] keys, Object[] values) {

    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (keys != null && values != null) {
        if (keys.length > 0 && values.length > 0) {
          for (int index = 0; index < keys.length; index++) {
            map.put(keys[index], values[index]);
          }
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.error(e.getMessage(), e);
      logger.error("conver spring mvc return map is error. The number of keys not equal to the number of values.");
    }
    return map;
  }

  @SuppressWarnings("rawtypes")
  public Map<String, Object> converMVCReturnMap(Object obj) {

    Map<String, Object> map = new HashMap<String, Object>();

    try {
      Class objClass = obj.getClass();
      for (Field field : objClass.getDeclaredFields()) {
        String key = field.getName();
        field.setAccessible(true);
        Object value = field.get(obj);
        if (value != null) {
          if (value instanceof Integer || value instanceof Long || value instanceof Short) {
            map.put(key, String.valueOf(value));
          } else if (value instanceof Float) {
            Float num = (Float) value;
            DecimalFormat df = new DecimalFormat("#.##");
            map.put(key, df.format(num));
          } else if (value instanceof Double) {
            Double num = (Double) value;
            DecimalFormat df = new DecimalFormat("#.##");
            map.put(key, df.format(num));
          } else {
            map.put(key, value);
          }
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new HashMap<String, Object>();
    }

    return map;
  }

  public HttpServletRequest getRequest() {
    return request;
  }

  public HttpServletResponse getResponse() {
    response.setContentType("text/html;charset=utf-8");
    response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS,"); 
    response.setHeader("Access-Control-Max-Age", "300"); //设置过期时间 ,单位秒
    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization"); 
//    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // 支持HTTP 1.1. 
//    response.setHeader("Pragma", "no-cache"); // 支持HTTP 1.0. response.setHeader("Expires", "0")
    return response;
  }

  public HttpSession getSession() {
    return session;
  }

  public void sendResponse(String message) {
    try {
      HttpServletResponse response = getResponse();
      response.getWriter().write(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getRespJson(int type, String message) {
    return "{\"result_code\":" + type + ",\"result_msg\": \"" + message + "\"}";
  }
  
  public String getResJson(WechatErrCode code) {
    return "{\"res_code\":"+code.getType()+",\"res_msg\":"+code.getDesc()+"\"}";
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public long getContentLength() {
    return contentLength;
  }

  public void setContentLength(long contentLength) {
    this.contentLength = contentLength;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

}
