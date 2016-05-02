package QuickConnect;

import java.sql.SQLException;

public class Threads implements Runnable {
	private FunctionUser fu;
	public Threads(FunctionUser fu) {
		this.fu=fu;
	}

	@Override
	public void run() {
		while(true){
			try {
				fu.con().update("UPDATE users SET last_on='"+fu.f.timestamp()+"' WHERE user_ID='"+fu.user().getUserID()+"'");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			System.out.println("Thread successfully run");
			try {
			    Thread.sleep(5000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	}
}
