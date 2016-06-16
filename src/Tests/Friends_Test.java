package Tests;

import java.io.IOException;
import java.sql.SQLException;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;

public class Friends_Test {

	public static void main(String[] args) throws SQLException, IOException{
		User u = new User();
		Function f = new Function(u);
		FunctionUser fu = new FunctionUser(f);
		fu.con().update("DELETE FROM contacts WHERE user_ID=1 AND contact_ID=2");
		u.setUserID(1);

		fu.addFriend("test2");
		if(fu.getAllFriendsNickname().length == 0) System.out.println("ingen venner");
		else{
			for(int i = 0; i<fu.getAllFriendsNickname().length; i++){
				System.out.println(fu.getAllFriendsNickname()[i] + " Venneliste uden accept");
			}
		}
		
		u.setUserID(2);
		fu.acceptFriend("test1");

		u.setUserID(1);
		for(int i = 0; i<fu.getAllFriendsNickname().length; i++){
			System.out.println(fu.getAllFriendsNickname()[i] + " Vennerliste med accept");
		}
	}
}
