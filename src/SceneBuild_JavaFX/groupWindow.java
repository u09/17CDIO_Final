package SceneBuild_JavaFX;

import java.awt.TextField;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import QuickConnect.Function;
import QuickConnect.FunctionUser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class groupWindow implements EventHandler<ActionEvent> {
	
	private Stage myStage;
	private Scene myScene; 
	private AnchorPane GroupFrame; 
	@FXML private Button bAge; 
	@FXML private TextField inAge;
	private FunctionUser fu; 
	
	public void start(Stage stage, FunctionUser fu){
		this.fu=fu; 
		this.myStage=stage; 
		this.myStage.setTitle("QuickConnect - Alder");
		this.myStage.setResizable(false);
		
		showGroupFrame();
		
		myScene = new Scene(GroupFrame);
		
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

	private void showGroupFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(groupWindow.class.getResource("GroupFrame.fxml"));
		loader.setController(this);
		
		try {
			GroupFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		bAge.setOnAction(this);
		bAge.setDefaultButton(true);
	}

	@Override
	public void handle(ActionEvent event) {
		
		if(event.getSource() == bAge){
			if(Function.isNumeric(inAge.getText())){
				int age = Integer.parseInt(inAge.getText());
				try {
					fu.setAge(age);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				
			}
		}
	
	}
	
	

}
