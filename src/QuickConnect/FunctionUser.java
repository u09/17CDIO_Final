package QuickConnect;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class FunctionUser {
	private static Connector con = Function.mysql();
	
	public static int changeNickname(String nickname, int id) {
		if(nickname.length() ==0 || nickname.equals(" ")) return 2;
		try {
			con.update("UPDATE users set nickname=? WHERE user_id=?",new String[]{"s",nickname},new String[]{"i",""+id});
			return 0;
		} catch(SQLException e) {
			return 1;
		}
	}
	
	public static String getNickName(int id) throws SQLException {
		ResultSet rs = con.select("SELECT nickname FROM users WHERE user_id=?", new String[][]{{"i",""+id}});
		String nickname = null;
		while(rs.next()) {
			String em = rs.getString("nickname");
			nickname = em.replace("\n", ",");
		}
		return nickname;
	}
	
	public static int changePassword(int id, String oldPass, String newPass, String newPass2) throws SQLException, NoSuchAlgorithmException{
		ResultSet rs = con.select("SELECT password from users where user_id=?", new String[][]{{"i",""+id}});
		String arr = null;
		while (rs.next()) {
			String em = rs.getString("password");
			arr = em.replace("\n", ",");
		}
		System.out.println(arr);
		System.out.println(Function.md5(oldPass));
		if(Function.checkPassword(newPass)==0 && newPass.equals(newPass2) && Function.md5(oldPass).equals(arr)){
			con.update("update users set password=? where user_id =?", new String[]{"s",Function.md5(newPass)},new String []{"i",""+id});
			return 0;
		}else if(!newPass.equals(newPass2)){
			// newPass stemmer ikke med med newPass2 stemmer ikke
			return 1;
		}else if(Function.checkPassword(newPass)==1){
			// Password skal være 8-24
			return 2;
		}else if(Function.checkPassword(newPass)==2){
			// Password skal indeholde et tal, stort og et lille bogstav
			return 3;
		}else if(!Function.md5(oldPass).equals(arr)){
			// oldPass er ikke det rigtige
			return 4;
		}else {
			// Passwordet må kun indeholde tegn fra ascii<32 || ascii>126
			return 5;
		}
	}
	
	public static int deactivateUser(int id, String password) throws SQLException, NoSuchAlgorithmException{
		boolean bool = false;
			bool = con.check("SELECT user_id FROM users WHERE user_ID=? AND password=?", new String []{"i",""+id},new String[]{"s",Function.md5(password)});
		if(bool){
			con.update("update users set user_deleted=1 WHERE user_ID=?",new String[][]{{"i",""+id}});
			return 0;
		}else{
			return 1;
		}
	}
	
	public static void activateUser(int id) throws SQLException{
		con.update("update users set user_deleted=0 WHERE user_ID=?",new String[][]{{"i",""+id}});
	}
	
	public static void setOnlineUser(int id) throws SQLException{
		 con.update("UPDATE users set online=1 WHERE user_ID="+id);
	}
	public static void setOfflineUser(int id) throws SQLException{
		 con.update("UPDATE users set online=0 WHERE user_ID="+id);
	}
	
	public static void sendMessage(String msg, int send_id, int receive_id) {
		
	}
	
	public static int addFriend(int id, String username) throws SQLException {
		ResultSet rs =con.select("SELECT user_id FROM users WHERE UPPER(username) LIKE UPPER(?)",new String[][]{{"s",""+username}});
		String contact_id = null;
		int c_id=0;
		while (rs.next()) {
			String em = rs.getString("user_id");
			contact_id = em.replace("\n", ",");
			c_id= Integer.parseInt(contact_id);
			
		}
		boolean isNotFriends = false;
		isNotFriends = con.check("SELECT user_id FROM contacts WHERE user_ID=? AND contact_id=?", new String []{"i",""+c_id},new String[]{"s",""+id});
		isNotFriends = con.check("SELECT user_id FROM contacts WHERE contact_id=? AND user_id=?", new String []{"i",""+c_id},new String[]{"s",""+id});
		if(isNotFriends){
		con.update("INSERT INTO contacts VALUES(?,?,0,?)", new String []{ "i",""+id },
				new String [] {"i",""+c_id},new String[] { "l", Long.toString(Function.timestamp()) });
		return 1;
		}
		else return 2;
		
	}
	
}
