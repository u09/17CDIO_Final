package SceneBuild_JavaFX;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class infoWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private VBox InfoFrame;
	@FXML private Label lUsername, lNickname, lAge, lUser_created;
	@FXML private Text tUsername, tNickname, tAge, tUser_created;
	private int activeUser;
	private FunctionUser fu;

	public void start(Stage stage, int activeUser) {
		this.activeUser=activeUser;
		User user = new User();
		Function f = new Function(user);
		this.fu = new FunctionUser(f);
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect");
		this.myStage.setResizable(false);
		showInfoFrame();
		this.myScene = new Scene(InfoFrame);

		this.myStage.setScene(myScene);
		this.myStage.show();
	}

	private void showInfoFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(infoWindow.class.getResource("InfoFrame.fxml"));
		loader.setController(this);
		try {
			InfoFrame = (VBox) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		lUsername.getStyleClass().add("titles");
		try {
			setInfo();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setInfo() throws SQLException{
		tUsername.setText(fu.infoUser(this.activeUser, 1));
		tNickname.setText(fu.infoUser(this.activeUser, 2));
		tAge.setText(fu.infoUser(this.activeUser, 3));
		tUser_created.setText(fu.infoUser(this.activeUser, 4));
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}
}