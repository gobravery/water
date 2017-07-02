package unionlock.iceserver.svr;

import org.json.JSONObject;

import unionlock.iceserver.poll.CmdCode;
import unionlock.iceserver.poll.EquCmd;
import unionlock.iceserver.utils.OptChannelMgr;
import unionlock.iceserver.utils.SrcChannelMgr;
/**
 * 
 * 
 *数据处理中心 ，用于处理指令
 * 
 */
public class DPCenter {
	//验证登陆令牌，反回登陆结果
	public static String execLoginOKToken(JSONObject cmd){
		//验证cmd
		
		
		//反回执行结果
		 EquCmd loginok=new EquCmd();
		 loginok.setCmd("{"+CmdCode.LOGIN+":'Y'}");
		 JSONObject loginokStr = new JSONObject(loginok); 
		 
		 return loginokStr.toString();
	}
	//执行web端发送的查询指令，返回结果
	public static String execMgrClit(JSONObject cmd){
		EquCmd e=new EquCmd();
		JSONObject lockInfo = new JSONObject(); 
		//查询设备连接数
		if(cmd.has(CmdCode.LOCK_CLIENT_INFO)){
			lockInfo.put(CmdCode.LOCK_CLIENT_INFO,SrcChannelMgr.chgroup.size());
			lockInfo.put(CmdCode.WEB_CLIENT_INFO,OptChannelMgr.chgroup.size());
			setSession(lockInfo,getSession(cmd));
			e.setCmd(lockInfo.toString());
			System.out.println("查询:"+lockInfo.toString());
			e.setModelCode(CmdCode.MIDDLE_SERVER);
		}
		//查询设备连接数
		if(cmd.has(CmdCode.WEB_CLIENT_INFO)){
			lockInfo.put(CmdCode.WEB_CLIENT_INFO,OptChannelMgr.chgroup.size());
			setSession(lockInfo,getSession(cmd));
			e.setCmd(lockInfo.toString());
			e.setModelCode(CmdCode.MIDDLE_SERVER);
		}
		//断开连接,不发送数据
		if(cmd.has(CmdCode.WEB_CLIENT_CLOSE)){
			OptChannelMgr.close();
			return null;
		}
		//断开连接,不发送数据
		if(cmd.has(CmdCode.MIDDLE_SERVER_CLOSE)){
			SrcChannelMgr.close();
			return null;
		}
		//
		return new JSONObject(e).toString();
	}
	//得到停止命令
	public static String getSrcClitCloseCmd(){
		EquCmd ec=new EquCmd();
		JSONObject jocmd=new JSONObject();
		jocmd.put(CmdCode.MIDDLE_SERVER_CLOSE,"Y");
		jocmd.put(CmdCode.MSG,"MIDDLE_SERVER_CLOSE 停止了!");
		ec.setCmd(jocmd.toString());
		ec.setModelType(CmdCode.MIDDLE_SERVER);
		ec.setModelCode(CmdCode.MIDDLE_SERVER);
		JSONObject joShutdownCmd=new JSONObject(ec);
		return joShutdownCmd.toString();
	}
	//设置session
	public static void setSession(JSONObject cmd,String sessionId){
		cmd.put("sessionId",sessionId);
	}
	//取到session
	public static String getSession(JSONObject cmd){
		return cmd.optString("sessionId");
	}
}
