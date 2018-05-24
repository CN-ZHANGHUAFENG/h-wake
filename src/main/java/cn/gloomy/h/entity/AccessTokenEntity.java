package cn.gloomy.h.entity;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import cn.gloomy.h.ReqEntity.AbstractReqEntity;

public class AccessTokenEntity extends AbstractReqEntity {
	private static final long serialVersionUID = 2841919283707568468L;

	@SerializedName("access_token")
	private String accessToken = "";

	@SerializedName("expires_in")
	private int expiresIn = 0;

	@SerializedName("errcode")
	private String errcode;

	@SerializedName("errmsg")
	private String errmsg;

	private Date createTime = null;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
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

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

}
