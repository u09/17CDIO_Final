package QuickConnect;

import java.io.IOException;
import java.sql.SQLException;

public class Threads implements Runnable {
	private FunctionUser fu;
	public Threads(FunctionUser fu) {
		this.fu=fu;
	}

	@Override
	public void run() {
		while(true){
			fu.f.timestampInc();
			try {
				System.out.println(fu.f.timestamp());
			} catch (IOException e) {
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
