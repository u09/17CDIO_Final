package QuickConnect_GUI_JavaFX;

import java.io.File;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import QuickConnect.Connector;
import QuickConnect.Function;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class loginFrame extends Application{
	
	public static void main(String[] args) {
		launch(loginFrame.class, args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		StackPane pane = new StackPane();
		pane.getChildren().add(addVBox());

		Scene scene = new Scene(pane, 300, 350);
		File file = new File("C:\\Users\\Samil\\Documents\\GitHub\\17CDIO_Final\\src\\QuickConnect_GUI_JavaFX\\standardLayout.css");
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
		TextField inUser = new TextField("Brugernavn");
		inUser.setMaxSize(150, 20);
		Text lPass = new Text("Indtast dit password:");
		PasswordField inPass = new PasswordField();
		inPass.setMaxSize(150, 20);
		Button bLogin = new Button("Login");
		Text lNoUser = new Text("Har du ikke nogen bruger?");
		Text lRegister = new Text("Registrer her:");
		Button bRegister = new Button("Registrer");
		
		myVBox.getChildren().addAll(lTitle, lUser, inUser, lPass, inPass, bLogin, lNoUser, lRegister, bRegister);
		
		return myVBox;
	}
	
	/**
	 * 
	 * @param style - NORMAL: 0, BOLD: 1
	 * @param size
	 * @return
	 */
	public Font getMyFont(int style, int size) {
		
		Font myFont;
		String myFontName = "Iowan Old Style";
		
		switch(style) {
		
		case 0: return myFont = Font.font(myFontName, FontWeight.NORMAL, size);
		case 1: return myFont = Font.font(myFontName, FontWeight.BOLD, size);
		default: return myFont = Font.font(myFontName, FontWeight.NORMAL, size);
		
		}

	}
}
