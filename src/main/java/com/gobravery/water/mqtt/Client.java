package com.gobravery.water.mqtt;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Client {

	public static final String HOST = "tcp://10.192.2.154:1883";//iot.eclipse.org:1883
	private static String clientid = "client";
	public static final String TOPIC = "tokudu/"+clientid;
	private MqttClient client;
	private MqttConnectOptions options;
	private boolean start=true;//标志位
	//private String userName = "test";
	//private String passWord = "test";
	public Client(int key){
		clientid+=key;
	}
	private ScheduledExecutorService scheduler;
	//运行程序
	private final static int runcount=300;
	private final static int step=10;//运行阶数10秒
	//
	private final static int testcount=2;
	//
	private static ScheduledExecutorService startPool=Executors.newScheduledThreadPool(runcount);
	//调度程序
	private static ScheduledExecutorService schedPool=Executors.newScheduledThreadPool(4);
	private static LinkedBlockingQueue<Client> waitList=new LinkedBlockingQueue<Client>();
	private static LinkedBlockingQueue<Client> runList=new LinkedBlockingQueue<Client>();
	private static LinkedBlockingQueue<Client> sleepList=new LinkedBlockingQueue<Client>();
	//重新链接
	public void startReconnect() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if(!start){
					
				}else if (!client.isConnected()) {
					try {
						client.connect(options);
					} catch (MqttSecurityException e) {
						e.printStackTrace();
					} catch (MqttException e) {
						e.printStackTrace();
					}
				}
			}
		}, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
	}

	private void start() {
		try {
			// host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			client = new MqttClient(HOST, clientid, new MemoryPersistence());
			// MQTT的连接设置
			options = new MqttConnectOptions();
			// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(false);//保证接收离线消息\
			// 设置连接的用户名
			//options.setUserName(userName);
			// 设置连接的密码
			//options.setPassword(passWord.toCharArray());
			// 设置超时时间 单位为秒
			options.setConnectionTimeout(10);
			// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
			options.setKeepAliveInterval(20);
			//
			// 设置回调
			client.setCallback(new PushCallback());
			MqttTopic topic = client.getTopic(TOPIC);
			//setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息  
			options.setWill(topic, "close".getBytes(), 0, true);
			
			client.connect(options);
			//订阅消息
			int[] Qos  = {2};
			String[] topic1 = {TOPIC};
			client.subscribe(topic1, Qos);
		} catch (Exception e) {
			System.out.println("连接出错!"+e.getLocalizedMessage());
		}
	}
	public void disconnect() {
		 try {
			start=false;
			if(client.isConnected()){
				client.disconnect();
			}
		} catch (MqttException e) {
			System.out.println("断开出错!"+e.getLocalizedMessage());
		}
	}
	public static void initStartList()throws MqttException,InterruptedException{
		for(int i=0;i<testcount;i++){
			waitList.put(new Client(i));
		}
	}
	public static void main(String[] args) throws Exception {
		//准备
		schedPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					initStartList();
				} catch (MqttException | InterruptedException e) {
					System.out.println("初始化waitList出错!"+e.getLocalizedMessage());
				}
			}
		});
		//调度进程运行300监听
		schedPool.execute(new Runnable() {
			@Override
			public void run() {
				while(!waitList.isEmpty()){
					try {
						final Client  d=waitList.take();
						startPool.execute(new Runnable() {
							
							@Override
							public void run() {
								try {
									runList.put(d);
								} catch (InterruptedException e) {
									System.out.println("转runlist出错!"+e.getLocalizedMessage());
								}
								d.start();
							}
						});
					} catch (InterruptedException e) {
						System.out.println("取waitList出错!"+e.getLocalizedMessage());
					}
					
				}
			}
		});
		//调度进程睡眠监听每次300,运行10秒
		schedPool.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				int i=runcount;
				do{
					try {
						Client d=runList.take();
						sleepList.put(d);
						d.disconnect();
					} catch (InterruptedException e) {
						System.out.println("取runList转sleepList!"+e.getLocalizedMessage());
					}
					i--;
				}while(i>0);
			}
		}, step * 1000, step * 1000, TimeUnit.MILLISECONDS);
		//调度进程待运行监听每次300,睡眠10秒
		schedPool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				int i=runcount;
				do{
					try {
						Client d=sleepList.take();
						waitList.put(d);
					} catch (InterruptedException e) {
						System.out.println("取runList转sleepList!"+e.getLocalizedMessage());
					}
					i--;
				}while(i>0);
			}
		}, 2*step * 1000, step * 1000, TimeUnit.MILLISECONDS);
		
	}

}
