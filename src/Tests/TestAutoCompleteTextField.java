package Tests;

import java.util.Arrays;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestAutoCompleteTextField extends Application {
	
	private Stage myStage;
	private Scene myScene;
	private VBox test;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.myStage = stage;
		this.myStage.setTitle("QuickConnect");
		this.myStage.setWidth(50);
		test = new VBox();
		AutoCompleteTextField hy = new AutoCompleteTextField();
		hy.getEntries().addAll(Arrays.asList("u_09", "samilesma"));
		test.getChildren().add(hy);
		this.myScene = new Scene(test);
		this.myStage.setScene(this.myScene);
		this.myStage.show();
	}

	public static void main(String[] args) {
		Application.launch(TestAutoCompleteTextField.class, args);
	}
	
}