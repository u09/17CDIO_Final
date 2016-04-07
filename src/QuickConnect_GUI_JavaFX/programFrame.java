package QuickConnect_GUI_JavaFX;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class programFrame extends Application implements EventHandler<ActionEvent>{
	
	String username;
	String password;
	
	public static void main(String[] args) {
		launch(programFrame.class, args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
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
		TextField inSearch = new TextField("Indtast søgning");
		Button bSearch = new Button("Søg");
		
		tabPane.getTabs().addAll(tabOnline, tabFriends, tabGroups);
		
		return tabPane;
	}

	private Node addHBox() {
		
		HBox hBox = new HBox(0);
		hBox.setAlignment(Pos.CENTER);
		Text lTitle = new Text("Velkommen til QuickConnect " + username);
		lTitle.setFont(startFrame.getMyFont(1, 20));
//		Image imageSearch = new Image(getClass().getResourceAsStream("/Users/Umais/Documents/Umais/DTU - Softwareteknologi/2. semester/CDIO/CDIO-Final/17CDIO_Final/src/QuickConnect_GUI_JavaFX/search.png"));
		
		hBox.getChildren().addAll(lTitle);
		return hBox;
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
