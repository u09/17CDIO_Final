package QuickConnect;

import java.awt.FontFormatException;
import java.io.IOException;
import java.sql.SQLException;

import SceneBuild_JavaFX.loginWindow;

public class Main {
	public static void main(String[] args) throws FontFormatException, IOException, SQLException {
		loginWindow.launch(loginWindow.class, args);
	}
}
