package SceneBuild_JavaFX;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import QuickConnect.Connector;
import QuickConnect.Function;
import javafx.application.Application;
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

public class registerWindow extends Application implements EventHandler<ActionEvent> {

	private Stage myStage;
	private Scene myScene;
	private VBox RegisterFrame;
	FXMLLoader loader;
	@FXML
	Label lTitle, lUser, lPass, lNewPass, lMail, lRegister;
	@FXML
	Button bRegister, bBack;
	@FXML
	TextField inUser, inMail;
	@FXML
	PasswordField inPass, inNewPass;

	@Override
	public void start(Stage stage) {
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect- Register");
		this.myStage.setResizable(false);

		showRegisterFrame();
		
		myScene = new Scene(RegisterFrame);
		
		File file = new File("src/SceneBuild_JavaFX/standardLayout.css");
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
		
		loader = new FXMLLoader();
		loader.setLocation(registerWindow.class.getResource("RegisterFrame.fxml"));
		loader.setController(this);
		try {
			RegisterFrame = (VBox) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
//		bLogin.setOnAction(this);
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
				loginWindow Sf = new loginWindow();
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

				inPass.setText("");
				inNewPass.setText("");
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
