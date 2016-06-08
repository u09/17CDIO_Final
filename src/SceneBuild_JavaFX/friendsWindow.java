package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

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
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class friendsWindow extends chatWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private AnchorPane friendsFrame;
	private TitledPane sentRequestsPane, recRequestsPane;
	@FXML private TextField inUsername;
	@FXML private Button bAdd, bAccept, bReject, bCancel;
	@FXML private ListView<String> sentList, receivedList;
	ObservableList<String> sentItems, reqItems;
	private FunctionUser fu;

	@Override
	public void start(Stage stage, FunctionUser fu) {
		this.fu = fu;
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - Anmodninger");
		this.myStage.setResizable(false);
		showFriendsFrame();

		myScene = new Scene(friendsFrame);

		File file = new File("QuickConnectCSS/StandardLayout.css");
		URL url;
		try {
			url = file.toURI().toURL();
			myScene.getStylesheets().add(url.toExternalForm());
		} catch(MalformedURLException e) {
			e.printStackTrace();
		}

		this.myStage.setScene(myScene);
		this.myStage.show();

	}

	private void showFriendsFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(friendsWindow.class.getResource("FriendsFrame.fxml"));
		loader.setController(this);
		try {
			friendsFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		setButtonFunctions();
		getRequestsContent();

	}

	private void getRequestsContent() {
		String[] sentRequests = null;
		try {
			sentRequests = fu.getSentRequests();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		sentItems = FXCollections.observableArrayList(sentRequests);
		if(sentItems != null) sentList.setItems(sentItems);
		else;
		
		String[] receivedRequests = null;
		try {
			receivedRequests = fu.getFriendsRequests();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		reqItems = FXCollections.observableArrayList(receivedRequests);
		if(reqItems != null) receivedList.setItems(reqItems);
		else;
		
	}

	private void setButtonFunctions() {
		EventHandler<KeyEvent> keyEvent = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getSource() == inUsername && event.getCode().equals(KeyCode.ENTER))
					bAdd.fire();
			}
		};

		inUsername.setOnKeyPressed(keyEvent);
		bAdd.setOnAction(this);
		bAccept.setOnAction(this);
		bReject.setOnAction(this);
		bCancel.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == bAdd) {
			try {
				int i = fu.addFriend(inUsername.getText());
				Alert passSuccess = new Alert(AlertType.INFORMATION);
				passSuccess.setTitle(myStage.getTitle());
				if(i>0) {
					passSuccess.setHeaderText("Din venneanmodning blev ikke sendt");
					if(i==1) passSuccess.setContentText("Du eller personen du prøver at tilføje har blokeret hinanden");
					else if(i==2) passSuccess.setContentText("Du er allerede venner med personen");
					else if(i==3) passSuccess.setContentText("Du har allerede sendt en venneanmodning til personen");
					else if(i==4) passSuccess.setContentText("Brugernavnet eksistere desværre ikke. Kontrollere om du har stavet korrekt");
				}
				else if(i==0) {
					passSuccess.setHeaderText("Din venneanmodning blev sendt");
					passSuccess.setContentText("Du har nu sendt en venneanmodning til " + inUsername.getText());
				}
				passSuccess.show();
			} catch(SQLException e) {
//				Alert passSuccess = new Alert(AlertType.INFORMATION);
//				passSuccess.setTitle(myStage.getTitle());
//				passSuccess.setHeaderText("Brugernavnet eksisterer ikke");
//				passSuccess.setContentText("Venligst indtast et gyldigt brugernavn");
//				passSuccess.show();
				e.printStackTrace();
			}
		}
		if(event.getSource() == bAccept) {
			String requestName = receivedList.getSelectionModel().getSelectedItem();
			try {
				fu.acceptFriend(requestName);
			} catch(SQLException | IOException e) {
				e.printStackTrace();
			}
			reqItems.remove(requestName);
		}
		if(event.getSource() == bReject) {
			String requestName = receivedList.getSelectionModel().getSelectedItem();
			try {
				fu.rejectFriend(requestName);
			} catch(SQLException e) {
				e.printStackTrace();
			}
			reqItems.remove(requestName);
		}
		if(event.getSource() == bCancel) {
			String sentName = sentList.getSelectionModel().getSelectedItem();
			try {
				fu.cancelRequest(sentName);
			} catch(SQLException e) {
				e.printStackTrace();
			}
			sentItems.remove(sentName);
		}
	}
}
