package SceneBuild_JavaFX;

import java.awt.TextField;
import java.io.IOException;

import QuickConnect.FunctionUser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class groupWindow implements EventHandler<ActionEvent> {
	
	private Stage myStage;
	private Scene myScene; 
	private AnchorPane GroupsFrame; 
	@FXML private TextField inGroupName;
	@FXML private Button bAdd, bRemove, bCreate;
	@FXML private ListView<String> allFriends, groupMembers, myGroups;
	private FunctionUser fu; 
	
	public void start(Stage stage, FunctionUser fu){
		this.fu=fu; 
		this.myStage=stage; 
		this.myStage.setTitle("QuickConnect - Grupper");
		this.myStage.setResizable(false);
		
		showGroupFrame();
		
		myScene = new Scene(GroupsFrame);
		
		this.myStage.setScene(myScene);
		this.myStage.show();
	}

	private void showGroupFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(groupWindow.class.getResource("GroupsFrame.fxml"));
		loader.setController(this);
		
		try {
			GroupsFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		bAdd.setOnAction(this);
		bAdd.setDefaultButton(true);
		bRemove.setOnAction(this);
		bCreate.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		
		if(event.getSource() == bAdd){
			boolean listEmpty = allFriends.getItems().isEmpty();
			if(listEmpty){
			}
			else{
			}
		}
	
	}
	
	

}
