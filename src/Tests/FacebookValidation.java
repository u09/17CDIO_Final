package Tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

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

public class FacebookValidation extends Application {

	/*
	 * Facebooks guide:
	 * https://developers.facebook.com/docs/facebook-login/manually-build-a-
	 * login-flow
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

		/*
		 * "<html> <script> window.fbAsyncInit = function() { FB.init({ appId :
		 * '153262288412700', xfbml : true, version : 'v2.6' }); }; (function(d,
		 * s, id){ var js, fjs = d.getElementsByTagName(s)[0]; if
		 * (d.getElementById(id)) {return;} js = d.createElement(s); js.id = id;
		 * js.src = "//connect.facebook.net/en_US/sdk.js";
		 * fjs.parentNode.insertBefore(js, fjs); }(document, 'script',
		 * 'facebook-jssdk')); </script> </html>"
		 */
		webEngine.load("https://www.facebook.com/dialog/oauth?" + "client_id=153262288412700" + "&display=popup"
		        + "&response_type=token" + "&redirect_uri=" + uri);

		String accessToken = "EAACLZAChCIBwBAGhAbKVp1drnj5eGrKVeXYjiNFV3zQb91sEV5ZAa9N73KnDyVzZAFDAZBGY0ruJWfN4M8htdn8TpFGGeebJeiIPAt8IOAbbPwBPKj3cmFI1T6LnsZAZBpDKySbt6eMiUMmyVcHUm8bgCZCa0R1gGePkAh68H8Yrvk6ZBwtnjOaAEJKJcwdTTFDUUwZAB3wt1enoQ0GWdZAPKI";
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		com.restfb.types.User me = fbClient.fetchObject("me", com.restfb.types.User.class);
		System.out.println(me.getName());
		System.out.println(me.getEmail());
		System.out.println(me.getBirthday());
		// webEngine.load("http://www.facebook.com");

		this.myStage.setScene(myScene);
		this.myStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
