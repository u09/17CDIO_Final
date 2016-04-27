package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import QuickConnect.Connector;
import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class settingsWindow implements EventHandler<ActionEvent> {

	Stage myStage;
	Scene myScene;
	AnchorPane settingsFrame;
	FXMLLoader loader;
	@FXML TextField inNickname, inCurrentPass, inNewPass, inNewPass2,inCurrentPass2;
	@FXML Button bSaveNickname, bSavePass, bDeleteUser;
	User user;

	public void start(Stage stage, User user) throws Exception {
		this.user = user;
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - Settings");
		this.myStage.setResizable(false);

		showSettingsFrame();

		myScene = new Scene(settingsFrame);

		File file = new File("src/SceneBuild_JavaFX/StandardLayout.css");
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

	private void showSettingsFrame() {

		loader = new FXMLLoader();
		loader.setLocation(settingsWindow.class.getResource("SettingsFrame.fxml"));
		loader.setController(this);
		try {
			settingsFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}

		setButtonFunctions();

	}

	private void setButtonFunctions() {

		bSaveNickname.setOnAction(this);
		bSaveNickname.setDefaultButton(true);
		bSavePass.setOnAction(this);
		bSavePass.setDefaultButton(true);
		bDeleteUser.setOnAction(this);
		bDeleteUser.setDefaultButton(true);

	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == bSaveNickname) {
			int changeNicknameAnswer = 0;
			changeNicknameAnswer = FunctionUser.changeNickname(inNickname.getText(), user.UserID);
			
			if(changeNicknameAnswer == 0) {
//				System.out.println(FunctionUser.getNickName(user.UserID));
				Alert nicknameSucces = new Alert(AlertType.INFORMATION);
				nicknameSucces.setTitle(myStage.getTitle());
				nicknameSucces.setHeaderText("Nickname blev sat!");
				nicknameSucces.setContentText("Dit nickname er blevet ændret til: " + inNickname.getText());
				nicknameSucces.show();
			}
			else {
				Alert nicknameFail = new Alert(AlertType.ERROR);
				nicknameFail.setTitle(myStage.getTitle());
				nicknameFail.setHeaderText("Nickname blev ikke sat!");
				nicknameFail.setContentText("Nicknamet må maks være på 15 tegn.");
				nicknameFail.show();
			}
		}
		if(event.getSource() == bSavePass) {
			int changePassAnswer = 0;
			try {
				changePassAnswer = FunctionUser.changePassword(user.UserID, inCurrentPass.getText(),
				        inNewPass.getText(), inNewPass2.getText());
			} catch(SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			if(changePassAnswer == 0) {
				Alert passSuccess = new Alert(AlertType.INFORMATION);
				passSuccess.setTitle(myStage.getTitle());
				passSuccess.setHeaderText("Ændring af password lykkedes!");
				passSuccess.setContentText("Dit password er blevet ændret");
				passSuccess.show();
			} else {
				String message;
				if(changePassAnswer == 1)
					message = "Begge passwords skal være ens.";
				else if(changePassAnswer == 2)
					message = "Passwordet skal være mellem 8-24 tegn.";
				else if(changePassAnswer == 3)
					message = "Passwordet skal mindst indeholde \n1 stort bogstav, 1 lille bogstav og 1 tal.";
				else message = "Passwordet må kun indeholde tal, bogstaver og følgende tegn: \n <h2>!\"#$%&'(,)*+-./:;<=>?@[\\]^_`{|}~</h2>";
				Alert passFail = new Alert(AlertType.ERROR);
				passFail.setTitle(myStage.getTitle());
				passFail.setHeaderText("Ændring af password mislykkedes!");
				passFail.setContentText(message);
				passFail.show();
			}
		}
		if(event.getSource() == bDeleteUser){
			int deactivateUserAnswer = 0;
			try{
				deactivateUserAnswer = FunctionUser.deactivateUser(user.UserID,inCurrentPass2.getText());
			}
			catch(SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if(deactivateUserAnswer == 0){
				Alert deactivateSuccess = new Alert(AlertType.INFORMATION);
				deactivateSuccess.setTitle(myStage.getTitle());
				deactivateSuccess.setHeaderText("Deaktivering af bruger lykkedes");
				deactivateSuccess.setContentText("Din bruger bliver nu deaktiveret, indtil du igen logger p�");
				deactivateSuccess.show();
				((Node) event.getSource()).getScene().getWindow().hide();
			}
			else if(deactivateUserAnswer == 1){
				Alert deactivateFail = new Alert(AlertType.INFORMATION);
				deactivateFail.setHeaderText("Deaktivering af bruger mislykkedes");
				deactivateFail.setContentText("Forkert password");
				deactivateFail.show();
			}
		}
	}
}