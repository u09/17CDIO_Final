package QuickConnect;

import java.awt.FontFormatException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import SceneBuild_JavaFX.loginWindow;
import javafx.application.Application;

public class Main {
	public static void main(String[] args) throws FontFormatException, IOException, SQLException {
		String[] g=FunctionUser.showGroups(1);
		for(int i=1;i<=g.length;i++) System.out.println(g[i-1]);
		Application.launch(loginWindow.class, args);
	}
}