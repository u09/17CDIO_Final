package SceneBuild_JavaFX;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.WebRequestor;
import com.restfb.types.User;

import QuickConnect.FunctionUser;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class FacebookAPI implements Runnable {

	/*
	 * Facebooks guide:
	 * https://developers.facebook.com/docs/facebook-login/manually-build-a-
	 * login-flow
	 */

	private Stage myStage;
	private Scene myScene;
	WebView facebookFrame;
	private static WebEngine webEngine;
	private FunctionUser fu;
	@FXML private WebView browser;

	private static final String REDIRECT_URI = "https://www.facebook.com/connect/login_success.html";
	private static final String MY_ACCESS_TOKEN = "EAACEdEose0cBAEvePZCkmOZBvExlDd2xNdG6S0doPU1PPg3X7ZBgbK9CpYFBh7DVxS9vjtJcsO9OMls1YOcdaHrwRvzX0IEikUQcLSKvjx1qG7TqijzAVjtAyEiHu8wayNZAUFJ3RF4YTof8wPuRXMS2UXhDwxTiIWsMSZCdZAFAZDZD";
	private static String USER_ACCESS_TOKEN;
	// Facebook App
	private static final String MY_APP_ID = "153262288412700";
	public static final String MY_APP_SECRET = "29b8cdc1c083c8de50d411257829a786";

	public void start(Stage stage, FunctionUser fu) {
		this.fu = fu;
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
						System.out.println(accessUrl);
						String token = accessUrl.substring(accessUrl.indexOf("#") + 1);
						String code = accessUrl.substring(accessUrl.indexOf("&code") + 6);
						getUserInfo(token, code, accessUrl);
					}
				}
			}
		});

		webEngine.load("https://www.facebook.com/dialog/oauth?" + "client_id=" + MY_APP_ID + "&display=popup"
		        + "&response_type=code%20token" + "&redirect_uri=" + REDIRECT_URI + "&scope=email");

		this.myStage.setScene(myScene);
		this.myStage.show();
	}

	private void getUserInfo(String token, String code, String accessUrl) {

		new AccessToken();
		AccessToken tokenInfo = AccessToken.fromQueryString(token);
		System.out.println("Access token 1: " + tokenInfo.getAccessToken());
		System.out.println("Expires 1: " + tokenInfo.getExpires());
		System.out.println("Code 1 : " + code);
		FacebookClient.AccessToken userToken = null;
		try {
			userToken = getFacebookUserToken(code, accessUrl);
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("UserBody: " + userToken);
		System.out.println("User Access token: " + userToken.getAccessToken());
		String userAccessToken = userToken.getAccessToken();
		FacebookClient fbClient = new DefaultFacebookClient(userAccessToken, MY_APP_SECRET, Version.VERSION_2_6);
		User user = fbClient.fetchObject("me", User.class,
		        Parameter.with("fields", "name, first_name, last_name, email, age_range, gender"));

		System.out.println("Navn = " + user.getName());
		System.out.println("Fornavn: " + user.getFirstName());
		System.out.println("Efternavn: " + user.getLastName());
		System.out.println("Email = " + user.getEmail());
		System.out.println("Placering: " + user.getLocation());
		System.out.println("Alder: " + user.getAgeRange());
		System.out.println("Fødselsdag: " + user.getBirthday());
		System.out.println("Køn: " + user.getGender());
		new Thread(new FacebookAPI()).start();
	}

	private FacebookClient.AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {

		WebRequestor wr = new DefaultWebRequestor();
		WebRequestor.Response accessTokenResponse = wr
		        .executeGet("https://graph.facebook.com/oauth/access_token?" + "client_id=" + MY_APP_ID
		                + "&redirect_uri=" + REDIRECT_URI + "&client_secret=" + MY_APP_SECRET + "&code=" + code);

		return DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
	}

	public static void getInputsFromJOptionPane() {
		String name;
		name = JOptionPane.showInputDialog(null, "Please enter your name");

	}

	@Override
	public void run() {
		getInputsFromJOptionPane();
	}
}
