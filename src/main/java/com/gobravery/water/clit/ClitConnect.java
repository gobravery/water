package com.gobravery.water.clit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashSet;
import java.util.Set;

public class ClitConnect {
	public void connect(String host, int port,int count) throws Exception {
		   		Set<ChannelFuture> fs=new HashSet<ChannelFuture>(count);
				EventLoopGroup workerGroup = new NioEventLoopGroup();  
				Bootstrap b = new Bootstrap();  
		         try {  
		             b.group(workerGroup);  
		             b.channel(NioSocketChannel.class);  
		             b.option(ChannelOption.SO_KEEPALIVE, true);  
		             //动态ByteBuf相对于FixedRecvByteBufAllocator
		             AdaptiveRecvByteBufAllocator arb=new AdaptiveRecvByteBufAllocator(1*4,4*4,50*4);
		             //arb=AdaptiveRecvByteBufAllocator.DEFAULT;
		             b.option(ChannelOption.RCVBUF_ALLOCATOR, arb);//AdaptiveRecvByteBufAllocator.DEFAULT
		             //内存池开启，默认不使用
		             b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		             //
		             b.handler(new ChannelInitializer<SocketChannel>() {  
		                 @Override  
		                 public void initChannel(SocketChannel ch) throws Exception {  
		                     ch.pipeline().addLast(new ClitHandler());  
		                 }  
		             });  
		   
		             for(int i=0;i<count;i++){
		            	 
		            	 // Start the client.  
		            	 ChannelFuture f = b.connect(host, port).sync();  
		            	 fs.add(f);
		             }
		             // Wait until the connection is closed.  
		             for(ChannelFuture f:fs){
		            	 f.channel().closeFuture().sync();  
		            	 System.out.println("conn>>>>");
		             }
		         } finally {  
		             workerGroup.shutdownGracefully();  
		         }  
		     }  
		       
}
