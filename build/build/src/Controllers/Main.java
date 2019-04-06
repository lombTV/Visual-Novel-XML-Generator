package Controllers;
	
import DataStructures.StringBag;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	private static StringBag theBag;
	static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			theBag = new StringBag(50);
			System.out.println("Opening mainFXML.fxml...");
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/FXML/MainFXML.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Jobbold XML Generator");
			primaryStage.show();
			Main.primaryStage = primaryStage;
			System.out.println("Application started successfully.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static StringBag getBag() {
		return theBag;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
