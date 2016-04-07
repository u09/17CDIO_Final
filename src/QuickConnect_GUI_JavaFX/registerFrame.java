package QuickConnect_GUI_JavaFX;

import java.io.File;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import QuickConnect.Connector;
import QuickConnect.Function;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class registerFrame extends Application implements EventHandler<ActionEvent> {

	TextField inUser;
	PasswordField inPass1;
	PasswordField inPass2;
	TextField inEmail;
	Button bRegister;
	Button bBack;

	public static void main(String[] args) {
		launch(registerFrame.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		StackPane pane = new StackPane();
		pane.getChildren().add(addVBox());

		Scene scene = new Scene(pane, 320, 470);
		File file = new File("src/QuickConnect_GUI_JavaFX/standardLayout.css");
		URL url = file.toURI().toURL();
		scene.getStylesheets().add(url.toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.setTitle("QuickConnect - Registrering");
		stage.show();
	}

	private VBox addVBox() {

		VBox myVBox = new VBox();
		myVBox.setSpacing(12.0);
		myVBox.setAlignment(Pos.CENTER);

		Text lTitle = new Text("QuickConnect registrering");
		lTitle.getStyleClass().add("titles");
		Text lUser = new Text("Indtast ønsket brugernavn:");
		inUser = new TextField();
		inUser.setMaxSize(150, 20);
		inUser.setPromptText("Brugernavn");
		Text lPass = new Text("Indtast dit password:");
		inPass1 = new PasswordField();
		inPass1.setMaxSize(150, 20);
		inPass1.setPromptText("Password");
		Text lPass2 = new Text("Gentag dit password:");
		inPass2 = new PasswordField();
		inPass2.setMaxSize(150, 20);
		inPass2.setPromptText("Password");
		Text lEmail = new Text("Indtast din email:");
		inEmail = new TextField();
		inEmail.setMaxSize(150, 20);
		inEmail.setPromptText("E-mail");
		Text lRegister = new Text("Registrer her:");
		bRegister = new Button("Registrer");
		bRegister.setOnAction(this);
		bBack = new Button("Tilbage");
		bBack.setOnAction(this);

		myVBox.getChildren().addAll(lTitle, lUser, inUser, lPass, inPass1, lPass2, inPass2, lEmail, inEmail, lRegister,
		        bRegister, bBack);

		return myVBox;
	}

	@Override
	public void handle(ActionEvent event) {
		// handle for bBack
		if(event.getSource() == bBack) {
			Stage stage = new Stage();
			startFrame sF = new startFrame();
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
			String cpass1 = inPass1.getText();
			String cpass2 = inPass2.getText();
			String cemail = inEmail.getText();

			int check = 0;
			try {
				check = checkRegister(cuser, cpass1, cpass2, cemail);
			} catch(SQLException e) {
				e.printStackTrace();
			}
			String msg;
			if(check == 0) msg = "Uforventet fejl";
			else if(check == 1) msg = "Brugernavnet skal være 4-24 tegn";
			else if(check == 2) msg = "Brugernavnet må kun indeholde tal, bogstaver og følgende tegn: -,_";
			else if(check == 3) msg = "Begge passwords skal være ens";
			else if(check == 4) msg = "Passwordet skal være 8-24 tegn";
			else if(check == 5) msg = "Passwordet skal mindst indeholde 1 stort bogstav, 1 lille bogstav og 1 tal";
			else if(check == 6) msg = "Passwordet må kun indeholde tal, bogstaver og følgende tegn:<br><h2>!\"#$%&'(,)*+-./:;<=>?@[\\]^_`{|}~</h2>";
			else if(check == 7) msg = "Ugyldigt email";
			else if(check == 8) msg = "Brugeren eksisterer allerede i systemet";
			else msg = "Du er registreret";

			if(check == 9) {
				try {
					boolean bool = addUser(cuser, cpass1, cemail);
				} catch(SQLException | NoSuchAlgorithmException e) {
					e.printStackTrace();
				}

				Stage stage = new Stage();
				startFrame Sf = new startFrame();
				try {
					Sf.start(stage);
				} catch(Exception e) {
					e.printStackTrace();
				}
				((Node) (event.getSource())).getScene().getWindow().hide();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Forkert kode");
				alert.setHeaderText("Forkert kodeord eller brugernavn");
				// alert.setContentText("<html>Registrering
				// mislykkedes!<br><br>"+msg+"</html>");

				inPass1.setText("");
				inPass2.setText("");
				inUser.requestFocus();
			}
		}
	}

	public boolean addUser(String user, String pass, String email) throws SQLException, NoSuchAlgorithmException {
		Connector con = Function.mysql();
		con.update("INSERT INTO users (username,password,email,timestamp) VALUES (?,?,?,?)", new String[] { "s", user },
		        new String[] { "s", Function.md5(pass) }, new String[] { "s", email },
		        new String[] { "l", Long.toString(Function.timestamp()) });
		return false;
	}

	public int checkRegister(String user, String pass1, String pass2, String email) throws SQLException {
		Connector con = Function.mysql();
		boolean bool;
		int in;
		in = Function.checkUsername(user);
		if(in == 1)
			return 1;
		else if(in == 2)
			return 2;
		if(!pass1.equals(pass2))
			return 3;
		in = Function.checkPassword(pass1);
		if(in == 1)
			return 4;
		else if(in == 2)
			return 5;
		else if(in == 3)
			return 6;
		if(!Function.checkEmail(email))
			return 7;
		bool = con.check("SELECT username FROM users WHERE username=?", user);
		if(bool)
			return 8;
		else return 9;
	}
}