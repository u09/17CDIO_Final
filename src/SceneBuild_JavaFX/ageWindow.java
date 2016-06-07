package SceneBuild_JavaFX;

import java.awt.Button;
import java.awt.TextField;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import QuickConnect.FunctionUser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ageWindow implements EventHandler<ActionEvent> {
	
	private Stage myStage;
	private Scene myScene; 
	private VBox AgeFrame; 
	@FXML private Button bAge; 
	@FXML private TextField inAge;
	private FunctionUser fu; 
	
	public void start(Stage stage, FunctionUser fu){
		this.fu=fu; 
		this.myStage=stage; 
		this.myStage.setTitle("QuickConnect - Alder");
		this.myStage.setResizable(false);
		
		showAgeFrame();
		
		myScene = new Scene(AgeFrame);
		
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

	private void showAgeFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ageWindow.class.getResource("AgeFrame.fxml"));
		loader.setController(this);
		
		try {
			AgeFrame = (VBox) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		bAge.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {

		
	}
	
	

}
