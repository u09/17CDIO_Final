package QuickConnect;

import java.io.IOException;

public class Threads implements Runnable {
	private FunctionUser fu;
	public Threads(FunctionUser fu) {
		this.fu=fu;
	}

	@Override
	public void run() {
		while(true){
			try {
				fu.f.timestampInc();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println(fu.f.timestamp());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}
}
