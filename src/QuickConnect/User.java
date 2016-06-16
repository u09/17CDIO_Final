package QuickConnect;

public class User {
	private int UserID;
	private String Username;
	private String Email;
	private String Nickname;
	private int Age;
	private int Created;

	public User() {

	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	public int getCreated() {
		return Created;
	}

	public void setCreated(int created) {
		Created = created;
	}
}
