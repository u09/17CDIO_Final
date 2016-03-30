package QuickConnect;

import java.awt.FontFormatException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import QuickConnect_GUI.startFrame;

public class Main {
	public static void main(String[] args) throws FontFormatException, IOException, SQLException {
		startFrame.start();
		Connector con=Function.mysql();
		ResultSet rs=con.doQuery("SELECT NOW()");
		rs.next();
		System.out.println(rs.getString("NOW()"));
		con.getConnection().close();

	}
}
