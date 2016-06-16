package Tests;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;

public class changePassword_Test {
	
	public static void main(String[] args) throws SQLException, NoSuchAlgorithmException, IOException {
		User u = new User();
		Function f = new Function(u);
		FunctionUser fu = new FunctionUser(f);
		
		String oldPass = "Test1234";
		String newPass = "newTest1234";
		u.setUserID(1);
		
		ResultSet rs = fu.con().select("SELECT password FROM users WHERE user_id=1");
		rs.next();
		System.out.println(rs.getString("password")+ "--> OLD PASSWORD MD5");
		
		fu.changePassword(oldPass, newPass, newPass);
		
		ResultSet newrs = fu.con().select("SELECT password FROM users WHERE user_id=1");
		newrs.next();
		System.out.println(newrs.getString("password") +" --> NEW PASSWORD IN DATABASE MD5");
		System.out.println(f.md5(newPass) + " --> NEW PASSWORD  MD5");
	}
}
