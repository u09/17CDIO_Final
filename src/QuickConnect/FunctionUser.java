package QuickConnect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class FunctionUser {
	
	static Connector con = Function.mysql();
	
	public static void ChangeNickname(String nickname, String username) throws SQLException{
		con.update("UPDATE nickname set nickname=? WHERE username=?", nickname,username);
	}
	public static void changePassword(String username, String oldPass, String newPass, String newPass2) throws SQLException, NoSuchAlgorithmException{
		if(Function.checkPassword(newPass)==0 && newPass.equals(newPass2)){
			con.update("update users set password=? where username =?", Function.md5(newPass),username);
		}else if(newPass.equals(newPass2)){
			// password stemmer ikke
		}else if(Function.checkPassword(newPass)==1){
			// Password skal være 8-24
		}else if(Function.checkPassword(newPass)==2){
			// Password skal indeholde et tal, stort og et lille bogstav
		}else {
			// Passwordet må kun indeholde tegn fra ascii<32 || ascii>126
		}
	}
	public static void deleteUser(String username, String password) throws SQLException, NoSuchAlgorithmException{
		boolean bool = false;
			bool = con.check("SELECT username FROM users WHERE username=? AND password=?", username, Function.md5(password));
		if(bool){
			con.update("DELETE FROM users WHERE username=?",username);
		}else{
			System.out.println("Password forkert");
		}
	}

}
