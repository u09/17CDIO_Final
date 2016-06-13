package SceneBuild_JavaFX;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import QuickConnect.FunctionUser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
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
	@FXML private Button bRegister, bBack, bFacebook;
	@FXML private TextField inUser, inMail;
	@FXML private PasswordField inPass1, inPass2;
	@FXML private DatePicker datePicker;
	@FXML private Hyperlink hyperlink;
	@FXML private CheckBox checkBox;
	private FunctionUser fu;

	public void start(Stage stage, FunctionUser fu) {
		this.fu = fu;
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect - Registering");
		this.myStage.setResizable(false);

		showRegisterFrame();

		myScene = new Scene(RegisterFrame);

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
		bRegister.setDefaultButton(true);
		bBack.setOnAction(this);
		bBack.setCancelButton(true);
		datePicker.setOnAction(this);
		hyperlink.setOnAction(this);
		bFacebook.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		// handle for bBack
		if(event.getSource() == bBack) {
			myStage.close();
			Stage stage = new Stage();
			loginWindow lW = new loginWindow();
			try {
				lW.start(stage);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		// handle for bRegister
		if(event.getSource() == bRegister) {
			if(checkBox.isSelected()) {
				String cUser = inUser.getText();
				String cPass1 = inPass1.getText();
				String cPass2 = inPass2.getText();
				String cEmail = inMail.getText();
				LocalDate cDate = datePicker.getValue();
				int age = (int) ChronoUnit.YEARS.between(cDate, LocalDate.now());
				int check = 0;
				try {
					check = fu.f.checkRegister(cUser, cPass1, cPass2, cEmail, cDate);
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
					msg = "Du skal være over 10 år for at oprette en bruger";
				else if(check == 9)
					msg = "Brugeren eksisterer allerede i systemet";
				else msg = "Du er registreret";

				if(check == 10) {
					try {
						fu.addUser(cUser, cPass1, cEmail, age);
					} catch(SQLException | NoSuchAlgorithmException | IOException e) {
						e.printStackTrace();
					}
					Alert registerSuccess = new Alert(AlertType.INFORMATION);
					registerSuccess.setTitle(this.myStage.getTitle());
					registerSuccess.setHeaderText("Du er nu registreret i databasen!");
					registerSuccess.setContentText("Venligst godkend din email adresse");
					registerSuccess.showAndWait();
					myStage.close();
					Stage stage = new Stage();
					EmailWindow eW = new EmailWindow();
					try {
						eW.start(stage, cEmail, cUser, fu);
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					Alert registerFail = new Alert(AlertType.WARNING);
					registerFail.setTitle(this.myStage.getTitle());
					registerFail.setHeaderText("Registrering mislykkedes!");
					registerFail.setContentText(msg);
					registerFail.showAndWait();
					inPass1.clear();
					inPass2.clear();
					inMail.clear();
					inUser.requestFocus();
				}
			} else {
				Alert terms = new Alert(AlertType.WARNING);
				terms.setTitle(this.myStage.getTitle());
				terms.setHeaderText("OBS!");
				terms.setContentText("Du skal accepterer vilkår og betingelser");
				terms.showAndWait();
			}
		}
		if(event.getSource() == hyperlink) {
			URL url = null;
			java.net.URI uri = null;
			try {
				url = new URL(
				        "http://vignette3.wikia.nocookie.net/simpsons/images/e/e9/Nelson_Ha-Ha.jpg/revision/latest?cb=20121205194057");
				// http://quickconnect.tk/termsandconditions.html
				uri = url.toURI();
				java.awt.Desktop.getDesktop().browse(uri);
			} catch(URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		}
		if(event.getSource() == bFacebook) {
			myStage.close();
			Stage stage = new Stage();
			FacebookAPI fbAPI = new FacebookAPI();
			try {
				fbAPI.start(stage, fu);
			} catch(Exception e) {
				e.printStackTrace();
			}

		}
	}
}
