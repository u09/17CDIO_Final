package QuickConnect_GUI.JavaFX;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class startFrame extends Application{

	public static void main(String[] args) {
		launch(startFrame.class, args);
	}
	
	@Override
	public void start(Stage myStage) throws Exception {
		
		StackPane myPane = new StackPane();
		
		myPane.getChildren().add(addVBox());

		Scene myScene = new Scene(myPane, 300, 300);
		
		
		
		myStage.setScene(myScene);
		myStage.setTitle("Layout Sample");
        myStage.show();
		
		
		
	}

	private VBox addVBox() {
		
		VBox myVBox = new VBox();
		myVBox.setSpacing(12.0);
		
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
