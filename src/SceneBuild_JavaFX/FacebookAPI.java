package SceneBuild_JavaFX;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;

import QuickConnect.EmailVal;
import QuickConnect.FunctionUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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
	private FunctionUser fu;
	@FXML private WebView browser;

	private static final String REDIRECT_URI = "https://www.facebook.com/connect/login_success.html";
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
						String token = accessUrl.substring(accessUrl.indexOf("#access_token") + 1);
						String code = accessUrl.substring(accessUrl.indexOf("&code") + 6);
						getUserInfo(token);
					}
				}
			}
		});

		webEngine.load("https://www.facebook.com/dialog/oauth?" + "client_id=" + MY_APP_ID + "&display=popup"
		        + "&response_type=code%20token" + "&redirect_uri=" + REDIRECT_URI + "&scope=email");

		this.myStage.setScene(myScene);
		this.myStage.show();
	}

	private void getUserInfo(String token) {

		AccessToken tokenInfo = AccessToken.fromQueryString(token);
		FacebookClient fbClient = new DefaultFacebookClient(tokenInfo.getAccessToken(), MY_APP_SECRET,
		        Version.VERSION_2_6);
		User user = fbClient.fetchObject("me", User.class,
		        Parameter.with("fields", "name, first_name, last_name, email, age_range, gender"));

		Integer age = null;
		if(user.getAgeRange().getMax() == null) {
			age = user.getAgeRange().getMin();
		} else age = user.getAgeRange().getMax();
		String ranPass = fu.randomString();
		try {
			fu.addFacebookUser(user.getEmail(), ranPass, age);
		} catch(NoSuchAlgorithmException | SQLException | IOException e) {
			e.printStackTrace();
		}
		myStage.close();
		EmailVal eVal = new EmailVal();
		eVal.sendMail(eVal.getMail(), eVal.getPass(), user.getEmail(), "QuickConnect", "Hej " + user.getFirstName()
		        + "\n\nDin første adgangskode er:\n\n" + ranPass + "\n\nDu kan ændre denne efter du er logget ind.");

		ButtonType bSignIn = new ButtonType("Log ind", ButtonData.OK_DONE);
		ButtonType bClose = new ButtonType("Luk", ButtonData.NO);
		Alert fbRegisterSuccess = new Alert(AlertType.CONFIRMATION, null, bSignIn, bClose);
		fbRegisterSuccess.initOwner(myStage);
		fbRegisterSuccess.setTitle("Registrering");
		fbRegisterSuccess.setHeaderText("Du er nu registreret igennem Facebook!");
		fbRegisterSuccess.setContentText(
		        "\nDit brugernavn er din email og du har modtaget\ndin første adgangskode på email.\nVi du logge ind nu eller lukke programmet?");

		Optional<ButtonType> result = fbRegisterSuccess.showAndWait();

		if(result.get() == bSignIn) {
			Stage stage = new Stage();
			loginWindow lW = new loginWindow();
			try {
				lW.start(stage);
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else System.exit(0);

	}
}
