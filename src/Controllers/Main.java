package Controllers;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

	static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			MainController.titleName = "";
			MainController.oldInput = "";
			System.out.println("Opening mainFXML.fxml...");
			Parent root = FXMLLoader.load(getClass().getResource("/FXML/MainFXML.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("VisualNovel XML Generator");
			primaryStage.show();
			Main.primaryStage = primaryStage;
			System.out.println("Application started successfully.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
