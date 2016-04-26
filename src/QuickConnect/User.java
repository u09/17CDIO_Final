package QuickConnect;

public class User {
	public int UserID;
	public String Username;
	public String Email;
	public String Nickname;
	public int Age;
	public int Created;
	
	public User(int id,String user,String mail,String name,int age,int created){
		this.UserID=id;
		this.Username=user;
		this.Email=mail;
		this.Nickname=name;
		this.Age=age;
		this.Created=created;
	}
}
