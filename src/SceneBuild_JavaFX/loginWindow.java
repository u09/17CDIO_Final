package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import QuickConnect.Connector;
import QuickConnect.Function;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class loginWindow extends Application implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private VBox LoginFrame;
	FXMLLoader loader;
	@FXML
	Label lTitle, lUser, lPass, lNoUser, lRegister;
	@FXML
	Button bLogin, bRegister;
	@FXML
	TextField inUser;
	@FXML
	PasswordField inPass;

	@Override
	public void start(Stage stage) {
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect");
		this.myStage.setResizable(false);

		showLoginFrame();
		
		myScene = new Scene(LoginFrame);
		
		File file = new File("src/SceneBuild_JavaFX/standardLayout.css");
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

	private void showLoginFrame() {
		
		loader = new FXMLLoader();
		loader.setLocation(loginWindow.class.getResource("LoginFrame.fxml"));
		loader.setController(this);
		try {
			LoginFrame = (VBox) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		bLogin.setOnAction(this);
		bRegister.setOnAction(this);
		lTitle.getStyleClass().add("titles");
	}

	@Override
	public void handle(ActionEvent event) {
		// handle for bLogin
		if(event.getSource() == bLogin) {
			String userIn = inUser.getText();
			String passIn = inPass.getText();
			Connector con = Function.mysql();
			boolean bool = false;
			try {
				bool = con.check("SELECT username FROM users WHERE UPPER(username) LIKE UPPER(?) AND password=?", userIn,
				        Function.md5(passIn));
				System.out.println(bool);
			} catch(SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			if(bool == true) {
				Stage stage = new Stage();
				chatWindow cW = new chatWindow();

				try {
					cW.start(stage);
				} catch(Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				((Node) (event.getSource())).getScene().getWindow().hide();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Forkert kode");
				alert.setHeaderText("Forkert kodeord eller brugernavn");
				alert.setContentText("Venligst indtast dit kodeord og brugernavn igen");

				alert.showAndWait();
				inPass.setText("");
				inUser.requestFocus();
			}
		}
		// handle for bRegister
		if(event.getSource() == bRegister) {
			Stage stage = new Stage();
			registerWindow Rf = new registerWindow();
			try {
				Rf.start(stage);
			} catch(Exception e) {
				e.printStackTrace();
			}
			((Node) (event.getSource())).getScene().getWindow().hide();
		}

	}

}
