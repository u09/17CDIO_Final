package QuickConnect;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FunctionUser {
	public Function f;
	private ArrayList<Integer> messages;

	public FunctionUser(Function f) {
		this.f = f;
	}

	public boolean addUser(String user, String pass, String email, int age)
	        throws SQLException, NoSuchAlgorithmException, IOException {
		con().update("INSERT INTO users VALUES (0,?,?,?,'',?,0,?,0,0,0)", new String[] { "s", user },
		        new String[] { "s", f.md5(pass) }, new String[] { "s", email }, new String[] { "i", "" + age },
		        new String[] { "l", Long.toString(f.timestamp()) });
		return false;
	}

	public void activateUser() throws SQLException {
		con().update("UPDATE users SET user_deleted=0 WHERE user_ID=" + user().getUserID());
	}

	public int deactivateUser(String password) throws SQLException, NoSuchAlgorithmException {
		boolean bool = false;
		bool = con().check("SELECT user_id FROM users WHERE user_ID=? AND password=?",
		        new String[] { "i", "" + user().getUserID() }, new String[] { "s", f.md5(password) });
		if(bool) {
			con().update("UPDATE users SET user_deleted=1 WHERE user_ID=?",
			        new String[][] { { "i", "" + user().getUserID() } });
			setUserOffline();
			return 0;
		} else {
			return 1;
		}
	}

	public void activateUserMail(String username) throws SQLException {
		con().update("UPDATE users SET activated=1 WHERE username='" + username + "'");
	}

	public String getEmail(String userIn) throws SQLException {
		ResultSet rs = con().select("SELECT email FROM users WHERE username='" + userIn + "'");
		rs.next();
		return rs.getString("email");
	}

	public void setUserOnline() throws SQLException {
		con().update("UPDATE users SET online=1 WHERE user_ID=" + user().getUserID());
	}

	public void setUserOffline() throws SQLException {
		con().update("UPDATE users SET online=0 WHERE user_ID=" + user().getUserID());
	}

	public void updateUser(int id, String username, String email, String nickname, int age, int user_created) {
		f.updateUser(id, username, email, nickname, age, user_created);
	}

	public String getInfoUser(int ID, int choice) throws SQLException {
		ResultSet rs = con().select("SELECT username,nickname,age,user_created FROM users WHERE user_id='" + ID + "'");
		rs.next();
		if(choice == 1)
			return rs.getString("username");
		if(choice == 2)
			return rs.getString("nickname");
		if(choice == 3)
			return Integer.toString(rs.getInt("age"));
		else return Integer.toString(rs.getInt("user_created"));
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
		return user().getNickname();
	}

	public int addFriend(String username) throws SQLException {
		// DENNE FUNKTION SKAL RETTES, DEN VIRKER IKKE KORREKT
		if(!con().check("SELECT user_id FROM users WHERE UPPER(username) LIKE UPPER(?)",
		        new String[][] { { "s", "" + username } }))
			return 4;
		ResultSet rs = con().select("SELECT user_id FROM users WHERE UPPER(username) LIKE UPPER(?)",
		        new String[][] { { "s", "" + username } });
		rs.next();
		int id = rs.getInt("user_id");
		if(con().check("SELECT user_id,blocked_id FROM blocked_contact WHERE (user_id='" + id + "' AND blocked_id='"
		        + user().getUserID() + "') OR (user_id='" + user().getUserID() + "' AND blocked_id='" + id + "')"))
			return 1;
		else if(con().check("SELECT user_id FROM contacts WHERE (user_id='" + id + "' AND contact_id='"
		        + user().getUserID() + "') OR (user_id='" + user().getUserID() + "' AND contact_id='" + id + "')")) {
			if(con().check(
			        "SELECT user_id FROM contacts WHERE ((user_id='" + id + "' AND contact_id='" + user().getUserID()
			                + "') OR (user_id='" + user().getUserID() + "' AND contact_id='" + id + "')) AND status=1"))
				return 2;
			else if(con().check(
			        "SELECT user_id FROM contacts WHERE ((user_id='" + id + "' AND contact_id='" + user().getUserID()
			                + "') OR (user_id='" + user().getUserID() + "' AND contact_id='" + id + "')) AND status=0"))
				return 3;
		}
		con().update("INSERT INTO contacts VALUES(?,?,0,?)", new String[] { "i", "" + user().getUserID() },
		        new String[] { "i", "" + id }, new String[] { "l", Long.toString(0) });
		return 0;
	}

	public void deleteFriend(int ID) throws SQLException {
		con().update("DELETE FROM contacts WHERE (user_ID='" + ID + "' AND contact_ID='" + user().getUserID() + "') "
		        + "OR (contact_ID='" + ID + "' AND user_ID='" + user().getUserID() + "')");
	}

	public void createGroup(int groupOwner, String groupName, ArrayList<Integer> allFriendsId2)
	        throws SQLException, IOException {
		allFriendsId2.add(user().getUserID());
		int[] ids = f.convertIntegers(allFriendsId2);
		allFriendsId2.remove(allFriendsId2.indexOf(user().getUserID()));
		con().update("INSERT INTO groups(owner_id,group_name, group_created) VALUES ('" + groupOwner + "','" + groupName
		        + "','" + f.timestamp() + "')");
		ResultSet rs = con()
		        .select("SELECT group_id FROM groups WHERE owner_id='" + groupOwner + "' ORDER BY group_id DESC");
		rs.next();
		int group_id = rs.getInt("group_id");
		for(int i = 0; i < ids.length; i++) {
			con().update("INSERT INTO group_members(group_id, user_id, group_joined) VALUES ('" + group_id + "','"
			        + ids[i] + "','" + f.timestamp() + "')");
		}
	}

	public int deleteGroup(int groupID) throws SQLException {
		if(con().check("SELECT owner_id FROM groups where owner_id =" + user().getUserID())) {
			con().update("DELETE FROM group_members WHERE group_id=" + groupID);
			con().update("DELETE FROM groups WHERE group_id=" + groupID);
			return 1;
		}
		return 0;
	}

	public void leaveGroup(int groupID) throws SQLException {
		con().update("DELETE FROM group_members WHERE group_id=" + groupID + " AND user_id=" + user().getUserID());
	}

	public void throwOut(int groupID) throws SQLException {
		if(con().check("SELECT owner_id FROM groups where owner_id =" + user().getUserID())) {
			con().update("DELETE FROM group_members WHERE group_id=" + groupID + "AND user_id=" + user().getUserID());
		}
	}

	public void sendMessage(String msg, int receive_id) throws SQLException, IOException {
		msg = cencorMessage(msg);
		con().update("INSERT INTO messages (message,user_ID,message_sent,receiver_id) VALUES (?,'" + user().getUserID()
		        + "','" + f.timestamp() + "','" + receive_id + "')", msg);
		System.out.println("SENDT: (message,user_ID,message_sent,receiver_id) VALUES ('" + msg + "','"
		        + user().getUserID() + "','" + f.timestamp() + "','" + receive_id + "')");
	}

	public void sendGroupMessage(String msg, int groupID) throws SQLException, IOException {
		msg = cencorMessage(msg);
		con().update("INSERT INTO group_messages (group_message,user_ID,group_message_sent,group_id) VALUES (?,'"
		        + user().getUserID() + "','" + f.timestamp() + "','" + groupID + "')", msg);
		System.out.println("SENDT: (message,user_ID,message_sent,receiver_id) VALUES ('" + msg + "','"
		        + user().getUserID() + "','" + f.timestamp() + "','" + groupID + "')");
	}

	public String checkBannedWords(String msg) throws SQLException {
		ResultSet rs = con().select("SELECT word FROM banned_words WHERE word_id = 1");
		return rs.getString("word");
	}

	public String cencorMessage(String msg) throws SQLException {
		ResultSet rs = con().select(
		        "SELECT * FROM banned_words WHERE MATCH (word) AGAINST ('" + msg + "' IN NATURAL LANGUAGE MODE)");
		String badword;
		while(rs.next()) {
			badword = rs.getString("word");
			msg = msg.replace(badword, "****");
		}
		return msg;
	}

	public void getMessages(ArrayList<ArrayList<String>> msg, ArrayList<Integer> users) throws SQLException {
		ResultSet rs = con()
		        .select("SELECT message,user_ID,message_sent FROM messages WHERE receiver_id='" + user().getUserID()
		                + "' AND message_deleted=0 AND message_sent>=ANY(SELECT last_on FROM users WHERE user_ID='"
		                + user().getUserID() + "')");
		int index;
		Integer ID;
		while(rs.next()) {
			ID = rs.getInt("user_ID");
			if(users.contains(ID))
				index = users.indexOf(ID);
			else {
				users.add(ID);
				msg.add(new ArrayList<String>());
				index = users.indexOf(ID);
			}
			msg.get(index).add(rs.getString("message"));
		}
		f.printArrayListMulti(msg);
		f.printArrayList(users);
	}

	public ArrayList<ArrayList<String>> getMessages(int id, long timestamp) throws SQLException {
		ArrayList<ArrayList<String>> messages = new ArrayList<ArrayList<String>>();
		messages.add(new ArrayList<String>());
		messages.add(new ArrayList<String>());
		ResultSet rs = con().select("SELECT message,user_ID FROM messages WHERE ((receiver_id='" + user().getUserID()
		        + "' AND user_ID=" + id + ") OR" + " (receiver_id='" + id + "' AND user_ID=" + user().getUserID()
		        + ")) AND message_deleted=0 AND message_sent>=" + timestamp);
		while(rs.next()) {
			messages.get(0).add(rs.getString("message"));
			messages.get(1).add(idToNickname(rs.getInt("user_ID")));
		}
		return messages;
	}

	public ArrayList<ArrayList<String>> getGroupMessages(int groupId) throws SQLException {
		ArrayList<ArrayList<String>> msg = new ArrayList<ArrayList<String>>();
		ResultSet rs = con()
		        .select("SELECT group_message,user_ID,group_message_sent FROM group_messages WHERE group_id=" + groupId
		                + " AND group_message_deleted=0 AND user_id!=" + user().getUserID()
		                + " AND group_message_sent>=ANY(SELECT last_on FROM users WHERE user_ID='" + user().getUserID()
		                + "')");
		msg.add(new ArrayList<String>());
		msg.add(new ArrayList<String>());
		while(rs.next()) {
			msg.get(0).add(rs.getString("group_message"));
			msg.get(1).add(idToNickname(rs.getInt("user_id")));
		}
		return msg;
	}

	public ArrayList<ArrayList<String>> getGroupMessages(int id, long timestamp) throws SQLException {
		ArrayList<ArrayList<String>> groupMessages = new ArrayList<ArrayList<String>>();
		groupMessages.add(new ArrayList<String>());
		groupMessages.add(new ArrayList<String>());
		ResultSet rs = con().select("SELECT group_message,user_ID FROM group_messages WHERE group_id='" + id
		        + "' AND group_message_deleted=0 AND group_message_sent>=" + timestamp);
		while(rs.next()) {
			groupMessages.get(0).add(rs.getString("group_message"));
			groupMessages.get(1).add(idToNickname(rs.getInt("user_ID")));
		}
		return groupMessages;
	}

	public ArrayList<Integer> getAllFriendsId() throws SQLException {
		ArrayList<Integer> allFriendsId = new ArrayList<Integer>();
		ResultSet rs = con()
		        .select("SELECT user_ID FROM users WHERE (user_ID = ANY(SELECT user_id FROM contacts WHERE contact_id = "
		                + user().getUserID()
		                + " AND status= 1) OR user_id = ANY(SELECT contact_id FROM contacts WHERE user_id = "
		                + user().getUserID() + " AND status= 1)) ORDER BY user_ID");
		while(rs.next())
			allFriendsId.add(rs.getInt("user_ID"));
		f.printArrayList(allFriendsId);
		return allFriendsId;
	}

	public String[] getAllFriendsNickname() throws SQLException {
		ArrayList<String> allFriendsUsername = new ArrayList<String>();
		ResultSet rs = con()
		        .select("SELECT nickname FROM users WHERE (user_ID = ANY(SELECT user_id FROM contacts WHERE contact_id = "
		                + user().getUserID()
		                + " AND status= 1) OR user_id = ANY(SELECT contact_id FROM contacts WHERE user_id = "
		                + user().getUserID() + " AND status= 1)) ORDER BY user_ID");
		while(rs.next()) {
			String uName = rs.getString("nickname");
			allFriendsUsername.add(uName);
		}
		f.printArrayList(allFriendsUsername);
		return allFriendsUsername.toArray(new String[allFriendsUsername.size()]);
	}

	public int[] getOnlineUsersId() throws SQLException, IOException {
		ArrayList<Integer> onlineUsers = new ArrayList<Integer>();
		ResultSet rs = con()
		        .select("SELECT user_ID FROM users WHERE (user_ID = ANY(SELECT user_id FROM contacts WHERE contact_id = "
		                + user().getUserID()
		                + " AND status= 1) OR user_id = ANY(SELECT contact_id FROM contacts WHERE user_id = "
		                + user().getUserID() + " AND status= 1))  AND online=1");
		while(rs.next()) {
			int uid = rs.getInt("user_ID");
			boolean chk = con()
			        .check("SELECT user_ID FROM users WHERE user_ID=" + uid + " AND last_on<" + (f.timestamp() - 10));
			if(chk)
				con().update("UPDATE users SET online=0 WHERE user_ID=" + uid);
			else onlineUsers.add(rs.getInt("user_ID"));
		}
		return f.convertIntegers(onlineUsers);
	}

	public String[] getOnlineUsersNickname() throws SQLException, IOException {
		ArrayList<String> onlineUsers = new ArrayList<String>();
		ResultSet rs = con()
		        .select("SELECT user_ID, nickname FROM users WHERE (user_ID = ANY(SELECT user_id FROM contacts WHERE contact_id = "
		                + user().getUserID()
		                + " AND status= 1) OR user_id = ANY(SELECT contact_id FROM contacts WHERE user_id = "
		                + user().getUserID() + " AND status= 1))  AND online=1");
		while(rs.next()) {
			int uid = rs.getInt("user_ID");
			boolean chk = con()
			        .check("SELECT user_ID FROM users WHERE user_ID=" + uid + " AND last_on<" + (f.timestamp() - 10));
			if(chk)
				con().update("UPDATE users SET online=0 WHERE user_ID=" + uid);
			else onlineUsers.add(rs.getString("nickname"));
		}
		return onlineUsers.toArray(new String[onlineUsers.size()]);
	}

	public int[] getOfflineUsersId() throws SQLException {
		ArrayList<Integer> offlineUsers = new ArrayList<Integer>();
		ResultSet rs = con()
		        .select("SELECT user_ID FROM users  WHERE (user_id = ANY(SELECT user_ID FROM contacts WHERE contact_id = "
		                + user().getUserID()
		                + " AND status= 1) OR user_ID = ANY(SELECT contact_id FROM contacts WHERE user_ID = "
		                + user().getUserID() + " AND status= 1))  AND online=0");
		while(rs.next())
			offlineUsers.add(rs.getInt("user_ID"));
		return f.convertIntegers(offlineUsers);
	}

	public String[] getOfflineUsersNickname() throws SQLException {
		ArrayList<String> offlineUsers = new ArrayList<String>();
		ResultSet rs = con()
		        .select("SELECT nickname FROM users  WHERE (user_id = ANY(SELECT user_ID FROM contacts WHERE contact_id = "
		                + user().getUserID()
		                + " AND status= 1) OR user_ID = ANY(SELECT contact_id FROM contacts WHERE user_ID = "
		                + user().getUserID() + " AND status= 1))  AND online=0");
		while(rs.next())
			offlineUsers.add(rs.getString("nickname"));
		return offlineUsers.toArray(new String[offlineUsers.size()]);
	}

	public int[] getGroupsId() throws SQLException {
		ArrayList<Integer> groups = new ArrayList<Integer>();
		ResultSet rs = con().select("SELECT group_id FROM group_members WHERE user_id=" + user().getUserID());
		while(rs.next())
			groups.add(rs.getInt("group_id"));
		return f.convertIntegers(groups);
	}

	public String[] getGroupsNames() throws SQLException {
		ArrayList<String> groups = new ArrayList<String>();
		ResultSet rs = con()
		        .select("SELECT `group_name` FROM `groups` WHERE group_id IN (SELECT group_id FROM `group_members` WHERE group_members.user_id="
		                + user().getUserID() + ")");
		while(rs.next())
			groups.add(rs.getString("group_name"));
		return groups.toArray(new String[groups.size()]);
	}

	public String[] getSentRequests() throws SQLException {
		ArrayList<String> requests = new ArrayList<String>();
		ResultSet rs = con().select("SELECT contact_id FROM contacts NATURAL JOIN users WHERE user_id='"
		        + user().getUserID() + "' AND status=0");
		ResultSet rs1;
		while(rs.next()) {
			rs1 = con().select("SELECT username FROM users WHERE user_id='" + rs.getInt("contact_id") + "'");
			while(rs1.next()) {
				requests.add(rs1.getString("username"));
			}
		}
		return requests.toArray(new String[requests.size()]);
	}

	public String[] getFriendsRequests() throws SQLException {
		ArrayList<String> requests = new ArrayList<String>();
		ResultSet rs = con().select("SELECT username FROM contacts NATURAL JOIN users WHERE contact_id='"
		        + user().getUserID() + "' AND status=0");
		while(rs.next())
			requests.add(rs.getString("username"));
		return requests.toArray(new String[requests.size()]);
	}

	public String[] getBlockedFriendsList() throws SQLException {
		ResultSet rs = con()
		        .select("select username from users where user_id = any(select blocked_id from blocked_contact where user_id="
		                + user().getUserID() + ");");
		ArrayList<String> blockedFriendsList = new ArrayList<String>();

		while(rs.next()) {
			blockedFriendsList.add(rs.getString("username"));
		}
		for(int i = 0; i < blockedFriendsList.size(); i++) {
			System.out.println("Du har blokeret brugernavnet: " + blockedFriendsList.get(i));

		}
		return blockedFriendsList.toArray(new String[blockedFriendsList.size()]);

	}

	public void acceptFriend(String requestName) throws SQLException, IOException {
		ResultSet rs = con().select("SELECT user_ID FROM users where username='" + requestName + "'");
		String uid = null;
		int user_id = 0;
		while(rs.next()) {
			String em = rs.getString("user_id");
			uid = em.replace("\n", ",");
			user_id = Integer.parseInt(uid);
		}
		con().update("UPDATE contacts SET status=1, friends_since=? WHERE user_id='" + user_id + "' AND contact_id='"
		        + user().getUserID() + "'", new String[][] { { "l", "" + Long.toString(f.timestamp()) } });
	}

	public void rejectFriend(String requestName) throws SQLException {
		ResultSet rs = con().select("SELECT user_ID FROM users where username='" + requestName + "'");
		String uid = null;
		int user_id = 0;
		while(rs.next()) {
			String em = rs.getString("user_id");
			uid = em.replace("\n", ",");
			user_id = Integer.parseInt(uid);
		}
		con().update(
		        "DELETE FROM contacts WHERE user_ID='" + user_id + "' AND contact_ID ='" + user().getUserID() + "'");
	}

	public void cancelRequest(String sentName) throws SQLException {
		ResultSet rs = con().select("SELECT user_ID FROM users where username='" + sentName + "'");
		String uid = null;
		int contact_id = 0;
		while(rs.next()) {
			String em = rs.getString("user_id");
			uid = em.replace("\n", ",");
			contact_id = Integer.parseInt(uid);
		}
		con().update(
		        "DELETE FROM contacts WHERE user_ID='" + user().getUserID() + "' AND contact_ID ='" + contact_id + "'");
	}

	public void blockContact(int ID) throws SQLException, IOException {
		if(!con().check("SELECT user_id,blocked_id FROM blocked_contact WHERE user_id='" + ID + "' AND blocked_id='"
		        + user().getUserID() + "'")) {
			con().update("INSERT INTO blocked_contact (user_ID,blocked_id,blocked_time) VALUES('" + user().getUserID()
			        + "','" + ID + "','" + f.timestamp() + "')");
			deleteFriend(ID);
		}
	}

	public void unBlockContact(int ID) throws SQLException {
		con().update(
		        "DELETE FROM blocked_contact WHERE user_id='" + user().getUserID() + "' AND blocked_id='" + ID + "'");
	}

	public void setAge(int age) throws SQLException {
		con().update("UPDATE users set age=" + age + "WHERE user_id=" + user().getUserID());
	}

	public String[] showSearchAddFriends(String inUser) throws SQLException {
		inUser = inUser.toLowerCase();
		ResultSet rs = con()
		        .select("select username from users where user_id  NOT IN( select user_id from blocked_contact where blocked_id="
		                + user().getUserID() + ") AND user_id!=" + user().getUserID()
		                + " AND (user_id not in((SELECT user_id FROM users WHERE (user_ID = ANY(SELECT user_id FROM contacts WHERE contact_id ="
		                + user().getUserID() + " ) OR user_id = ANY(SELECT contact_id FROM contacts WHERE user_id ="
		                + user().getUserID() + " ))))) AND LOWER(username) LIKE '%" + inUser + "%'");
		ArrayList<String> allUserName = new ArrayList<String>();

		while(rs.next()) {
			allUserName.add(rs.getString("username"));
		}
		for(int i = 0; i < allUserName.size(); i++) {
			System.out.println(allUserName.get(i));

		}
		return allUserName.toArray(new String[allUserName.size()]);
	}

	public int usernameToId(String username) throws SQLException {
		ResultSet rs = con().select("SELECT user_id FROM users WHERE username='" + username + "'");
		rs.next();
		return rs.getInt("user_id");
	}

	public String idToNickname(int id) throws SQLException {
		ResultSet rs = con().select("SELECT nickname FROM users WHERE user_ID=" + id);
		rs.next();
		return rs.getString("nickname");
	}

	public Connector con() {
		return f.getConnector();
	}

	public User user() {
		return f.getUser();
	}
}
