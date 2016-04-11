package QuickConnect_GUI_JavaFX;

import java.io.File;
import java.net.URL;

import SceneBuild_JavaFX.loginWindow;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class programFrame extends Application implements EventHandler<ActionEvent> {

	Scene scene;;
	BorderPane borderPane;
	String username;
	String password;

	public static void main(String[] args) {
		launch(programFrame.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		stage.setTitle("QuickConnect - user: " + username);

		borderPane = new BorderPane();
		borderPane.setTop(addHBox());
		borderPane.setLeft(addTabs());
		borderPane.setCenter(addMessagePane());

		scene = new Scene(borderPane, 1000, 700);
		File file = new File("src/QuickConnect_GUI_JavaFX/standardLayout.css");
		URL url = file.toURI().toURL();
		scene.getStylesheets().add(url.toExternalForm());
		
		stage.setScene(scene);
		stage.show();

	}
	
	private Node addMessagePane() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private HBox addHBox() {

		HBox hBox = new HBox(0);
		hBox.setId("hbox");
		Text lTitle = new Text("Velkommen til QuickConnect " + username);
		lTitle.setFont(loginWindow.getMyFont(1, 20));
		MenuBar myMenuBar = new MenuBar();
		myMenuBar.setUseSystemMenuBar(true);
		
		Menu qC = new Menu("QuickConnect");
		Menu profile = new Menu("Min profil");
		Menu show = new Menu("Vis");
		ObservableList<Menu> menus = FXCollections.<Menu>observableArrayList(qC, profile, show);
		
		MenuItem about = new MenuItem("Om");
		MenuItem close = new MenuItem("Luk");
		close.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
		close.setOnAction((ActionEvent t) -> {
			System.exit(0);
		});
		qC.getItems().addAll(about, close);
		
		MenuItem signOut = new MenuItem("Log ud");
		MenuItem settings = new MenuItem("Instillinger");
		profile.getItems().addAll(settings, signOut);
		
		myMenuBar.getMenus().addAll(menus);

		hBox.getChildren().addAll(myMenuBar);
		return hBox;
	}

	private Node addTabs() {

		TabPane tabPane = new TabPane();
		tabPane.setPrefHeight(1500);
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		Tab tabRecent = new Tab("Seneste", addRecentContent());
		Tab tabFriends = new Tab("Venner", addFriendsContent());
		Tab tabGroups = new Tab("Grupper", addGroupsContent());
		tabRecent.setTooltip(new Tooltip("Vis alle online venner"));
		tabFriends.setTooltip(new Tooltip("Vis alle venner"));
		tabGroups.setTooltip(new Tooltip("Vis mine grupper"));

		tabPane.getTabs().addAll(tabRecent, tabFriends, tabGroups);

		return tabPane;
	}

	private Node addRecentContent() {

		VBox recentPane = new VBox();

		HBox myHBox = new HBox(0);
		TextField inSearch = new TextField();
		inSearch.setPrefWidth(210);
		inSearch.setPromptText("Indtast søgning");
		Button bSearch = new Button("Søg");
		myHBox.getChildren().addAll(inSearch, bSearch);

		ScrollPane recentScrollPane = new ScrollPane();
		ListView<String> list = new ListView<String>();
		ObservableList<String> items = FXCollections.observableArrayList("Ahmad", "Ibrahim", "Samil", "Tolga", "Harun",
		        "Umais", "Lars", "Hans", "Peter", "Søren", "Gurli", "Lars", "Hans", "Peter", "Søren", "Gurli", "Hans",
		        "Peter", "Søren", "Gurli");
		list.setItems(items);
		recentScrollPane.setContent(list);

		recentPane.getChildren().addAll(myHBox, recentScrollPane);
		recentPane.setVisible(true);
		
		return recentPane;
	}

	private Node addFriendsContent() {
		
		SplitPane mySplitPane = new SplitPane();
		VBox friendsPaneOnline = new VBox();
		VBox friendsPaneOffline = new VBox();
		

		HBox myHBox = new HBox(0);
		TextField inSearch = new TextField();
		inSearch.setPrefWidth(210);
		inSearch.setPromptText("Indtast søgning");
		Button bSearch = new Button("Søg");
		myHBox.getChildren().addAll(inSearch, bSearch);
		ScrollPane onlineScrollPane = new ScrollPane();
		ListView<String> onlineList = new ListView<String>();
		ObservableList<String> onlineItems = FXCollections.observableArrayList("Online1", "Online2", "Online3", "Online4", "Online5", "Online10", "Online20", "Online30", "Online40", "Online50", "Online100", "Online200",
		        "Online300	", "Online400", "Online500");
		onlineList.setItems(onlineItems);
		onlineScrollPane.setContent(onlineList);
		
		friendsPaneOnline.getChildren().addAll(myHBox, onlineScrollPane);
		
		ScrollPane offlineScrollPane = new ScrollPane();
		ListView<String> offlineList = new ListView<String>();
		ObservableList<String> offlineItems = FXCollections.observableArrayList("Offline1", "Offline2", "Offline3", "Offline4", "Offline5", "Offline10", "Offline20", "Offline30", "Offline40", "Offline50", "Offline100", "Offline200",
		        "Offline300	", "Offline400", "Offline500");
		offlineList.setItems(offlineItems);
		offlineScrollPane.setContent(offlineList);
		
		friendsPaneOffline.getChildren().addAll(offlineScrollPane);
		
		mySplitPane.getItems().addAll(friendsPaneOnline, friendsPaneOffline);
		mySplitPane.setOrientation(Orientation.VERTICAL);
		mySplitPane.setVisible(true);
		
		return mySplitPane;
	}

	private Node addGroupsContent() {
		
		VBox groupsPane = new VBox();

		HBox myHBox = new HBox(0);
		TextField inSearch = new TextField();
		inSearch.setPrefWidth(210);
		inSearch.setPromptText("Indtast søgning");
		Button bSearch = new Button("Søg");
		myHBox.getChildren().addAll(inSearch, bSearch);

		ScrollPane groupScrollPane = new ScrollPane();
		ListView<String> list = new ListView<String>();
		ObservableList<String> items = FXCollections.observableArrayList("Group1", "Group2", "Group3", "Group4", "Group5", "Group10", "Group20", "Group30", "Group40", "Group50", "Group100", "Group200",
		        "Group300", "Group400", "Group500");
		list.setItems(items);
		groupScrollPane.setContent(list);
		
		groupsPane.getChildren().addAll(myHBox, groupScrollPane);
		groupsPane.setVisible(true);
		
		return groupsPane;
	}

	@Override
	public void handle(ActionEvent event) {
		
	}

}
