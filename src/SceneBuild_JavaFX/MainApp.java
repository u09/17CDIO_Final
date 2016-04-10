package SceneBuild_JavaFX;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import QuickConnect.Connector;
import QuickConnect.Function;
import QuickConnect_GUI_JavaFX.programFrame;
import QuickConnect_GUI_JavaFX.registerFrame;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application implements EventHandler<ActionEvent>{

	private Stage primaryStage;
	private BorderPane rootLayout;
	FXMLLoader loader;
	@FXML MenuItem about, close, settings, logOut, fullScreen, exitFullScreen;
	@FXML ListView<String> recentList, friendsOnlineList, friendsOfflineList, groupsList;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("QuickConnect");
		
		initRootLayout();
		showProgramOverview();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
			loader.setController(this);
			rootLayout = (BorderPane) loader.load();
			
			close.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
			close.setOnAction((ActionEvent t) -> {
				System.exit(0);
			});
			
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the person overview inside the root layout.
	 */
	public void showProgramOverview() {
		try {
			// Load person overview.
			loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("programFrame.fxml"));
			loader.setController(this);
			BorderPane programFrame = (BorderPane) loader.load();

			ObservableList<String> items = FXCollections.observableArrayList("Ahmad", "Ibrahim", "Samil", "Tolga",
			        "Harun", "Umais", "Lars", "Hans", "Peter", "Søren", "Gurli", "Lars", "Hans", "Peter");
			recentList.setItems(items);

			ObservableList<String> onlineItems = FXCollections.observableArrayList("Online1", "Online2", "Online3",
			        "Online4", "Online5", "Online10", "Online20", "Online30", "Online40", "Online50");
			friendsOnlineList.setItems(onlineItems);

			ObservableList<String> offlineItems = FXCollections.observableArrayList("Offline1", "Offline2", "Offline3",
			        "Offline4", "Offline5", "Offline10", "Offline20", "Offline30", "Offline40", "Offline50");
			friendsOfflineList.setItems(offlineItems);

			ObservableList<String> groupsItems = FXCollections.observableArrayList("Group1", "Group2", "Group3",
			        "Group4", "Group5", "Group10", "Group20", "Group30", "Group40", "Group50");
			groupsList.setItems(groupsItems);

			// Set person overview into the center of root layout.
			rootLayout.setCenter(programFrame);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	@Override
	public void handle(ActionEvent event) {
		
	}
}