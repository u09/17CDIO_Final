package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import QuickConnect.Connector;
import QuickConnect.Function;
import QuickConnect.FunctionUser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class registerWindow implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private VBox RegisterFrame;
	@FXML private Label lTitle, lUser, lPass, lNewPass, lMail, lRegister;
	@FXML private Button bRegister, bBack;
	@FXML private TextField inUser, inMail;
	@FXML private PasswordField inPass, inNewPass;
	private FunctionUser fu;

	public void start(Stage stage, FunctionUser fu) {
		this.fu=fu;
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - Registering");
		this.myStage.setResizable(false);

		showRegisterFrame();

		myScene = new Scene(RegisterFrame);

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

	private void showRegisterFrame() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(registerWindow.class.getResource("RegisterFrame.fxml"));
		loader.setController(this);
		try {
			RegisterFrame = (VBox) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}

		bRegister.setOnAction(this);
		bBack.setOnAction(this);
		lTitle.getStyleClass().add("titles");
	}

	@Override
	public void handle(ActionEvent event) {
		// handle for bBack
		if(event.getSource() == bBack) {
			Stage stage = new Stage();
			loginWindow sF = new loginWindow();
			try {
				sF.start(stage);
			} catch(Exception e) {
				e.printStackTrace();
			}
			((Node) (event.getSource())).getScene().getWindow().hide();
		}
		// handle for bRegister
		if(event.getSource() == bRegister) {

			String cuser = inUser.getText();
			String cpass1 = inPass.getText();
			String cpass2 = inNewPass.getText();
			String cemail = inMail.getText();

			int check = 0;
			try {
				check = fu.f.checkRegister(cuser, cpass1, cpass2, cemail);
			} catch(SQLException e) {
				e.printStackTrace();
			}
			String msg;
			if(check == 0)
				msg = "Uforventet fejl";
			else if(check == 1)
				msg = "Brugernavnet skal være 4-24 tegn";
			else if(check == 2)
				msg = "Brugernavnet må kun indeholde tal, bogstaver og følgende tegn: -,_";
			else if(check == 3)
				msg = "Begge passwords skal være ens";
			else if(check == 4)
				msg = "Passwordet skal være mellem 8-24 tegn";
			else if(check == 5)
				msg = "Passwordet skal mindst indeholde 1 stort bogstav, 1 lille bogstav og 1 tal";
			else if(check == 6)
				msg = "Passwordet må kun indeholde tal, bogstaver og følgende tegn:<br><h2>!\"#$%&'(,)*+-./:;<=>?@[\\]^_`{|}~</h2>";
			else if(check == 7)
				msg = "Ugyldigt email";
			else if(check == 8)
				msg = "Brugeren eksisterer allerede i systemet";
			else msg = "Du er registreret";

			if(check == 9) {
				try {
					boolean bool = fu.addUser(cuser, cpass1, cemail);
				} catch(SQLException | NoSuchAlgorithmException e) {
					e.printStackTrace();
				}

				Stage stage = new Stage();
				loginWindow Sf = new loginWindow();
				try {
					Sf.start(stage);
				} catch(Exception e) {
					e.printStackTrace();
				}
				((Node) (event.getSource())).getScene().getWindow().hide();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle(this.myStage.getTitle());
				alert.setHeaderText("Registrering mislykkedes!");
				alert.setContentText(msg);
				alert.show();
				inPass.setText("");
				inNewPass.setText("");
				inUser.requestFocus();
			}
		}
	}
}
