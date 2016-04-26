package QuickConnect;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FunctionUser {
	private static Connector con = Function.mysql();
	
	public static void ChangeNickname(String nickname, int id) throws SQLException{
		con.update("UPDATE users set nickname=? WHERE user_id=?",new String[]{"s",nickname},new String[]{"i",""+id});
	}
	
	public static void changePassword(int id, String oldPass, String newPass, String newPass2) throws SQLException, NoSuchAlgorithmException{
		if(Function.checkPassword(newPass)==0 && newPass.equals(newPass2)){
			con.update("update users set password=? where user_id =?", new String[]{"s",Function.md5(newPass)},new String []{"i",""+id});
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
	
	public static void deleteUser(int id, String password) throws SQLException, NoSuchAlgorithmException{
		boolean bool = false;
			bool = con.check("SELECT user_id FROM users WHERE user_id=? AND password=?", new String []{"i",""+id},new String[]{"s",Function.md5(password)});
		if(bool){
			con.update("DELETE FROM users WHERE user_id=?",new String[]{"i",""+id});
		}else{
			System.out.println("Password forkert");
		}
	}
	
	public static ArrayList<String> showGroups(int id) throws SQLException{
		ArrayList<String> groups=new ArrayList<String>();
		ResultSet rs=con.select("SELECT `group_name` FROM `groups` WHERE group_id IN (SELECT group_id FROM `group_members` WHERE group_members.user_id=?)",new String[]{"i",""+id});
		while(rs.next()) groups.add(rs.getString("group_name"));
		return groups;
	}
}
