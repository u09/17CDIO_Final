package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import QuickConnect.Connector;
import QuickConnect.Function;
import QuickConnect_GUI_JavaFX.registerFrame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class startFrame extends Application implements EventHandler<ActionEvent> {

	FXMLLoader loader;
	private VBox startFrame;
	@FXML Label lTitle, lUser, lPass, lNoUser, lRegister; 
	@FXML
	Button bLogin, bRegister;
	@FXML
	TextField inUser;
	@FXML
	PasswordField inPass;

	@Override
	public void start(Stage primaryStage) throws Exception {

		loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("startFrame.fxml"));
		loader.setController(this);

		try {
			startFrame = (VBox) loader.load();
			bLogin.setOnAction(this);
			bRegister.setOnAction(this);
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene scene = new Scene(startFrame);
		File file = new File("src/SceneBuild_JavaFX/standardLayout.css");
		URL url = file.toURI().toURL();
		scene.getStylesheets().add(url.toExternalForm());
		lTitle.getStyleClass().add("titles");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@Override
	public void handle(ActionEvent event) {
		// handle for bLogin
		if(event.getSource() == bLogin) {
			String userIn = inUser.getText();
			String passIn = inPass.getText();
			Connector con = Function.mysql();
			boolean bool = false;
			try {
				bool = con.check("SELECT username FROM users WHERE username=? AND password=?", userIn,
				        Function.md5(passIn));
				System.out.println(bool);
			} catch(SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			if(bool == true) {
				Stage stage = new Stage();
				MainApp pf = new MainApp();

				try {
					pf.start(stage);
				} catch(Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				((Node) (event.getSource())).getScene().getWindow().hide();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Forkert kode");
				alert.setHeaderText("Forkert kodeord eller brugernavn");
				alert.setContentText("Venligst indtast dit kodeord og brugernavn igen");

				alert.showAndWait();
				inPass.setText("");
				inUser.requestFocus();
			}
		}
		// handle for bRegister
		if(event.getSource() == bRegister) {
			Stage stage = new Stage();
			registerFrame Rf = new registerFrame();
			try {
				Rf.start(stage);
			} catch(Exception e) {
				e.printStackTrace();
			}
			((Node) (event.getSource())).getScene().getWindow().hide();
		}

	}

}
