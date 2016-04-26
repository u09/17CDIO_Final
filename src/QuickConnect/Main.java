package QuickConnect;

import java.awt.FontFormatException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import SceneBuild_JavaFX.loginWindow;
import javafx.application.Application;

public class Main {
	public static void main(String[] args) throws FontFormatException, IOException, SQLException {
		ArrayList<String> g=FunctionUser.showGroups(1);
		for(int i=1;i<=g.size();i++) System.out.println(g.get(i-1));
		System.out.println("tesT");
		Application.launch(loginWindow.class, args);
	}
}
