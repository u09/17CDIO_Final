package QuickConnect;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FunctionUser {
	public Function f;

	public FunctionUser(Function f) {
		this.f = f;
	}

	public boolean addUser(String user, String pass, String email) throws SQLException, NoSuchAlgorithmException {
		con().update("INSERT INTO users VALUES (0,?,?,?,'',0,0,?,0,0)", new String[] { "s", user },
		        new String[] { "s", f.md5(pass) }, new String[] { "s", email },
		        new String[] { "l", Long.toString(f.timestamp()) });
		return false;
	}

	public int changeNickname(String nickname) {
		if(nickname.length() == 0 || nickname.equals(" "))
			return 2;
		try {
			con().update("UPDATE users SET nickname=? WHERE user_id=?", new String[] { "s", nickname },
			        new String[] { "i", "" + user().getUserID() });
			user().setNickname(nickname);
			return 0;
		} catch(SQLException e) {
			return 1;
		}
	}

	public String getNickName() throws SQLException {
//		ResultSet rs = con().select("SELECT nickname FROM users WHERE user_id=" + user().getUserID());
//		String nickname = null;
//		while(rs.next()) {
//			String em = rs.getString("nickname");
//			nickname = em.replace("\n", ",");
//		}
		return user().getNickname();
	}

	public int changePassword(String oldPass, String newPass, String newPass2)
	        throws SQLException, NoSuchAlgorithmException {
		ResultSet rs = con().select("SELECT password FROM users WHERE user_id=?",
		        new String[][] { { "i", "" + user().getUserID() } });
		String arr = null;
		while(rs.next()) {
			String em = rs.getString("password");
			arr = em.replace("\n", ",");
		}
		System.out.println(arr);
		System.out.println(f.md5(oldPass));
		if(f.checkPassword(newPass) == 0 && newPass.equals(newPass2) && f.md5(oldPass).equals(arr)) {
			con().update("UPDATE users SET password=? WHERE user_id =?", new String[] { "s", f.md5(newPass) },
			        new String[] { "i", "" + user().getUserID() });
			return 0;
		} else if(!newPass.equals(newPass2)) {
			// newPass stemmer ikke med med newPass2 stemmer ikke
			return 1;
		} else if(f.checkPassword(newPass) == 1) {
			// Password skal være 8-24
			return 2;
		} else if(f.checkPassword(newPass) == 2) {
			// Password skal indeholde et tal, stort og et lille bogstav
			return 3;
		} else if(!f.md5(oldPass).equals(arr)) {
			// oldPass er ikke det rigtige
			return 4;
		} else {
			// Passwordet må kun indeholde tegn fra ascii<32 || ascii>126
			return 5;
		}
	}

	public int deactivateUser(String password) throws SQLException, NoSuchAlgorithmException {
		boolean bool = false;
		bool = con().check("SELECT user_id FROM users WHERE user_ID=? AND password=?",
		        new String[] { "i", "" + user().getUserID() }, new String[] { "s", f.md5(password) });
		if(bool) {
			con().update("UPDATE users SET user_deleted=1 WHERE user_ID=?",
			        new String[][] { { "i", "" + user().getUserID() } });
			setOfflineUser();
			return 0;
		} else {
			return 1;
		}
	}

	public void activateUser() throws SQLException {
		con().update("UPDATE users SET user_deleted=0 WHERE user_ID=" + user().getUserID());
	}

	public void setOnlineUser() throws SQLException {
		con().update("UPDATE users SET online=1 WHERE user_ID=" + user().getUserID());
	}

	public void setOfflineUser() throws SQLException {
		con().update("UPDATE users SET online=0 WHERE user_ID=" + user().getUserID());
	}

	public void sendMessage(String msg, int send_id, int receive_id) {

	}

	public int addFriend(String username) throws SQLException {
		ResultSet rs = con().select("SELECT user_id FROM users WHERE UPPER(username) LIKE UPPER(?)",
		        new String[][] { { "s", "" + username } });
		String contact_id = null;
		int c_id = 0;
		while(rs.next()) {
			String em = rs.getString("user_id");
			contact_id = em.replace("\n", ",");
			c_id = Integer.parseInt(contact_id);

		}
		boolean isNotFriends = false;
		boolean friendRequest = false;
		if((isNotFriends == con().check("SELECT user_id FROM contacts WHERE user_ID=? AND contact_id=?",
		        new String[] { "i", "" + user().getUserID() }, new String[] { "s", "" + c_id }))
		        || (isNotFriends == con().check("SELECT user_id FROM contacts WHERE contact_id=? AND user_id=?",
		                new String[] { "i", "" + c_id }, new String[] { "s", "" + user().getUserID() }))) {
			con().update("INSERT INTO contacts VALUES(?,?,0,?)", new String[] { "i", "" + user().getUserID() },
			        new String[] { "i", "" + c_id }, new String[] { "l", Long.toString(f.timestamp()) });
			if(friendRequest == con().check(
			        "SELECT contact_id FROM contacts WHERE user_id in("
			        + "SELECT user_id FROM users WHERE username IN("
			        + "SELECT username FROM users WHERE status=0))",
			        new String[] { "i", "" + c_id })) {

			}
			return 1;
		} else return 2;
	}
	
	public String[] OnlineUsersNickname() throws SQLException{
		ArrayList <String> onlineUsers= new ArrayList<String>();
		ResultSet rs = con().select("SELECT nickname FROM users  WHERE (user_ID = ANY(SELECT user_id FROM contacts WHERE contact_id = "+user().getUserID()+" AND status= 1) OR user_id = ANY(SELECT contact_id FROM contacts WHERE user_id = "+user().getUserID()+" AND status= 1))  AND online=1");
		while(rs.next()){
			int uid=rs.getInt("user_ID");
			boolean chk=con().check("SELECT user_ID FROM users WHERE user_ID="+uid+" AND last_on<"+(f.timestamp()-10));
			if(chk) con().update("UPDATE users SET online=0 WHERE user_ID="+uid);
			else onlineUsers.add(rs.getString("nickname"));
		}
		return onlineUsers.toArray(new String[onlineUsers.size()]);
	}
	
	public int[] OnlineUsersId() throws SQLException{
		ArrayList <Integer> onlineUsers= new ArrayList<Integer>();
		ResultSet rs = con().select("SELECT user_ID FROM users  WHERE (user_ID = ANY(SELECT user_id FROM contacts WHERE contact_id = "+user().getUserID()+" AND status= 1) OR user_id = ANY(SELECT contact_id FROM contacts WHERE user_id = "+user().getUserID()+" AND status= 1))  AND online=1");
		while(rs.next()){
			int uid=rs.getInt("user_ID");
			boolean chk=con().check("SELECT user_ID FROM users WHERE user_ID="+uid+" AND last_on<"+(f.timestamp()-10));
			if(chk) con().update("UPDATE users SET online=0 WHERE user_ID="+uid);
			else onlineUsers.add(rs.getInt("user_ID"));
		}
		return f.convertIntegers(onlineUsers);
	}
	
	public String[] offlineUsersNickname() throws SQLException{
		ArrayList <String> offlineUsers=new ArrayList<String>();
		ResultSet rs = con().select("SELECT nickname FROM users  WHERE (user_id = ANY(SELECT user_ID FROM contacts WHERE contact_id = "+user().getUserID()+" AND status= 1) OR user_ID = ANY(SELECT contact_id FROM contacts WHERE user_ID = "+user().getUserID()+" AND status= 1))  AND online=0");
		while(rs.next()) offlineUsers.add(rs.getString("nickname"));
		return offlineUsers.toArray(new String[offlineUsers.size()]);
	}
	
	public int[] offlineUsersId() throws SQLException{
		ArrayList <Integer> offlineUsers=new ArrayList<Integer>();
		ResultSet rs = con().select("SELECT user_ID FROM users  WHERE (user_id = ANY(SELECT user_ID FROM contacts WHERE contact_id = "+user().getUserID()+" AND status= 1) OR user_ID = ANY(SELECT contact_id FROM contacts WHERE user_ID = "+user().getUserID()+" AND status= 1))  AND online=0");
		while(rs.next()) offlineUsers.add(rs.getInt("user_ID"));
		return f.convertIntegers(offlineUsers);
	}
            
	public String[] showGroups() throws SQLException{
		ArrayList<String> groups=new ArrayList<String>();
		ResultSet rs=con().select("SELECT `group_name` FROM `groups` WHERE group_id IN (SELECT group_id FROM `group_members` WHERE group_members.user_id="+user().getUserID()+")");
		while(rs.next()) groups.add(rs.getString("group_name"));
		return groups.toArray(new String[groups.size()]);
	}
	
	public String[][] newMessages() throws SQLException{
		ArrayList<ArrayList<String>> messages=new ArrayList<ArrayList<String>>();
		ResultSet rs = con().select("SELECT user_ID FROM users  WHERE (user_id = ANY(SELECT user_ID FROM contacts WHERE contact_id = "+user().getUserID()+" AND status= 1) OR user_ID = ANY(SELECT contact_id FROM contacts WHERE user_ID = "+user().getUserID()+" AND status= 1))  AND online=0");
		return null;
	}
	
	public Connector con(){
		return f.con();
	}

	public void updateUser(int id, String username, String email, String nickname, int age, int user_created) {
		f.updateUser(id, username, email, nickname, age, user_created);
	}

	public String checkBannedWords(String msg) throws SQLException {
		ResultSet rs = con().select("SELECT word FROM banned_words WHERE word_id = 1");
		return rs.getString("word");
	}
	
	public User user() {
		return f.user();
	}
}
