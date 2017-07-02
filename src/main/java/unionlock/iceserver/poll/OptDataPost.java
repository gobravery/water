package unionlock.iceserver.poll;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import unionlock.iceserver.svr.DPCenter;
import unionlock.iceserver.utils.OptChannelMgr;
import unionlock.iceserver.utils.SrcChannelMgr;

public class OptDataPost {
	private static ExecutorService monitorPool = Executors.newFixedThreadPool(2);
	private static ExecutorService sendPool = Executors.newFixedThreadPool(3);
	private static BlockingQueue<String> dataCache = new LinkedBlockingQueue<String>();
	public static void postData(String dto) {
		try {
			dataCache.put(dto);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//监听进程
	public static void start() {
		
		monitorPool.submit(new Runnable() {

			public void run() {
				while(true) {
					try {
						String dto = dataCache.take();
						EquCmd ec=new EquCmd(new JSONObject(dto));
						if(dto != null) {
							postCachedData(ec.getCmd(),ec.getModelType(),ec.getModelCode());
						}
					} catch (InterruptedException e) {
						//TODO
						e.printStackTrace();
					}
					
				}
			}
			
		});
	}
	//发送进程
	private static void postCachedData(String cmd,String modelType,String modelCode) {
		try {
			//是停止,或超过10个，或上次发时间小于当前时间1分钟
				System.out.println("发送");
				final String fcmd=new String(cmd);
				final String m=new String(modelType);
				sendPool.submit(new Runnable() {
					public void run() {
						try {
							if(m.equals(CmdCode.LOCK_CLIENT)){
								OptChannelMgr.send(fcmd);//直接发送
							}else if(m.equals(CmdCode.MGR_CLIENT)){
								JSONObject jocmd=new JSONObject(fcmd);
								
								OptChannelMgr.send(DPCenter.execMgrClit(jocmd));//发送结果
							}
							
						} catch (Exception e) {
							//TODO
							e.printStackTrace();
						}		


					}
					
				});
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
