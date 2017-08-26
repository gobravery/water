package com.gobravery.water.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

public class OptChannelMgr {
	//数据缓冲池用于发送
	//public static ConcurrentHashMap<String,ChannelId> channels=new ConcurrentHashMap<String,ChannelId>();
	public static ChannelGroup chgroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static boolean has(Channel cn){
		return chgroup.contains(cn);
	}
	public static void add(Channel cn){
		chgroup.add(cn);
	}
	public static void del(Channel cn){
		chgroup.remove(cn);
	}
	public static Channel writeChannel(){
		for(Channel c:chgroup){
			if(c.isWritable()){
				return c;
			}
		}
		return null;
	}
	public static boolean send(String v){
		 Channel ch=writeChannel();
		 return send(v,ch);
	}
	public static boolean send(String v,Channel ch){
		if(v==null || ch==null)return false;
		ByteBuf encoded = ch.alloc().buffer(4 * v.length());  
        encoded.writeBytes(v.getBytes());  
        ch.writeAndFlush(encoded);
        return true;
	}
public static void close(){
		chgroup.close().awaitUninterruptibly();
	}
}
