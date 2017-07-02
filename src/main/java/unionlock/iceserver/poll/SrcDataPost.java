package unionlock.iceserver.poll;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import unionlock.iceserver.utils.SrcChannelMgr;

public class SrcDataPost {
	private static ExecutorService monitorPool = Executors.newFixedThreadPool(2);
	private static ExecutorService sendPool = Executors.newFixedThreadPool(3);
	private static BlockingQueue<EquCmd> dataCache = new LinkedBlockingQueue<EquCmd>();
	public static void postData(EquCmd dto) {
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
						EquCmd dto = dataCache.take();
						if(dto != null) {
							postCachedData(dto);
							return;
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
	private static void postCachedData(EquCmd v) {
		try {
			//是停止,或超过10个，或上次发时间小于当前时间1分钟
				System.out.println("发送");
				final String modelCode=new String(v.getModelCode());
				final String cmd=new String(v.getCmd());
				sendPool.submit(new Runnable() {
					public void run() {
						try {
							SrcChannelMgr.send(modelCode,cmd);//直接发送
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
