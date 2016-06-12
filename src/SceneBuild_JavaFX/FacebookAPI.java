package SceneBuild_JavaFX;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Session;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.experimental.api.Facebook;
import com.restfb.types.FacebookType;
import com.restfb.types.User;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.net.httpserver.Authenticator.Result;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import QuickConnect.FunctionUser;
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

public class FacebookAPI {

	/*
	 * Facebooks guide:
	 * https://developers.facebook.com/docs/facebook-login/manually-build-a-
	 * login-flow
	 */

	private Stage myStage;
	private Scene myScene;
	WebView facebookFrame;
	private static WebEngine webEngine;
	@FXML private WebView browser;

	private static final String REDIRECT_URI = "https://www.facebook.com/connect/login_success.html";
	private static final String MY_ACCESS_TOKEN = "EAACEdEose0cBANPkygHiLr2z41ZBfWWnrG3JEAbQ6mbJZAPGGzgZCQfZAjOyj8vLXZAFgHe9FL0U8G2YZC8ed5FVZAEEXtvzEVdoToUP9KfmaGbXuBedL5LzUUX9yQcsDZACmd70a86AT8aFcz70p91qM0jVb37YiVOL9WQeFl7I9QZDZD";
	private static String USER_ACCESS_TOKEN;
	// Facebook App
	private static final String MY_APP_ID = "153262288412700";
	public static final String MY_APP_SECRET = "29b8cdc1c083c8de50d411257829a786";

	public void start(Stage stage, FunctionUser fu) {
		this.myStage = stage;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FacebookAPI.class.getResource("FacebookFrame.fxml"));
		loader.setController(this);
		try {
			facebookFrame = (WebView) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		this.myScene = new Scene(facebookFrame);

		webEngine = browser.getEngine();
		browser.setContextMenuEnabled(true);
		
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				if(newState == Worker.State.SUCCEEDED) {
					String url = webEngine.getLocation();
					myStage.setTitle(url);
					if(url.contains("#access_token")) {
						String accessUrl = webEngine.getLocation();
						String string = accessUrl.substring(accessUrl.indexOf("#")+1);
						getUserInfo(string);
					}
				}
			}
		});
		
		webEngine.load("https://www.facebook.com/dialog/oauth?" + "client_id=" + MY_APP_ID + "&display=popup"
				+ "&response_type=token" + "&redirect_uri=" + REDIRECT_URI);

		this.myStage.setScene(myScene);
		this.myStage.show();
		
	}

	private void getUserInfo(String token) {
		AccessToken tokenInfo = new AccessToken().fromQueryString(token);
		System.out.println(tokenInfo.getAccessToken());
		DefaultFacebookClient facebookClient = new DefaultFacebookClient(tokenInfo.getAccessToken());
//		AccessToken haa = facebookClient.obtainUserAccessToken(MY_APP_ID, MY_APP_SECRET, REDIRECT_URI, token);
		User user = facebookClient.fetchObject("me", User.class);
		String username = user.getName();
		String email = user.getEmail();
		String birthday = user.getBirthday();
		System.out.println("Navn = " + email);
		System.out.println("Email = " + email);
		System.out.println("Fødselsdag = " + birthday);
		
	}

}
