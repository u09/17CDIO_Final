package SceneBuild_JavaFX;

import java.io.IOException;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class throwWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene; 
	private AnchorPane ThrowFrame;
	private ListView GroupMembers; 
	private Button bThrow;
	private int activeUser;
	private FunctionUser fu;
	
	public void start(Stage stage, int activeUser){
		User user = new User();
		Function f = new Function(user);
		this.fu = new FunctionUser(f);
		this.activeUser = activeUser; 
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - Group members");
		this.myStage.setResizable(false);
		showThrowFrame();
		
		myScene = new Scene(ThrowFrame);

		this.myStage.setScene(myScene);
		this.myStage.show();
	}
	
	private void showThrowFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(throwWindow.class.getResource("ThrowFrame.fxml"));
		loader.setController(this);
		try {
			ThrowFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void handle(ActionEvent event) {

		
	}

	
}
