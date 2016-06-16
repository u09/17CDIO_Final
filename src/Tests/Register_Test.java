package Tests;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;

public class Register_Test {
	
	public static void main(String[] args) throws SQLException, NoSuchAlgorithmException, IOException {
		User u = new User();
		Function f = new Function(u);
		FunctionUser fu = new FunctionUser(f);
		
		fu.con().update("DELETE FROM users WHERE username=test");
		
		final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		final String input = "01-01-2000";
		final LocalDate localDate = LocalDate.parse(input, DATE_FORMAT);
		
		
		int i =f.checkRegister("test", "Test1234", "Test1234", "test123@gmail.com", localDate);
		System.out.println(i);
		if(i==10){
			fu.addUser("test", "Test1234", "test123@gmail.com", 19);
			ResultSet rs = fu.con().select("SELECT email FROM users WHERE username='test'");
			rs.next();
			System.out.println(rs.getString("email"));
		} else{
			System.out.println("Opfylder ikke betingelser");
		}
	}
}
