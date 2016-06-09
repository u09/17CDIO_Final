package SceneBuild_JavaFX;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;

import QuickConnect.FunctionUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class settingsWindow extends chatWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private AnchorPane settingsFrame;
	@FXML private TextField inNickname;
	@FXML private PasswordField inCurrentPass, inNewPass, inNewPass2, inCurrentPass2;
	@FXML private Button bSaveNickname, bSavePass, bDeleteUser;
	@FXML private ListView blockedList;
	@FXML private ContextMenu rightClick;
	@FXML private MenuItem unBlock;
	private String[] blocked;
	private FunctionUser fu;

	@Override
	public void start(Stage stage, FunctionUser fu) {
		this.fu = fu;
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - Settings");
		this.myStage.setResizable(false);
		showSettingsFrame();

		myScene = new Scene(settingsFrame);

		this.myStage.setScene(myScene);
		this.myStage.show();
	}

	private void showSettingsFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(settingsWindow.class.getResource("SettingsFrame.fxml"));
		loader.setController(this);
		try {
			settingsFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}

		setButtonFunctions();
		try {
			blockedListView();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unchecked")
	private void blockedListView() throws SQLException{	
		this.blocked=fu.getBlockedFriendsList();
		ObservableList<String> Items = FXCollections.observableArrayList(this.blocked);
		blockedList.setItems(Items);
		unBlock.setText("Fjern blokeringen");
		blockedList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				int id = blockedList.getSelectionModel().getSelectedIndex();
				if(id==-1) return;
				String name = (String) blockedList.getSelectionModel().getSelectedItem();
				
				unBlock.setOnAction(new EventHandler<ActionEvent>() {
					
				
					@Override
					public void handle(ActionEvent event){
						
						try {
							fu.unBlockContact(fu.usernameToID(name));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Items.remove(blockedList.getSelectionModel().getSelectedItem());
					}
				});
			}
		});

		//		System.out.println(blockedList.getSelectionModel().getSelectedItem());
		//		String name = (String) blockedList.getSelectionModel().getSelectedItem();
		//		System.out.println(name);
		//		int id = fu.usernameToID(name);
		//		System.out.println(name);
		//		unBlock.setText("Fjern blokeringen");
		//		unBlock.setOnAction(new EventHandler<ActionEvent>() {
		//			@Override
		//			public void handle(ActionEvent event) {
		//				try {
		//					fu.unBlockContact(id);
		//				} catch (SQLException e) {
		//					// TODO Auto-generated catch block
		//					e.printStackTrace();
		//				}
		//			}
		//		});
	}

	private void setButtonFunctions() {
		EventHandler<KeyEvent> keyEvent = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getSource() == inNickname && event.getCode().equals(KeyCode.ENTER))
					bSaveNickname.fire();
				if(event.getSource() == inCurrentPass && event.getCode().equals(KeyCode.ENTER))
					bSavePass.fire();
				if(event.getSource() == inNewPass && event.getCode().equals(KeyCode.ENTER))
					bSavePass.fire();
				if(event.getSource() == inNewPass2 && event.getCode().equals(KeyCode.ENTER))
					bSavePass.fire();
				if(event.getSource() == inCurrentPass2 && event.getCode().equals(KeyCode.ENTER))
					bDeleteUser.fire();
			}
		};

		inNickname.setOnKeyPressed(keyEvent);
		bSaveNickname.setOnAction(this);
		inCurrentPass.setOnKeyPressed(keyEvent);
		inNewPass.setOnKeyPressed(keyEvent);
		inNewPass2.setOnKeyPressed(keyEvent);
		bSavePass.setOnAction(this);
		inCurrentPass2.setOnKeyPressed(keyEvent);
		bDeleteUser.setOnAction(this);

	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == bSaveNickname) {
			int changeNicknameAnswer = 0;
			changeNicknameAnswer = fu.changeNickname(inNickname.getText());

			if(changeNicknameAnswer == 0) {
				// System.out.println(FunctionUser.getNickName(user.UserID));
				Alert nicknameSucces = new Alert(AlertType.INFORMATION);
				nicknameSucces.setTitle(myStage.getTitle());
				nicknameSucces.setHeaderText("Nickname blev sat!");
				nicknameSucces.setContentText("Dit nickname er blevet ændret til: " + inNickname.getText());
				nicknameSucces.show();
			} else {
				Alert nicknameFail = new Alert(AlertType.ERROR);
				nicknameFail.setTitle(myStage.getTitle());
				nicknameFail.setHeaderText("Nickname blev ikke sat!");
				nicknameFail.setContentText("Nicknamet skal være mellem 1 og 40 tegn,\nog må ikke kun indeholde mellemrum.");
				nicknameFail.show();
			}
		}
		if(event.getSource() == bSavePass) {
			int changePassAnswer = 0;
			try {
				changePassAnswer = fu.changePassword(inCurrentPass.getText(), inNewPass.getText(), inNewPass2.getText());
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
				else if(changePassAnswer == 4)
					message = "Det nuværende password er ikke korrekt";
				else message = "Passwordet må kun indeholde tal, bogstaver og følgende tegn: \n <h2>!\"#$%&'(,)*+-./:;<=>?@[\\]^_`{|}~</h2>";
				Alert passFail = new Alert(AlertType.ERROR);
				passFail.setTitle(myStage.getTitle());
				passFail.setHeaderText("Ændring af password mislykkedes!");
				passFail.setContentText(message);
				passFail.show();
			}
		}
		if(event.getSource() == bDeleteUser) {
			int deactivateUserAnswer = 0;
			try {
				deactivateUserAnswer = fu.deactivateUser(inCurrentPass2.getText());
			} catch(SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if(deactivateUserAnswer == 0) {

				ButtonType bLogW = new ButtonType("Login", ButtonData.OK_DONE);
				ButtonType bClose = new ButtonType("Luk", ButtonData.NO);
				Alert deactivateSuccess = new Alert(AlertType.CONFIRMATION, null, bLogW, bClose);
				deactivateSuccess.initOwner(myStage);
				deactivateSuccess.setTitle("QuickConnect - Deaktivering");
				deactivateSuccess.setHeaderText("Deaktivering lykkedes!");
				deactivateSuccess.setContentText("Din bruger bliver nu deaktiveret, indtil du igen logger på.\nVil du gå til loginsiden eller lukke QuickConnect.");

				Optional<ButtonType> result = deactivateSuccess.showAndWait();

				if(result.get() == bLogW) {
					th.interrupt(); // Threaden stopper ikke når denne kodes køres, resten virker
					try {
						fu.setOfflineUser();
					} catch(SQLException e) {
						e.printStackTrace();
					}
					myStage.close();
					myStage.getOwner().hide();

					Stage stage = new Stage();
					loginWindow lW = new loginWindow();
					try {
						lW.start(stage);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						fu.setOfflineUser();
					} catch(SQLException e1) {
						e1.printStackTrace();
					}
					System.exit(0);;
				}
			} else if(deactivateUserAnswer == 1) {
				Alert deactivateFail = new Alert(AlertType.ERROR);
				deactivateFail.setHeaderText("Deaktivering mislykkedes!");
				deactivateFail.setContentText("Forkert password.\nIndtast dit password igen.");
				deactivateFail.showAndWait();
				inCurrentPass2.clear();
				inCurrentPass2.requestFocus();
			}
		}
	}
}
