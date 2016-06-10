package Tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import SceneBuild_JavaFX.friendsWindow;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class FacebookValidation extends Application{
	
	/* Facebooks guide: 
	 * https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow
	 */
	
	private Stage myStage;
	private Scene myScene;
	AnchorPane facebookFrame;
	@FXML private WebView browser;
	
	@Override
	public void start(Stage stage) {
		this.myStage = stage;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FacebookValidation.class.getResource("FacebookFrame.fxml"));
		loader.setController(this);
		try {
			facebookFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		this.myScene = new Scene(facebookFrame);
		
		WebEngine webEngine = browser.getEngine();
		
		java.net.URI uri = null;
		try {
			URL url = new URL("https://www.facebook.com/connect/login_success.html");
			uri = url.toURI();
		} catch(MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		webEngine.getLoadWorker().stateProperty()
		.addListener(new ChangeListener<State>() {
          @Override
          public void changed(ObservableValue ov, State oldState, State newState) {

            if (newState == Worker.State.SUCCEEDED) {
              myStage.setTitle(webEngine.getLocation());
            }

          }
        });
		// https://www.facebook.com/dialog/oauth?client_id=153262288412700&redirect_uri=https://www.facebook.com/connect/login_success.html
//		webEngine.load("http://www.facebook.com");
		webEngine.load("https://www.facebook.com/dialog/oauth?client_id=153262288412700&redirect_uri="+uri);
		
		this.myStage.setScene(myScene);
		this.myStage.show();
//		while(true) {
//			System.out.println(webEngine.getLocation());
//		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
