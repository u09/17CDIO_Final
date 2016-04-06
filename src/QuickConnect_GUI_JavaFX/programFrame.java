package QuickConnect_GUI_JavaFX;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class programFrame extends startFrame{
	
	public void startPFrame(Stage stage, String username, String password) throws Exception {
		
		BorderPane pane = new BorderPane();
		pane.setTop(addHBox());
		pane.setLeft(addTabs());
		
		Scene scene = new Scene(pane, 1000, 700);
		
		stage.setScene(scene);
		stage.setTitle("QuickConnect - user: " + username);
        stage.show();
		
	}

	private Node addTabs() {
		
		TabPane tabPane = new TabPane();
		Tab tabOnline = new Tab("Online");
		Tab tabFriends = new Tab("Friends");
		Tab tabGroups = new Tab("Groups");
		tabOnline.setClosable(false);
		tabFriends.setClosable(false);
		tabGroups.setClosable(false);
		
		tabPane.getTabs().addAll(tabOnline, tabFriends, tabGroups);
		
		return tabPane;
	}

	private Node addHBox() {
		
		HBox hBox = new HBox(0);
		
		TextField inSearch = new TextField("Indtast søgning");
//		Image imageSearch = new Image(getClass().getResourceAsStream("/Users/Umais/Documents/Umais/DTU - Softwareteknologi/2. semester/CDIO/CDIO-Final/17CDIO_Final/src/QuickConnect_GUI_JavaFX/search.png"));
		Button bSearch = new Button("Søg");
		
		hBox.getChildren().addAll(inSearch, bSearch);
		return hBox;
	}	
	
}
