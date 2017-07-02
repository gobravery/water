package unionlock.iceserver.clit;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
public class ClitMain {
	public static Integer i=0;
    public static void main(String[] args) throws Exception {  
   	 	int cons=100;
   	 	final int fs=2;
        Executor executor = Executors.newFixedThreadPool(cons); 
        for(i=0;i<cons;i++){
        	executor.execute(new Runnable() {
        		
        		@Override
        		public void run() {
        			ClitConnect client=new ClitConnect();  
        			try {
        				System.out.println(">>>"+i);
        				client.connect("127.0.0.1", 9999,fs);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}  
        		}
        	});
        }
    }  

}

