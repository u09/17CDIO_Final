package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmailFrame extends Application implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private VBox EmailFrame;
	@FXML private Label lCode;
	@FXML private Button bAccept, bSend;
	@FXML private TextField inCode;
	private FunctionUser fu;
	
	@Override
	public void start(Stage stage) {
		User user = new User();
		Function f = new Function(user);
		this.fu = new FunctionUser(f);
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect");
		this.myStage.setResizable(false);
		showEmailFrame();
		this.myScene = new Scene(EmailFrame);

		File file = new File("QuickConnectCSS/StandardLayout.css");
		URL url;
		try {
			url = file.toURI().toURL();
			myScene.getStylesheets().add(url.toExternalForm());
		} catch(MalformedURLException e) {
			e.printStackTrace();
		}
		this.myStage.setScene(myScene);
		this.myStage.show();
	}

	private void showEmailFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(EmailFrame.class.getResource("EmailFrame.fxml"));
		loader.setController(this);
		try {
			EmailFrame = (VBox) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		bSend.setOnAction(this);
		bAccept.setDefaultButton(true);
		bRegister.setOnAction(this);
		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81 };
		String emojiAsString = new String(emojiBytes, Charset.forName("UTF-8"));
		inUser.setText(emojiAsString);
	}

	@Override
	public void handle(ActionEvent event) {
		// handle for bLogin
		if(event.getSource() == bLogin) {
			String userIn = inUser.getText();
			String passIn = inPass.getText();
			boolean bool = false;
			try {
				bool = fu.con().check("SELECT username FROM users WHERE UPPER(username) LIKE UPPER(?) AND password=?", userIn, fu.f.md5(passIn));
				System.out.println(bool);
			} catch(SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			if(bool == true) {
				User user = null;
				try {
					ResultSet sql = fu.con().select(
					        "SELECT user_ID,username,email,nickname,age,user_created FROM users WHERE UPPER(username) LIKE UPPER(?) AND password=?", userIn, fu.f.md5(passIn));
					sql.next();
					fu.updateUser(sql.getInt("user_ID"), sql.getString("username"), sql.getString("email"), sql.getString("nickname"), sql.getInt("age"), sql.getInt("user_created"));
				} catch(NoSuchAlgorithmException | SQLException e1) {
					e1.printStackTrace();
				}
				myStage.close();
				Stage stage = new Stage();
				chatWindow cW = new chatWindow();
				try {
					cW.start(stage, fu);
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				Alert loginFail = new Alert(AlertType.ERROR);
				loginFail.setTitle(this.myStage.getTitle());
				loginFail.setHeaderText("Login mislykkedes!");
				loginFail.setContentText("Venligst indtast dit brugernavn og password igen");
				loginFail.showAndWait();
				inPass.clear();
				inUser.requestFocus();
			}
		}
		// handle for bRegister
		if(event.getSource() == bRegister) {
			Stage stage = new Stage();
			registerWindow rW = new registerWindow();
			myStage.close();
			try {
				rW.start(stage, fu);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

	}

}