package Controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/**
 * @author Matthew Lombardo
 */
public class MainController implements Initializable {

	/**
	 * Stores the value of the text inside of the input field. When MainController is re-run, oldInput is the text of the input field.
	 */
	static String oldInput;
	/**
	 * Stores the current title value. If title is not empty, the program handles loading/saving differently.
	 */
	static String titleName;
	/**
	 * Stores the current placement of the caret. This is used to put a tag on the current line.
	 */
	static int caretValue;

	@FXML
	private Button getDataButton;
	@FXML
	private Button changeViewButton;
	@FXML
	private MenuItem menuLoad, menuSave, menuClose, menuAbout;
	@FXML
	private StyleClassedTextArea richTextFX;

	/**
	 * <p>This method is run as soon as MainController is loaded by the Main class.
	 * </p>
	 * @param arg0 Used by the Controller.
	 * @param arg1 Used by the Controller.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		richTextFX.replaceText(oldInput);
		System.out.println("Controller Initialized...");
		menuSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		menuLoad.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
	}
	/**
	 * <p>Closes the program.
	 * </p>
	 * @param e Used by the Controller.
	 */
	public void closeProgram(ActionEvent e) {
		Platform.exit();
		System.exit(0);
	}

	/**
	 * <p>Shows the credits of the program.
	 * </p>
	 * @param e Used by the Controller.
	 */
	public void showAbout(ActionEvent e) {
		Alert alert = new Alert(AlertType.INFORMATION, "Credits:\nProgram written by Matthew Lombardo. \nXML Syntax designed by Eleo Espinosa.", ButtonType.OK);
		alert.showAndWait();
	}

	/**
	 * <p>Clears the input field's text.
	 * </p>
	 * @param e Used by the Controller.
	 */
	public void getData(ActionEvent e) {
		System.out.println("Get Data button clicked.");
		richTextFX.clear();
	}
	/**
	 * <p>Opens a File Chooser and allows the user to open a .xml file, which is then parsed to plain text for use in the editor.
	 * </p>
	 * @param e Used by the Controller.
	 */
	public void loadXML(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

		fileChooser.setTitle("Open XML File");
		File file = fileChooser.showOpenDialog(Main.primaryStage);

        if (file != null) {
            String text = getTextFromFile(file);

            // Loop through and remove XML tags and new line symbols
            String[] lines = text.split("\\n");
            StringBuilder finalText = new StringBuilder();
            String theTitle = "";
            for (int i = 0; i < lines.length; i++) {
            	if (lines[i].contains("<node>")) {
            		continue;
            	}

            	if (lines[i].contains("<title>")) {
            		String title = lines[i];
            		title = title.replace("		<title>", "");
            		title = title.replace("</title>", "");
            		theTitle = title;
            		finalText.append("|title:" + title + "\n");
            		titleName = title;
            		continue;
            	}


            	if (lines[i].contains("<body")) {
            		// get everything after the body tag
            		lines[i] = lines[i].replace("<body id=''>", "");
            		lines[i] = lines[i].replace("	", "");
            		lines[i] = lines[i].replace("\\n", "");
            		finalText.append(lines[i] + "\n");
            		continue;
            	}

            	if (lines[i].contains("</body")) {
            		continue;
            	}

            	if (lines[i].contains("<tags>")) {
            		continue;
            	}
            	if (lines[i].contains("<position")) {
            		continue;
            	}
            	if (lines[i].contains("<colorID>")) {
            		continue;
            	}
            	if (lines[i].contains("</node>")) {
            		String title = lines[i];
            		if (title.contains("</node>")) {
            			title = "|endTitle:";
            		}
            		finalText.append(title + theTitle + "\n");
            		continue;
            	}
            	lines[i] = lines[i].replace("\\n", "");
            	if (lines[i].equals("")) continue;
            	finalText.append(lines[i] + "\n");
            }

            richTextFX.replaceText(finalText.toString());
        }
	}
	/**
	 * <p>Reads a file and returns it as a plain text string.
	 * </p>
	 * @param file The given file.
	 * @return the file in plain text.
	 */
	private String getTextFromFile(File file) {

	    String content = null;
	    FileReader reader = null;
	    try {
	        reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        content = new String(chars);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if(reader != null){
	            try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	    return content;
	}

	/**
	 * <p>Saves the plain text in .xml format.
	 * </p>
	 * @param e Used by the Controller.
	 */
	public void saveXML(ActionEvent e) {
		// Get String
		StringBuilder input = new StringBuilder();
		input.append(richTextFX.getText());
		System.out.println("Rich Text: " + richTextFX.getText());


		if (titleName != "") {
			Pattern p;
			Matcher m;

			p = Pattern.compile("\\|endTitle(.*)");
			m = p.matcher(input);
			input = new StringBuilder(m.replaceAll(""));
			p = Pattern.compile("\\|title(.*)");
			m = p.matcher(input);
			input = new StringBuilder(m.replaceAll(""));

			String[] lines = input.toString().split("\\n");
			input = new StringBuilder();
			for (int i = 0; i < lines.length; i++) {
				if (i > 0) {
					input.append(lines[i]);
					if (i < lines.length) input.append("\\n\n");
				}
			}
		} else {

			String[] lines = input.toString().split("\\n");
			input = new StringBuilder();
			for (int i = 0; i < lines.length; i++) {
				if (i >= 0) {
					input.append(lines[i] + "\\n\n");
				}
			}
		}

		// Add new line symbol


		System.out.println(input);
		// Parse it to XML
		StringBuilder XMLBuilder = new StringBuilder();
		String[] originalLines = richTextFX.getText().split("\\n");
		String[] parsedLines = input.toString().split("\\n");
		for (int i = 0; i < originalLines.length; i++) {
			// If the line has a |title tag, increment i by 1, and add the node, title, tags, and body lines.
			if (originalLines[i].contains("|title")) {
				titleName = originalLines[i].split(":")[1];
				XMLBuilder.append("	<node>\n");
				XMLBuilder.append("		<title>" + titleName + "</title>\n");
				XMLBuilder.append("		<tags></tags>\n");
				XMLBuilder.append("		<body id=''>");
				XMLBuilder.append(parsedLines[i] + "\n");
			} else if (originalLines[i].contains("|endTitle")) {
				XMLBuilder.append("</body>\n");
				XMLBuilder.append("		<position x=\"0\" y=\"0\"></position>\n");
				XMLBuilder.append("		<colorID>0</colorID>\n");
				XMLBuilder.append("	</node>\n");
			} else {
				if (i < parsedLines.length) {
					if (parsedLines[i].equals("\\n")) {
						continue;
					}
					XMLBuilder.append(parsedLines[i] + "\n");
				}
			}
			// If the line has a |endTitle tag, increment i by 1, then finish the XML node.
		}


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
	/**
	 * <p>Saves a string of text to a file.
	 * </p>
	 *
	 * @param content The string that is to be stored as a file.
	 * @param file The chosen file that the content will be saved to.
	 */
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
	/**
	 * <p>Changes the view to the SecondaryController.
	 * </p>
	 * @param e Used by the Controller.
	 */
	public void changeView(ActionEvent e) {
		oldInput = richTextFX.getText();
		try {
			/* Loads the secondary FXML Scene */
			caretValue = richTextFX.getCurrentParagraph();
			System.out.println("Opening SecondaryFXML.fxml...");
			Parent root = FXMLLoader.load(getClass().getResource("/FXML/SecondaryFXML.fxml"));
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
