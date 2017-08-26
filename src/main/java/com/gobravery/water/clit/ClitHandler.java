package com.gobravery.water.clit;

import org.json.JSONObject;

import com.gobravery.water.poll.CmdCode;
import com.gobravery.water.poll.EquCmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClitHandler extends ChannelInboundHandlerAdapter {
	public static int i=0;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		JSONObject login =new JSONObject(new LoginToken());
		String msg = login.toString();    
		EquCmd ec=new EquCmd();
		ec.setModelCode(ec.getModelType());
		ec.setCmd(msg);
		ec.setModelType(CmdCode.LOCK_CLIENT);
		JSONObject cmd=new JSONObject(ec);
		String cmdStr=cmd.toString();
		ByteBuf encoded = ctx.alloc().buffer(4 * cmdStr.length());    
		encoded.writeBytes(cmdStr.getBytes());    
		System.out.println("登陆发送："+cmdStr );    
		ctx.write(encoded);    
		ctx.flush();
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
				System.out.println("SimpleClientHandler.channelRead");    
				ByteBuf result = (ByteBuf) msg;    
				if(result.readableBytes()>0){
					byte[] result1 = new byte[result.readableBytes()];    
					result.readBytes(result1);    
					System.out.println("Server said:" + new String(result1));    
					result.release();    
				}
				Thread.sleep(1000*5);
//				int code=i;
//				i++;
//				String dd = "my client is "+code;    
//				ByteBuf encoded = ctx.alloc().buffer(4 * dd.length());    
//				encoded.writeBytes(dd.getBytes());    
//				ctx.write(encoded);    
//				ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
			cause.printStackTrace();  
			ctx.close();  

	}

}
