package cn.gloomy.h.enumeration;

public enum WechatErrCode {
  FAIL(-2,"失败"),
	SYSTEM_BUSY(-1,"系统繁忙"),
	SUCCESS(0, "成功");

	private int type;

	private String desc;

	private WechatErrCode(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public static String getDesc(int type) {
		for (WechatErrCode code : WechatErrCode.values()) {
			if (type == code.getType()) {
				return code.getDesc();
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

}
