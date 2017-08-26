package com.gobravery.water.svr;

import org.json.JSONObject;

import com.gobravery.water.poll.CmdCode;
import com.gobravery.water.poll.EquCmd;
import com.gobravery.water.poll.OptDataPost;
import com.gobravery.water.poll.SrcDataPost;
import com.gobravery.water.utils.OptChannelMgr;
import com.gobravery.water.utils.SrcChannelMgr;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProxyServerHandler extends ChannelInboundHandlerAdapter {
	//public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
//        try {
        			ByteBuf buf = (ByteBuf) msg;  
        			byte[] restByte = new byte[buf.readableBytes()];  
        	         // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中  
        	         buf.readBytes(restByte);  
        	         String resultStr = new String(restByte);  
        	         // 接收并打印客户端的信息  
        	         System.out.println("data:" + resultStr);  
        	         // 释放资源，这行很关键  
        	         buf.release();  
        	         // 向客户端发送消息  
        	         JSONObject re = new JSONObject(resultStr);  
        	         EquCmd ecmd=new EquCmd(re);
        	         //web端未登陆
        	         if(!OptChannelMgr.has(ctx.channel()) && 
        	        		 (ecmd.getModelType().equals(CmdCode.WEB_CLIENT)
        	        				 || ecmd.getModelType().equals(CmdCode.MGR_CLIENT))
        	        		 ){
        	        	 JSONObject logintoken=new JSONObject(ecmd.getCmd());
        	        	 if(logintoken.has(CmdCode.LOGIN)){
        	        		 
        	        		 OptChannelMgr.add(ctx.channel());
        	        		 //
        	        		 OptChannelMgr.send(DPCenter.execLoginOKToken(logintoken), ctx.channel());
        	        		 //
        	        	 }
        	        	 return;
        	         }
        	       //锁送未登陆
        	         if(!SrcChannelMgr.has(ctx.channel()) && ecmd.getModelType().equals(CmdCode.LOCK_CLIENT)){
        	        	 JSONObject logintoken=new JSONObject(ecmd.getCmd());
        	        	 if(logintoken.has(CmdCode.LOGIN)){
        	        		 
        	        		 SrcChannelMgr.add(ctx.channel(),ecmd.getModelCode());
        	        		 //
        	        		 SrcChannelMgr.send(DPCenter.execLoginOKToken(logintoken), ctx.channel());
        	        	 }
        	        	 return;
        	         }
        	         //
        	         if(ecmd.getModelType().equals(CmdCode.LOCK_CLIENT)){
        	        	 
        	        	 OptChannelMgr.add(ctx.channel());
        	        	 
        	        	 OptDataPost.postData(resultStr);
        	         }else if(ecmd.getModelType().equals(CmdCode.WEB_CLIENT)){
        	        	 
        	        	 SrcChannelMgr.add(ctx.channel(),ecmd.modelCode);
        	        	 
        	        	 SrcDataPost.postData(ecmd);
        	         }else if(ecmd.getModelType().equals(CmdCode.MGR_CLIENT)){
        	        	 
        	        	 OptChannelMgr.add(ctx.channel());
        	        	 
        	        	 OptDataPost.postData(resultStr);
        	         }else{
        	        	 System.out.println("设备类型不对!");  
        	         }
        	        
//        } finally {
//            ReferenceCountUtil.release(msg); // (2)
//        }

	}

//	@Override
//	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//		channels.add(ctx.channel());
//	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		//super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		ctx.close();
	}
	
}
