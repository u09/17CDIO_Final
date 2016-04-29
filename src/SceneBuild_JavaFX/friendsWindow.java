package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import QuickConnect.FunctionUser;
import QuickConnect.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class friendsWindow implements EventHandler<ActionEvent> {

	Stage myStage;
	Scene myScene;
	AnchorPane friendsFrame;
	FXMLLoader loader;
	@FXML TextField inUsername;
	@FXML Button bAdd, bAccept, bReject;
	User user;
	chatWindow cW;

	public void start(Stage stage, User user) throws Exception {
		this.user = user;
		this.cW = null;
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - Anmodninger");
		this.myStage.setResizable(false);
		showFriendsFrame();

		myScene = new Scene(friendsFrame);

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

	private void showFriendsFrame() {

		loader = new FXMLLoader();
		loader.setLocation(friendsWindow.class.getResource("FriendsFrame.fxml"));
		loader.setController(this);
		try {
			friendsFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}

		setButtonFunctions();

	}

	private void setButtonFunctions() {
		EventHandler<KeyEvent> haha = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getSource() == inUsername && event.getCode().equals(KeyCode.ENTER)) bAdd.fire();
			}
		};
		
		inUsername.setOnKeyPressed(haha);
		bAdd.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == bAdd) {
			try {
				int i =FunctionUser.addFriend(user.UserID,inUsername.getText());
				if(i ==1){
				Alert passSuccess = new Alert(AlertType.INFORMATION);
				passSuccess.setTitle(myStage.getTitle());
				passSuccess.setHeaderText("Din venneanmodning er sendt");
				passSuccess.setContentText("Du har nu sendt en venneanmodning til " + inUsername.getText());
				passSuccess.show();
				}
				else{
					Alert passSuccess = new Alert(AlertType.INFORMATION);
					passSuccess.setTitle(myStage.getTitle());
					passSuccess.setHeaderText("Du har allerede sendt en venneanmodning");
					passSuccess.setContentText("Venligst tjek dine kontakter eller vent til at personen accepterer");
					passSuccess.show();
				}
			} catch(SQLException SQLe) {
				Alert passSuccess = new Alert(AlertType.INFORMATION);
				passSuccess.setTitle(myStage.getTitle());
				passSuccess.setHeaderText("Brugernavnet eksisterer ikke");
				passSuccess.setContentText("Venligst indtast et gyldigt brugernavn");
				passSuccess.show();
			}
		}
	}
}