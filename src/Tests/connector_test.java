package Tests;

import java.sql.SQLException;

public class connector_test {
	public static void main(String[] args) throws SQLException {
		Connector con=new Connector("localhost","dtu_livechat","root","gmc22kra");
		con.doUpdate("INSERT INTO users (username,password,email,age,timestamp) VALUES ('Umais','123','a@a.a','22','"+timestamp()+"')");
	}
	
	public static long timestamp(){
		long unixTime = System.currentTimeMillis() / 1000L;
		return unixTime;
	}
}
