package unionlock.iceserver.clit;


public class LoginToken {
	private String userId="web_app";
	private String password="123456";
	private String LOGIN="Y";
	private final String modelType="LOCK_CLIENT";
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getModelType() {
		return modelType;
	}
	public String getLOGIN() {
		return LOGIN;
	}
	public void setLOGIN(String lOGIN) {
		LOGIN = lOGIN;
	}
	
	
	
}
