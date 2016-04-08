package QuickConnect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class FunctionUser {
	// hej
	public static void ChangeNickname(String nickname, String username) throws SQLException{
		Connector con = Function.mysql();
		con.update("UPDATE users set nickname=? WHERE username=?", nickname,username);
		
	}

}
