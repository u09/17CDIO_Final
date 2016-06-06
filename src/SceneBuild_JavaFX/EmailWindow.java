package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import QuickConnect.EmailVal;
import QuickConnect.Function;
import QuickConnect.FunctionUser;
import QuickConnect.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmailWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private VBox EmailFrame;
	private String email;
	@FXML private Label lCode;
	@FXML private Button bAccept, bSend;
	@FXML private TextField inCode;
	private FunctionUser fu;
	private EmailVal eVal = new EmailVal();

	public void start(Stage stage, String email) {
		this.email=email;
		User user = new User();
		Function f = new Function(user);
		this.fu = new FunctionUser(f);
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect");
		this.myStage.setResizable(false);
		showEmailFrame();
		this.myScene = new Scene(EmailFrame);

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
		String code = eVal.getCode();
    	eVal.sendFromGMail("Samilesma","Kode", this.email, "QuickConnect","Venligst indtast følgende kode ind\n"+ code);
	
	}

	private void showEmailFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(EmailWindow.class.getResource("EmailFrame.fxml"));
		loader.setController(this);
		try {
			EmailFrame = (VBox) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		bSend.setOnAction(this);
		bAccept.setDefaultButton(true);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == bSend) {
			String newcode = eVal.getCode();
	    	eVal.sendFromGMail("Samilesma","Kode", this.email, "QuickConnect","Venligst indtast følgende kode ind\n"+ newcode);
		}
		if(event.getSource() == bAccept) {
			Stage stage = new Stage();
			loginWindow lW = new loginWindow();
			myStage.close();
			try {
				lW.start(stage);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
