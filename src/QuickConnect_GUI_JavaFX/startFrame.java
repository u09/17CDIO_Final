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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class startFrame extends Application{
	
	public static void main(String[] args) {
		launch(startFrame.class, args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		StackPane pane = new StackPane();
		pane.getChildren().add(addVBox());

		Scene scene = new Scene(pane, 300, 350);
		File file = new File("src\\layoutsamplecss\\standardLayout.css");
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
		
		bLogin.setOnAction(new EventHandler<ActionEvent>() {
            @FXML
            public void handle(ActionEvent event) {
            	String userIn = inUser.getText();
				String passIn = inPass.getText();
				Connector con = Function.mysql();
				boolean bool= false;
				try {
				 bool=con.check("SELECT username FROM users WHERE username=? AND password=?",userIn,Function.md5(passIn));
					System.out.println(bool);
				}catch (SQLException | NoSuchAlgorithmException e){
					e.printStackTrace();
				}
				
				if(bool== true){
//					loginFrame logFace = new loginFrame();
					Stage stage = new Stage();
					loginFrame Lf = new loginFrame();
	
						try {
							Lf.start(stage);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					startFrame fr = new startFrame();
					((Node)(event.getSource())).getScene().getWindow().hide();
				}
				
				/*
				if(userIn.equals("DTU") && passIn.equals("12345")) {
					loginFrame logFace = new loginFrame(userIn, passIn);
					logFace.setVisible(true);
					dispose();
				} 
				*/
				
				else{
					inUser.setText("");
					inPass.setText("");
					inUser.requestFocus();
				}
            }
        });
		
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
