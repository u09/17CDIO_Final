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
import javafx.fxml.FXML;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class startFrame extends Application implements EventHandler<ActionEvent> {

	TextField inUser;
	PasswordField inPass;
	Button bLogin;
	Button bRegister;

	public static void main(String[] args) {
		launch(startFrame.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		StackPane pane = new StackPane();
		pane.getChildren().add(addVBox());

		Scene scene = new Scene(pane, 300, 350);
		File file = new File("src/QuickConnect_GUI_JavaFX/standardLayout.css");
		URL url = file.toURI().toURL();
		scene.getStylesheets().add(url.toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.setTitle("QuickConnect");
		stage.show();
	}

	private VBox addVBox() {

		VBox myVBox = new VBox();
		myVBox.setSpacing(12.0);
		myVBox.setAlignment(Pos.CENTER);

		Text lTitle = new Text("Velkommen til QuickConnect");
		lTitle.setFont(getMyFont(1, 20));
		Text lUser = new Text("Indtast dit brugernavn:");
		inUser = new TextField("Brugernavn");
		inUser.setMaxSize(150, 20);
		Text lPass = new Text("Indtast dit password:");
		inPass = new PasswordField();
		inPass.setMaxSize(150, 20);
		bLogin = new Button("Login");
		bLogin.setOnAction(this);
		Text lNoUser = new Text("Har du ikke nogen bruger?");
		Text lRegister = new Text("Registrer her:");
		bRegister = new Button("Registrer");
		bRegister.setOnAction(this);

		myVBox.getChildren().addAll(lTitle, lUser, inUser, lPass, inPass, bLogin, lNoUser, lRegister, bRegister);

		return myVBox;
	}

	@Override
	public void handle(ActionEvent event) {
		// handle for bLogin
		if(event.getSource() == bLogin) {
			String userIn = inUser.getText();
			String passIn = inPass.getText();
			Connector con = Function.mysql();
			boolean bool = false;
			try {
				bool = con.check("SELECT username FROM users WHERE username=? AND password=?", userIn, Function.md5(passIn));
				System.out.println(bool);
			} catch(SQLException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			if(bool == true) {
				Stage stage = new Stage();
				programFrame pf = new programFrame();

				try {
					pf.start(stage);
				} catch(Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				((Node) (event.getSource())).getScene().getWindow().hide();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Forkert kode");
				alert.setHeaderText("Forkert kodeord eller brugernavn");
				alert.setContentText("Venligst indtast dit kodeord og brugernavn igen");

				alert.showAndWait();
				inPass.setText("");
				inUser.requestFocus();
			}
		}
		// handle for bRegister
		if(event.getSource() == bRegister) {
			Stage stage = new Stage();
			registerFrame Rf = new registerFrame();
			try {
				Rf.start(stage);
			} catch(Exception e) {
				e.printStackTrace();
			}
			((Node) (event.getSource())).getScene().getWindow().hide();
		}

	}

	/**
	 * Returns the font with a given style and size
	 * @param style - NORMAL: 0, BOLD: 1
	 * @param size
	 * @return
	 */
	public static Font getMyFont(int style, int size) {

		Font myFont;
		String myFontName = "Iowan Old Style";

		switch(style) {

		case 0: return myFont = Font.font(myFontName, FontWeight.NORMAL, size);
		case 1: return myFont = Font.font(myFontName, FontWeight.BOLD, size);
		default: return myFont = Font.font(myFontName, FontWeight.NORMAL, size);

		}

	}

}
