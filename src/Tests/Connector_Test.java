package Tests;

import java.sql.SQLException;

public class Connector_Test {
	public static void main(String[] args) throws SQLException {
		TestConnector con=new TestConnector("localhost","dtu_livechat","root","gmc22kra");
		con.doUpdate("INSERT INTO users (username,password,email,age,timestamp) VALUES ('Umais','123','a@a.a','22','"+timestamp()+"')");
	}
	
	public static long timestamp(){
		long unixTime = System.currentTimeMillis() / 1000L;
		return unixTime;
	}
}
