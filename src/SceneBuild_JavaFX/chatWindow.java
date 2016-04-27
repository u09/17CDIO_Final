package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.Threads;
import QuickConnect.User;
import javafx.application.Application;
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
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class chatWindow implements EventHandler<ActionEvent>{

	private Stage myStage;
	private Scene myScene;
	private BorderPane chatFrame;
	FXMLLoader loader;
	@FXML
	MenuBar menuBar;
	@FXML
	MenuItem about, close, settings, signOut, fullScreen, exitFullScreen;
	@FXML
	ListView<String> recentList, friendsOnlineList, friendsOfflineList, groupsList;
	@FXML
	TitledPane titledPane;
	@FXML
	TextArea textArea;
	@FXML
	TextField inMessage;
	@FXML
	Button bEmojis, bSend, bSearchRecent, bSearchFriends, bSearchGroups;
	@FXML
	HBox hBoxMessage;
	User user;

	public void start(Stage stage, User user) throws SQLException {
		this.user = user;
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - user: " + user.Username);

		showChatFrame();

		myScene = new Scene(chatFrame);

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
		Runnable r = new Threads(user);
		new Thread(r).start();
	}

	public void showChatFrame() throws SQLException {
		loader = new FXMLLoader();
		loader.setLocation(chatWindow.class.getResource("ChatFrame.fxml"));
		loader.setController(this);
		try {
			chatFrame = (BorderPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		titledPane.setText("Chat system");
		hBoxMessage.setHgrow(inMessage, Priority.ALWAYS);
		bSearchRecent.setId("bSearch");
		bSearchFriends.setId("bSearch");
		bSearchGroups.setId("bSearch");
		bEmojis.setId("bEmoji");
		setMenuBarFunctions();
		getListsContents();
		setListsFunctions();
	}

	private void setMenuBarFunctions() {
		menuBar.setUseSystemMenuBar(true);
		about.setOnAction(this);
		close.setOnAction(this);
		close.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
		settings.setOnAction(this);
		settings.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
		signOut.setOnAction(this);
		signOut.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
		fullScreen.setOnAction(this);
		fullScreen.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
		exitFullScreen.setOnAction(this);
		exitFullScreen.setAccelerator(KeyCombination.keyCombination("Esc"));
		exitFullScreen.setDisable(true);

	}

	private void getListsContents() throws SQLException {
		String[] rec = { "Recent1", "Recent2" };
		ObservableList<String> items = FXCollections.observableArrayList(rec);
		recentList.setItems(items);
		
		String[] onl = { "Online1", "Online2" };
		ObservableList<String> onlineItems = FXCollections.observableArrayList(onl);
		friendsOnlineList.setItems(onlineItems);

		String[] off = { "Offline1", "Offline2" };
		ObservableList<String> offlineItems = FXCollections.observableArrayList(off);
		friendsOfflineList.setItems(offlineItems);

		String[] gro = FunctionUser.showGroups(user.UserID);// {"Group1",
		                                                    // "Group2"};
		ObservableList<String> groupsItems = FXCollections.observableArrayList(gro);
		groupsList.setItems(groupsItems);

	}

	private void setListsFunctions() {
		recentList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String name = recentList.getSelectionModel().getSelectedItem();
				System.out.println("clicked on " + name);
				titledPane.setText(name);
			}
		});

		friendsOnlineList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String name = recentList.getSelectionModel().getSelectedItem();
				System.out.println("clicked on " + name);
				titledPane.setText(name);
			}
		});

		friendsOfflineList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String name = friendsOfflineList.getSelectionModel().getSelectedItem();
				System.out.println("clicked on " + name);
				titledPane.setText(name);
			}
		});

		groupsList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String name = groupsList.getSelectionModel().getSelectedItem();
				System.out.println("clicked on " + name);
				titledPane.setText(name);
			}
		});
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == about) {

		}
		if(event.getSource() == close) {
			System.exit(0);
		}
		if(event.getSource() == settings) {
			Stage stage = new Stage();
			settingsWindow sF = new settingsWindow();
			try {
				sF.start(stage, user);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(event.getSource() == signOut) {
			
			Alert confirmSignOut = new Alert(AlertType.CONFIRMATION);
			DialogPane x = confirmSignOut.getDialogPane();
			x.getStylesheets().add(getClass().getResource("StandardLayout.css").toExternalForm());
			
			confirmSignOut.setTitle("Log ud");
			confirmSignOut.setHeaderText("Du er ved at logge ud af QuickConnect");
			confirmSignOut.setContentText("Er du sikker på at du vil logge ud?");
			
			ButtonType bSignOut = new ButtonType("Log ud", ButtonData.OK_DONE);
			ButtonType bCancel = new ButtonType("Annullér", ButtonData.NO);
			bCancel.getButtonData().isCancelButton();
			confirmSignOut.getButtonTypes().setAll(bSignOut, bCancel);
			
			Optional<ButtonType> result = confirmSignOut.showAndWait();
			if(result.get() == bSignOut) {
				System.out.println("Trykket på Log ud");
			} else {
				confirmSignOut.close();
			}
		}
		if(event.getSource() == fullScreen) {
			myStage.setFullScreen(true);
			exitFullScreen.setDisable(false);
		}
		if(event.getSource() == exitFullScreen) {
			myStage.setFullScreen(false);
			exitFullScreen.setDisable(true);
		}
	}

	public Stage getPrimaryStage() {
		return myStage;
	}
}