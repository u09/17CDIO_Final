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
import javafx.scene.input.MouseEvent;
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
		
		String[] rec = {"Recent1", "Recent2"};
		ObservableList<String> items = FXCollections.observableArrayList(rec);
		recentList.setItems(items);
		
		String[] onl = {"Online1", "Online2"};
		ObservableList<String> onlineItems = FXCollections.observableArrayList(onl);
		friendsOnlineList.setItems(onlineItems);

		String[] off = {"Offline1", "Offline2"};
		ObservableList<String> offlineItems = FXCollections.observableArrayList(off);
		friendsOfflineList.setItems(offlineItems);

		String[] gro = {"Group1", "Group2"};
		ObservableList<String> groupsItems = FXCollections.observableArrayList(gro);
		groupsList.setItems(groupsItems);
	}

	private void setMenuBarFunctions() {

		menuBar.setUseSystemMenuBar(true);
		about.setOnAction(this);
		close.setOnAction(this);
		close.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
		settings.setOnAction(this);
		
	}
	
	private void setListsFunctions() {
		
		recentList.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	            System.out.println("clicked on " + recentList.getSelectionModel().getSelectedItem());
	        }
	    });
		
		friendsOnlineList.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	            System.out.println("clicked on " + friendsOnlineList.getSelectionModel().getSelectedItem());
	        }
	    });
		
		friendsOfflineList.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	            System.out.println("clicked on " + friendsOfflineList.getSelectionModel().getSelectedItem());
	        }
	    });
		
		groupsList.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	            System.out.println("clicked on " + groupsList.getSelectionModel().getSelectedItem());
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