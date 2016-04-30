package QuickConnect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Function {
	private static Connector con = Function.mysql();
	
	public static long timestamp(){
		long unixTime = System.currentTimeMillis() / 1000L;
		return unixTime;
	}

	public static Connector mysql(){
		BufferedReader br = null;
		try{
			String host,db,un,pw;
			br = new BufferedReader(new FileReader("db.txt"));
			host=br.readLine();
			db=br.readLine();
			un=br.readLine();
			pw=br.readLine();
			return new Connector(host,db,un,pw);
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static String md5(String str) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}

	public static int checkUsername(String user) {
		char c;
		if(user.length() <4 || user.length() > 24) return 1;
		for(int i = 0; i<user.length(); i++){
			c = user.charAt(i);
			if(c!=45 && c!= 95 && (c<48 || c>57) && (c<65 || c>90) && (c<97 || c>122)) return 2;
		}
		return 0;
	}

	public static int checkPassword(String pass) {
		String num   = ".*[0-9].*";
		String bl = ".*[A-Z].*";
		String sl = ".*[a-z].*";
		char c;
		if(pass.length() <8 || pass.length() >24) return 1;
		if(!pass.matches(bl) || !pass.matches(num) || !pass.matches(sl)) return 2;
		for(int i = 0; i<pass.length(); i++){
			c = pass.charAt(i);
			if(c<32 || c>126) return 3;
		}
		return 0;
	}

	public static boolean checkEmail(String email) {
		boolean b = email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"); 
		if (b == false) {
//			Alert advarsel = new Alert(AlertType.INFORMATION);
//			advarsel.setTitle("Ugyldig e-mail adresse!");
//			advarsel.setHeaderText(null);
//			advarsel.setContentText("Indtast en gyldig e-mail adresse, for at kunne registrere dig.");
//			advarsel.showAndWait();
			return false;
		}	
		return true;
	}
	
	public static int checkRegister(String user, String pass1, String pass2, String email) throws SQLException {
		Connector con = Function.mysql();
		boolean bool;
		int in;
		in = Function.checkUsername(user);
		if(in == 1)
			return 1;
		else if(in == 2)
			return 2;
		if(!pass1.equals(pass2))
			return 3;
		in = Function.checkPassword(pass1);
		if(in == 1)
			return 4;
		else if(in == 2)
			return 5;
		else if(in == 3)
			return 6;
		if(!Function.checkEmail(email))
			return 7;
		bool = con.check("SELECT username FROM users WHERE UPPER(username) LIKE UPPER(?)", user);
		if(bool)
			return 8;
		else return 9;
	}
	
	public static String[] showOnlineUsers(int id) throws SQLException{
		ArrayList <String> onlineUsers= new ArrayList<String>();
		ResultSet rs = con.select("SELECT user_ID,nickname FROM users  WHERE (user_ID = ANY(SELECT user_id FROM contacts WHERE contact_id = ? AND status= 1) OR user_id = ANY(SELECT contact_id FROM contacts WHERE user_id = ? AND status= 1))  AND online=1;",new String[]{"i",""+id},new String[]{"i",""+id});
		while(rs.next()){
			int uid=rs.getInt("user_ID");
			System.out.println(Function.timestamp()-10);
			System.out.println((int)(Function.timestamp()-10));
			boolean chk=con.check("SELECT user_ID FROM users WHERE user_ID="+uid+" AND last_on<"+(Function.timestamp()-10));
			if(chk) con.update("UPDATE users SET online=0 WHERE user_ID="+uid);
			else onlineUsers.add(rs.getString("nickname"));
		}
		return onlineUsers.toArray(new String[onlineUsers.size()]);
	}
	
	public static String[] showOfflineUsers(int id) throws SQLException{
		ArrayList <String> offlineUsers= new ArrayList<String>();
		ResultSet rs = con.select("select nickname from users  where (user_id = any(select user_id from contacts where contact_id = ? AND status= 1) OR user_id = any(select contact_id from contacts where user_id = ? AND status= 1))  and online=0;",new String[]{"i",""+id},new String[]{"i",""+id});
		while(rs.next())
		offlineUsers.add(rs.getString("nickname"));
		return offlineUsers.toArray(new String[offlineUsers.size()]);
	}
	
	public static String[] showGroups(int id) throws SQLException{
		ArrayList<String> groups=new ArrayList<String>();
		ResultSet rs=con.select("SELECT `group_name` FROM `groups` WHERE group_id IN (SELECT group_id FROM `group_members` WHERE group_members.user_id=?)",new String[][]{{"i",""+id}});
		while(rs.next()) groups.add(rs.getString("group_name"));
		return groups.toArray(new String[groups.size()]);
	}
}
