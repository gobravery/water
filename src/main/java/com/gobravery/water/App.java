package com.gobravery.water;

import com.gobravery.water.poll.OptDataPost;
import com.gobravery.water.poll.SrcDataPost;
import com.gobravery.water.svr.ProxyServer;
import com.gobravery.water.utils.IceLogger;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	//启动监听
    	OptDataPost.start();
    	SrcDataPost.start();
    	//启动服务
    	//System.out.println("server is start!");
    	IceLogger.log("server is start!");
        int port=9999;
        if(args!=null && args.length>0){
        	port=Integer.valueOf(args[0]);
        }
        new ProxyServer(port).run();
    }
}
