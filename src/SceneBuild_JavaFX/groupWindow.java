package SceneBuild_JavaFX;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import QuickConnect.FunctionUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class groupWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private TabPane GroupsFrame;
	@FXML private TextField inGroupName;
	@FXML private Button bAdd, bRemove, bCreate;
	@FXML private ListView<String> allFriends, groupMembers;
	private FunctionUser fu;
	private ArrayList<Integer> allFriendsId1 = new ArrayList<Integer>(), allFriendsId2 = new ArrayList<Integer>();

	public void start(Stage stage, FunctionUser fu) throws SQLException {
		this.fu = fu;
		this.myStage = stage;
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

		String[] allItems = fu.getAllFriendsNickname();
		allFriendsId1 = fu.getAllFriendsId();
		ObservableList<String> onlineItems = FXCollections.observableArrayList(allItems);
		allFriends.setItems(onlineItems);

		allFriends.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				int index = allFriends.getSelectionModel().getSelectedIndex();
				if(index == -1)
					return;
				System.out.println(allFriendsId1.get(index));
			}
		});
		groupMembers.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				int index = groupMembers.getSelectionModel().getSelectedIndex();
				if(index == -1)
					return;
				System.out.println(allFriendsId2.get(index));
			}
		});

		bAdd.setOnAction(this);
		bAdd.setDefaultButton(true);
		bRemove.setOnAction(this);
		bCreate.setOnAction(this);

	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == bAdd) {
			boolean listEmpty = allFriends.getItems().isEmpty();
			if(!listEmpty) {
				groupMembers.getSelectionModel().clearSelection();
				String selectedFriend = allFriends.getSelectionModel().getSelectedItem();
				int index = allFriends.getSelectionModel().getSelectedIndex();
				allFriends.getItems().remove(selectedFriend);
				groupMembers.getItems().add(selectedFriend);
				allFriendsId2.add(allFriendsId1.get(index));
				allFriendsId1.remove(index);
			}
		}
		if(event.getSource() == bRemove) {
			boolean listEmpty = groupMembers.getItems().isEmpty();
			String selectedMember = groupMembers.getSelectionModel().getSelectedItem();
			if(!listEmpty && selectedMember != null) {
				allFriends.getSelectionModel().clearSelection();
				String memberName = groupMembers.getSelectionModel().getSelectedItem();
				int index = groupMembers.getSelectionModel().getSelectedIndex();
				groupMembers.getItems().remove(memberName);
				allFriends.getItems().add(memberName);
				allFriendsId1.add(allFriendsId2.get(index));
				allFriendsId2.remove(index);
			}
		}
		if(event.getSource() == bCreate) {
			System.out.println(fu.f.getUser().getUserID() + " " + inGroupName.getText());
			try {
				fu.createGroup(fu.f.getUser().getUserID(), inGroupName.getText(), allFriendsId2);
				String out = "";
				for(int i = 1; i <= allFriendsId2.size(); i++) {
					int id = allFriendsId2.get(i - 1);
					String nick = fu.idToNickname(id);
					if(i == 1)
						out = nick;
					else out = out + ", " + nick;
				}

				Alert createSuccess = new Alert(AlertType.INFORMATION);
				createSuccess.setTitle(this.myStage.getTitle());
				createSuccess.setHeaderText("Gruppe oprettet");
				createSuccess.setContentText("Din gruppe '" + inGroupName.getText()
				        + "' er nu oprettet. F�lgende er inviteret til din gruppe:\n" + out);
				createSuccess.showAndWait();

				myStage.close();

			} catch(SQLException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
