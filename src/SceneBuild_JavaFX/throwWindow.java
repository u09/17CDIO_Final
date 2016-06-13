package SceneBuild_JavaFX;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class throwWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene; 
	private AnchorPane ThrowFrame;
	@FXML private ListView<String> groupMembers;
	@FXML private Button bThrow;
	private int activeUser;
	private int[] usersID;
	private int au;
	private FunctionUser fu;
	
	public void start(Stage stage, int activeUser) throws SQLException, IOException{
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
	
	private void showThrowFrame() throws SQLException, IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(throwWindow.class.getResource("ThrowFrame.fxml"));
		loader.setController(this);
		try {
			ThrowFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		String[] allItems = fu.getAllMembersNickname(activeUser);
		usersID = fu.getAllMembersID(activeUser);
		ObservableList<String> onlineItems = FXCollections.observableArrayList(allItems);
		groupMembers.setItems(onlineItems);
		
		groupMembers.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				int index = groupMembers.getSelectionModel().getSelectedIndex();
				if(index == -1)
					return;
				au=index;
			}
		});
		
		bThrow.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		
		if(event.getSource() == bThrow){
			try {
				fu.kickMember(activeUser,usersID[au]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			groupMembers.getItems().remove(au);
			Alert createSuccess = new Alert(AlertType.INFORMATION);
			createSuccess.setTitle(this.myStage.getTitle());
			createSuccess.setHeaderText("Kick af medlem lykkedes!");
			createSuccess.setContentText("Du har nu smidt medlemmet ud af gruppen.");
			createSuccess.showAndWait();
		}
	}

	
}
