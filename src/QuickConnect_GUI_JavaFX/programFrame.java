package QuickConnect_GUI_JavaFX;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class programFrame extends Application implements EventHandler<ActionEvent> {

	String username;
	String password;

	public static void main(String[] args) {
		launch(programFrame.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		stage.setTitle("QuickConnect - user: " + username);

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(addHBox());
		borderPane.setLeft(addTabs());

		Scene scene = new Scene(borderPane, 1000, 700);

		File file = new File("src/QuickConnect_GUI_JavaFX/standardLayout.css");
		URL url = file.toURI().toURL();
		scene.getStylesheets().add(url.toExternalForm());
		
		stage.setScene(scene);
		stage.show();

	}

	private HBox addHBox() {

		MenuBar myMenuBar = new MenuBar();
		Menu qC = new Menu("QuickConnect");
		Menu profile = new Menu("Min profil");
		ObservableList<Menu> menus = FXCollections.<Menu>observableArrayList(qC, profile);
		
		MenuItem signOut = new MenuItem("Log ud");
		MenuItem close = new MenuItem("Luk");
		qC.getItems().addAll(signOut, close);
		
		MenuItem settings = new MenuItem("Instillinger");
		profile.getItems().addAll(settings);
		
		myMenuBar.getMenus().addAll(menus);
		
		HBox hBox = new HBox(0);
		hBox.setId("hbox");
		Text lTitle = new Text("Velkommen til QuickConnect " + username);
		lTitle.setFont(startFrame.getMyFont(1, 20));

		hBox.getChildren().addAll(myMenuBar);
		return hBox;
	}

	private Node addTabs() {

		TabPane tabPane = new TabPane();
		tabPane.setPrefHeight(1500);
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		Tab tabOnline = new Tab("Online", addOnlineContent());
		Tab tabFriends = new Tab("Venner", addFriendsContent());
		Tab tabGroups = new Tab("Grupper", addGroupsContent());
		tabOnline.setTooltip(new Tooltip("Vis alle online venner"));
		tabFriends.setTooltip(new Tooltip("Vis alle venner"));
		tabGroups.setTooltip(new Tooltip("Vis mine grupper"));

		tabPane.getTabs().addAll(tabOnline, tabFriends, tabGroups);

		return tabPane;
	}

	private Node addOnlineContent() {

		VBox onlinePane = new VBox();

		HBox myHBox = new HBox(0);
		TextField inSearch = new TextField();
		inSearch.setPrefWidth(210);
		inSearch.setPromptText("Indtast søgning");
		Button bSearch = new Button("Søg");
		myHBox.getChildren().addAll(inSearch, bSearch);

		ScrollPane onlineScrollPane = new ScrollPane();
		ListView<String> list = new ListView<String>();
		ObservableList<String> items = FXCollections.observableArrayList("Ahmad", "Ibrahim", "Samil", "Tolga", "Harun",
		        "Umais", "Lars", "Hans", "Peter", "Søren", "Gurli", "Lars", "Hans", "Peter", "Søren", "Gurli", "Hans",
		        "Peter", "Søren", "Gurli");
		list.setItems(items);
		onlineScrollPane.setPrefHeight(1500);
		onlineScrollPane.setContent(list);

		onlinePane.getChildren().addAll(myHBox, onlineScrollPane);
		onlinePane.setVisible(true);
		
		return onlinePane;
	}

	private Node addFriendsContent() {
		
		VBox friendsPane = new VBox();

		HBox myHBox = new HBox(0);
		TextField inSearch = new TextField();
		inSearch.setPrefWidth(210);
		inSearch.setPromptText("Indtast søgning");
		Button bSearch = new Button("Søg");
		myHBox.getChildren().addAll(inSearch, bSearch);

		friendsPane.getChildren().addAll(myHBox);
		friendsPane.setVisible(true);
		
		return friendsPane;
	}

	private Node addGroupsContent() {
		
		VBox groupsPane = new VBox();

		HBox myHBox = new HBox(0);
		TextField inSearch = new TextField();
		inSearch.setPrefWidth(210);
		inSearch.setPromptText("Indtast søgning");
		Button bSearch = new Button("Søg");
		myHBox.getChildren().addAll(inSearch, bSearch);

		groupsPane.getChildren().addAll(myHBox);
		groupsPane.setVisible(true);
		
		return groupsPane;
	}

	@Override
	public void handle(ActionEvent event) {

	}

}
