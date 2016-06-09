package SceneBuild_JavaFX;

import java.awt.TextField;
import java.io.IOException;
import java.sql.SQLException;

import QuickConnect.FunctionUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class groupWindow implements EventHandler<ActionEvent> {
	
	private Stage myStage;
	private Scene myScene; 
	private TabPane GroupsFrame; 
	@FXML private TextField inGroupName;
	@FXML private Button bAdd, bRemove, bCreate;
	@FXML private ListView<String> allFriends, groupMembers;
	@FXML private TableView myGroups;
	private FunctionUser fu; 
	
	public void start(Stage stage, FunctionUser fu) throws SQLException{
		this.fu=fu; 
		this.myStage=stage; 
		this.myStage.setTitle("QuickConnect - Grupper");
		this.myStage.setResizable(false);
		showGroupFrame();
		
		myScene = new Scene(GroupsFrame);
		
		this.myStage.setScene(myScene);
		this.myStage.show();
	}

	private void showGroupFrame() throws SQLException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(groupWindow.class.getResource("GroupsFrame.fxml"));
		loader.setController(this);
		
		try {
			GroupsFrame = (TabPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		String[] allItems = fu.allFriendsUsername();
		ObservableList<String> onlineItems = FXCollections.observableArrayList(allItems);
		allFriends.setItems(onlineItems);
		
		bAdd.setOnAction(this);
		bAdd.setDefaultButton(true);
		bRemove.setOnAction(this);
		bCreate.setOnAction(this);
		
	}

	@Override
	public void handle(ActionEvent event) {
		
		if(event.getSource() == bAdd){
			boolean listEmpty = allFriends.getItems().isEmpty();
			if(!listEmpty){
				groupMembers.getSelectionModel().clearSelection();
				String selectedFriend = allFriends.getSelectionModel().getSelectedItem();
				allFriends.getItems().remove(selectedFriend);
				groupMembers.getItems().add(selectedFriend);			
			}
		}
		if(event.getSource() == bRemove){
			boolean listEmpty = groupMembers.getItems().isEmpty();
			String selectedMember = groupMembers.getSelectionModel().getSelectedItem();
			if(!listEmpty && selectedMember != null) {
				allFriends.getSelectionModel().clearSelection();
				String memberName = groupMembers.getSelectionModel().getSelectedItem();
				groupMembers.getItems().remove(memberName);
				allFriends.getItems().add(memberName);
			}
		}
		if(event.getSource() == bCreate){
//			groupMembers.getItems();
//			fu.createGroup(""+fu.f.user().getUserID(), inGroupName.getText(), );
		}
		
	}
	
	

}
