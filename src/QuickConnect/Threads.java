package QuickConnect;

import java.io.IOException;

public class Threads implements Runnable {
	private FunctionUser fu;

	public Threads(FunctionUser fu) {
		this.fu = fu;
	}

	@Override
	public void run() {
		while(true) {
			try {
				fu.f.timestampInc();
				Thread.sleep(1000);
			} catch(InterruptedException | IOException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
