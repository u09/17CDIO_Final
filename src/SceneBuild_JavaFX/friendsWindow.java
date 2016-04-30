package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import QuickConnect.FunctionUser;
import QuickConnect.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class friendsWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private AnchorPane friendsFrame;
	@FXML private TextField inUsername;
	@FXML private Button bAdd, bAccept, bReject;
	@FXML private ListView<String> sentList, receivedList;
	private User user;

	public void start(Stage stage, User user) throws Exception {
		this.user = user;
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

		FXMLLoader loader = new FXMLLoader();
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
		EventHandler<KeyEvent> keyEvent = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getSource() == inUsername && event.getCode().equals(KeyCode.ENTER))
					bAdd.fire();
			}
		};

		inUsername.setOnKeyPressed(keyEvent);
		bAdd.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == bAdd) {
			try {
				int i = FunctionUser.addFriend(user.UserID, inUsername.getText());
				if(i == 1) {
					Alert passSuccess = new Alert(AlertType.INFORMATION);
					passSuccess.setTitle(myStage.getTitle());
					passSuccess.setHeaderText("Din venneanmodning er sendt");
					passSuccess.setContentText("Du har nu sendt en venneanmodning til " + inUsername.getText());
					passSuccess.show();
				} else {
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
