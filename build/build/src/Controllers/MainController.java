package Controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataStructures.StringBag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {
	private StringBag theBag;
	static String oldInput;
	
	@FXML
	private TextArea inputField;
	@FXML
	private Button getDataButton;
	@FXML
	private Button changeViewButton;
	@FXML
	private MenuItem menuSave;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inputField.setText(oldInput);
		theBag = Main.getBag();
		System.out.println("Controller Initialized...");
		menuSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
	}
	
	public void getData(ActionEvent e) {
		System.out.println("Get Data button clicked.");
		theBag = Main.getBag();
		theBag.insert(inputField.getText());
		inputField.clear();
	}
	
	public void saveXML(ActionEvent e) {
		// Get String
		String input = inputField.getText();
		input = input.replace("\n","\\n\n");
		System.out.println(input);
		// Parse it to XML
		StringBuilder XMLBuilder = new StringBuilder();
		XMLBuilder.append("	<node>\n");
		XMLBuilder.append("		<title></title>\n");
		XMLBuilder.append("		<tags></tags>\n");
		XMLBuilder.append("		<body id=''>" + input + "\\n</body>\n");
		XMLBuilder.append("		<position x=\"310\" y=\"153\"></position>\n");
		XMLBuilder.append("		<colorID>0</colorID>\n");
		XMLBuilder.append("	</node>\n");
		
		
		// Pick save location

        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(Main.primaryStage);

        if (file != null) {
            saveTextToFile(XMLBuilder.toString(), file);
        }
	}
	
	private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	public void changeView(ActionEvent e) {
		oldInput = inputField.getText();
		try {
			/* Loads the secondary FXML Scene */
			System.out.println("Opening SecondaryFXML.fxml...");
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/FXML/SecondaryFXML.fxml"));
			System.out.println("SecondaryFXML.fxml has loaded successfully.");
			Scene scene = new Scene(root);
			// Capture the original stage
			Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.show();
			System.out.println("View has been changed successfully.");
		} catch (IOException e1) {
			System.err.println("Error! IOException. (This should never happen)");
			e1.printStackTrace();
		}
	}

}
