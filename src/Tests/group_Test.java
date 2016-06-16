package Tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;

public class group_Test {
	static User u = new User();
	static Function f = new Function(u);
	static FunctionUser fu = new FunctionUser(f);
	public static void main(String[] args) throws SQLException, IOException {
		ArrayList<Integer> allFriendsId = new ArrayList<>();
		
		fu.con().update("TRUNCATE TABLE group_members");
		fu.con().update("TRUNCATE TABLE group_messages");
		fu.con().update("DELETE FROM groups WHERE group_id>0");
		fu.con().update("ALTER TABLE groups AUTO_INCREMENT = 1");

		u.setUserID(1);
		allFriendsId.add(2);
		fu.createGroup(u.getUserID(), "TestGruppe", allFriendsId);
		System.out.println("Gruppe navnet er: "+fu.getGroupsNames()[0]);

		fu.sendGroupMessage("Testbesked ", 1);
		u.setUserID(2);
		fu.sendGroupMessage("Testbesked 2", 1);
		
		System.out.println("Beskeder i gruppe 1: " + test() +"\n");
		for(int i= 0; i<fu.getAllMembersNickname(1).length; i++)
			System.out.println("Medlemmer af gruppe 1 uden gruppeejeren: "+fu.getAllMembersNickname(1)[i]);
	}
	
	public static String test(){
	      String result = "";
	      try {
			for(int i = 0; i < fu.getGroupMessages(1,0).size(); i++){
			      for(int j = 0; j < fu.getGroupMessages(1,0).get(i).size(); j++){
			          result += fu.getGroupMessages(1,0).get(i).get(j);
			      }
			      result += "\n";
			  }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	      return result;
	  }


}
