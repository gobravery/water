package unionlock.iceserver.poll;

import org.json.JSONObject;

public class EquCmd {
	public String modelCode;
	public String cmd;
	private String modelType="MIDDLE_SERVER";
	public EquCmd(JSONObject c){
		modelCode=c.optString("modelCode");
		cmd=c.optString("cmd");
		modelType=c.optString("modelType");	
	}
	public EquCmd(String m,String c){
		modelCode=m;
		cmd=c;		
	}
	public EquCmd(){
		
	}
	
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	
}
