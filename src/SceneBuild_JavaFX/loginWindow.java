package SceneBuild_JavaFX;

import java.io.IOException;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class loginWindow extends Application implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene, myScene2;
	private VBox loginFrame;
	@FXML private Label lTitle, lUser, lPass, lNoUser, lRegister;
	@FXML private Button bLogin;
	@FXML private Hyperlink hClickHere;
	@FXML private TextField inUser;
	@FXML private PasswordField inPass;
	private FunctionUser fu;

	@Override
	public void start(Stage stage) {
		User user = new User();
		Function f = new Function(user);
		this.fu = new FunctionUser(f);
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect");
		this.myStage.setResizable(false);
		showLoginFrame();
		this.myScene = new Scene(loginFrame);

		this.myStage.setScene(myScene);
		this.myStage.show();
	}

	private void showLoginFrame() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(loginWindow.class.getResource("LoginFrame.fxml"));
		loader.setController(this);
		try {
			loginFrame = (VBox) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}

		bLogin.setOnAction(this);
		bLogin.setDefaultButton(true);
		hClickHere.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		// handle for bLogin
		if(event.getSource() == bLogin) {
			String userIn = inUser.getText();
			String passIn = inPass.getText();
			boolean bool = false;
			boolean activated = false;
			try {
				bool = fu.con().check("SELECT username FROM users WHERE UPPER(username) LIKE UPPER(?) AND password=?",
				        userIn, fu.f.md5(passIn));
				System.out.println(bool);
			} catch(SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			if(bool == true) {
				try {
					activated = fu.con().check("SELECT username FROM users WHERE UPPER(username)= UPPER('"
					        + userIn.toUpperCase() + "') AND activated=1");
				} catch(SQLException e2) {
					e2.printStackTrace();
				}
				if(activated) {
					User user = null;
					try {
						ResultSet sql = fu.con().select(
						        "SELECT user_ID,username,email,nickname,age,user_created FROM users WHERE UPPER(username) LIKE UPPER(?) AND password=?",
						        userIn, fu.f.md5(passIn));
						sql.next();
						fu.updateUser(sql.getInt("user_ID"), sql.getString("username"), sql.getString("email"),
						        sql.getString("nickname"), sql.getInt("age"), sql.getInt("user_created"));
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
					ResultSet mail = null;
					try {
						mail = fu.con().select("SELECT email FROM users WHERE UPPER(username) = UPPER('"
						        + userIn.toUpperCase() + "')");
					} catch(SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						mail.next();
					} catch(SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String email = null;
					try {

						email = mail.getString("email");
					} catch(SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					myStage.close();
					Stage stage = new Stage();
					EmailWindow eW = new EmailWindow();
					eW.start(stage, email, userIn, fu);
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
		// handle for hClickHere
		if(event.getSource() == hClickHere) {
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
