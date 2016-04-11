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

public class registerWindow extends Application implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private VBox RegisterFrame;
	FXMLLoader loader;
	@FXML
	Label lTitle, lUser, lPass, lNewPass, lMail, lRegister;
	@FXML
	Button bRegister, bBack;
	@FXML
	TextField inUser;
	@FXML
	PasswordField inPass;

	@Override
	public void start(Stage stage) {
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect- Register");
		this.myStage.setResizable(false);

		showRegisterFrame();
		
		myScene = new Scene(RegisterFrame);
		
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

	private void showRegisterFrame() {
		
		loader = new FXMLLoader();
		loader.setLocation(registerWindow.class.getResource("RegisterFrame.fxml"));
		loader.setController(this);
		try {
			RegisterFrame = (VBox) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
//		bLogin.setOnAction(this);
		bRegister.setOnAction(this);
		lTitle.getStyleClass().add("titles");
	}

	@Override
	public void handle(ActionEvent event) {
		
	}

}
