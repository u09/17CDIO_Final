package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class settingsWindow extends Application implements EventHandler<ActionEvent> {

	Stage myStage;
	Scene myScene;
	AnchorPane settingsFrame;
	FXMLLoader loader;
	
	@Override
	public void start(Stage stage) throws Exception {

		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - Settings");
		this.myStage.setResizable(false);
		
		showSettingsFrame();
		
		myScene = new Scene(settingsFrame);
		
		File file = new File("src/SceneBuild_JavaFX/standardLayout.css");
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

	private void showSettingsFrame() {
		
		loader = new FXMLLoader();
		loader.setLocation(settingsWindow.class.getResource("SettingsFrame.fxml"));
		loader.setController(this);
		try {
			settingsFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}
}
