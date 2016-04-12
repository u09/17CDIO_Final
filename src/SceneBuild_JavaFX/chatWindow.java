package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class chatWindow extends Application implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private BorderPane chatFrame;
	FXMLLoader loader;
	@FXML
	MenuBar menuBar;
	@FXML
	TextArea textArea;
	@FXML
	MenuItem about, close, settings, logOut, fullScreen, exitFullScreen;
	@FXML
	ListView<String> recentList, friendsOnlineList, friendsOfflineList, groupsList;

	@Override
	public void start(Stage stage) {
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - user: ");

		showChatFrame();

		myScene = new Scene(chatFrame);

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

	public void showChatFrame() {

		loader = new FXMLLoader();
		loader.setLocation(chatWindow.class.getResource("ChatFrame.fxml"));
		loader.setController(this);
		try {
			chatFrame = (BorderPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}

		setMenuBarFunctions();

		ObservableList<String> items = FXCollections.observableArrayList("Ahmad", "Ibrahim", "Samil", "Tolga", "Harun",
		        "Umais", "Lars", "Hans", "Peter", "Søren", "Gurli", "Lars", "Hans", "Peter");
		recentList.setItems(items);

		ObservableList<String> onlineItems = FXCollections.observableArrayList("Online1", "Online2", "Online3",
		        "Online4", "Online5", "Online10", "Online20", "Online30", "Online40", "Online50");
		friendsOnlineList.setItems(onlineItems);

		ObservableList<String> offlineItems = FXCollections.observableArrayList("Offline1", "Offline2", "Offline3",
		        "Offline4", "Offline5", "Offline10", "Offline20", "Offline30", "Offline40", "Offline50");
		friendsOfflineList.setItems(offlineItems);

		ObservableList<String> groupsItems = FXCollections.observableArrayList("Group1", "Group2", "Group3", "Group4",
		        "Group5", "Group10", "Group20", "Group30", "Group40", "Group50");
		
		groupsList.setItems(groupsItems);
	}

	private void setMenuBarFunctions() {

		menuBar.setUseSystemMenuBar(true);
		about.setOnAction(this);
		close.setOnAction(this);
		close.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
		settings.setOnAction(this);
		
		String text1 = "Dette er en TextArea. Vi kunne bruge den til at samtalen.\n\n";
		String text2 = "Alt dette er en <Strings>.\n\n";
		String text3 = "Man kan ikke ændre ved skrifttypen og størrelsen og tykkelsen osv. fordi det er String.\n\n";
		String text4 = "Så vi skal nok bruge TextFlow i stedet for TextArea.\n\n";
	    textArea.appendText(text1 + text2 + text3 + text4);
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
				sF.start(stage);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(event.getSource() == logOut) {

		}
		if(event.getSource() == fullScreen) {

		}
		if(event.getSource() == exitFullScreen) {

		}
	}

	public Stage getPrimaryStage() {
		return myStage;
	}
}