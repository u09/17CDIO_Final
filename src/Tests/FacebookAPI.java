package Tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.net.httpserver.Authenticator.Result;

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

public class FacebookAPI extends Application {

	/*
	 * Facebooks guide:
	 * https://developers.facebook.com/docs/facebook-login/manually-build-a-
	 * login-flow
	 */

	private Stage myStage;
	private Scene myScene;
	AnchorPane facebookFrame;
	@FXML private WebView browser;

	public static final String REDIRECT_URI = "https://www.facebook.com/connect/login_success.html";

	public static final String MY_ACCESS_TOKEN = "EAACLZAChCIBwBAKBNJYOUiZAsEu6t9jQZBK2TOSx5WlN6yKg57DB5JBPVEvv6jZAD2YUfRL7v9CPSS0ZA8vndx5yzx7FWlaoUpraD3tZAqPr5ZCW9aL9dQFED3ZBUIPb87E2xZBtIEMoNTJRfSkZAMCVXzraKslXtD4lqWgmH9cyrmQQZDZD";
	
	// Facebook App
	public static final String MY_APP_ID = "153262288412700";
	public static final String MY_APP_SECRET = "29b8cdc1c083c8de50d411257829a786";
	
	@Override
	public void start(Stage stage) {
		this.myStage = stage;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FacebookAPI.class.getResource("FacebookFrame.fxml"));
		loader.setController(this);
		try {
			facebookFrame = (AnchorPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		this.myScene = new Scene(facebookFrame);

		WebEngine webEngine = browser.getEngine();
		browser.setContextMenuEnabled(true);

		java.net.URI uri = null;
		try {
			URL url = new URL("https://www.facebook.com/connect/login_success.html");
			uri = url.toURI();
		} catch(MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		}

		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {

				if(newState == Worker.State.SUCCEEDED) {
					myStage.setTitle(webEngine.getLocation());
				}

			}
		});

		webEngine.load("https://www.facebook.com/dialog/oauth?" + "client_id=153262288412700" + "&display=popup"
		        + "&response_type=token" + "&redirect_uri=" + uri);
		System.out.println(webEngine.getTitle());
		// webEngine.load("http://www.facebook.com");
		
		this.myStage.setScene(myScene);
		this.myStage.show();

	}

	/*
	 * "<html> <script> window.fbAsyncInit = function() { FB.init({ appId :
	 * '153262288412700', xfbml : true, version : 'v2.6' }); }; (function(d,
	 * s, id){ var js, fjs = d.getElementsByTagName(s)[0]; if
	 * (d.getElementById(id)) {return;} js = d.createElement(s); js.id = id;
	 * js.src = "//connect.facebook.net/en_US/sdk.js";
	 * fjs.parentNode.insertBefore(js, fjs); }(document, 'script',
	 * 'facebook-jssdk')); </script> </html>"
	 */
	public static void main(String[] args) {
//		launch(args);
		
		authUser(MY_ACCESS_TOKEN);
		
	}
	
	public static void authUser(String accessToken) {
		
	    FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
	    User user = facebookClient.fetchObject("me", User.class);

	    System.out.println("User="+ user);
        System.out.println("UserName= "+ user.getUsername());
        System.out.println("Birthday= "+ user.getBirthday());
        System.out.println("Email= "+ user.getEmail());
	}

}
