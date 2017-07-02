package unionlock.iceserver.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

import unionlock.iceserver.svr.DPCenter;

public class SrcChannelMgr {
	//数据缓冲池用于发送
	public static ConcurrentHashMap<String,ChannelId> channels=new ConcurrentHashMap<String,ChannelId>();
	public static ChannelGroup chgroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static boolean has(Channel cn){
		return chgroup.contains(cn);
	}
	public static void add(Channel cn,String modelCode){
		channels.put(modelCode, cn.id());
		chgroup.add(cn);
	}
	public static void del(String modelCode){
		chgroup.remove(findChannel(modelCode));
		channels.remove(modelCode);
	}
	public static Channel findChannel(String modelCode){
		return chgroup.find(channels.get(modelCode));
	}
	public static Channel writeChannel(String modelCode){
		Channel ch=findChannel(modelCode);
		if(ch!=null){
			if(ch.isWritable()){
				return ch;
			}
		}
		return null;
	}
	public static boolean send(String modelCode,String cmd){
		Channel ch=writeChannel(modelCode);
		if(ch==null || cmd==null)return false;//通道为空
		ByteBuf encoded = ch.alloc().buffer(4 * cmd.length());  
        encoded.writeBytes(cmd.getBytes());  
        ch.writeAndFlush(encoded);
        return true;
	}
	public static boolean send(String v,Channel ch){
		if(v==null || ch==null)return false;
		ByteBuf encoded = ch.alloc().buffer(4 * v.length());  
       encoded.writeBytes(v.getBytes());  
       ch.writeAndFlush(encoded);
       return true;
	}
	public static void close(){
		String v=DPCenter.getSrcClitCloseCmd();//,CharsetUtil.UTF_8
		ByteBuf encoded=ByteBufAllocator.DEFAULT.buffer(4 * v.length());
		encoded.writeBytes(v.getBytes());  
		chgroup.writeAndFlush(encoded);
		chgroup.close().awaitUninterruptibly();
		System.out.println("ddd");
	}
}
