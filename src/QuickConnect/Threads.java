package QuickConnect;

import java.sql.SQLException;

public class Threads implements Runnable {
	private User user;
	private Connector sql=Function.mysql();
	public Threads(User user) {
		this.user=user;
	}

	public void run() {
		while(true){
			try {
				sql.update("UPDATE users SET last_on='"+Function.timestamp()+"' WHERE user_ID='"+user.UserID+"'");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
