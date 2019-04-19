package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SecondaryController implements Initializable {
	/**
	 * Stores the current choice found by clicking on a Radio Button.
	 */
	private String active;
	/**
	 * String Buiilder that stores the text that will be given back to the inputField in the Main Controller.
	 */
	private StringBuilder inputText;
	/**
	 * Array of strings that stores all the given parameters of the generated tag.
	 */
	private String[] parameters;

	@FXML
	private TextArea outputArea;
	@FXML
	private Button showDataButton;
	@FXML
	private Button changeViewButton, addTagButton;
	@FXML
	private RadioButton radioCharacter, radioMood, radioAnimation, radioBackground, radioEffect,
	radioMusic, radioDisplay, radioOptions, radioVoid, radioEnd, radioTitle, radioGoto, radioExtra;

	/**
	 * <p>This method is run as soon as SecondaryController is loaded by the MainController class.
	 * </p>
	 * @param location Used by the Controller.
	 * @param resources Used by the Controller.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("SecondaryController initalized...");
		radioCharacter.setOnAction(e -> setActive("character"));
		radioMood.setOnAction(e -> setActive("mood"));
		radioAnimation.setOnAction(e -> setActive("animation"));
		radioBackground.setOnAction(e -> setActive("background"));
		radioEffect.setOnAction(e -> setActive("effect"));
		radioMusic.setOnAction(e -> setActive("music"));
		radioDisplay.setOnAction(e -> setActive("display"));
		radioOptions.setOnAction(e -> setActive("options"));
		radioVoid.setOnAction(e -> setActive("void"));
		radioEnd.setOnAction(e -> setActive("end"));
		radioTitle.setOnAction(e -> setActive("title"));
		radioGoto.setOnAction(e -> setActive("goto"));
		radioExtra.setOnAction(e -> setActive("extra"));
	}

	/**
	 * <p>Sets the "Parameters" text from the outputArea depending on which Radio Button is selected.
	 * </p>
	 * @param i The current parameter based on the chosen Radio Button..
	 */
	private void setActive(String i) {
		active = i;

		switch(i) {
		case "character": outputArea.setPromptText("Parameters: CharaNumber, CharaName"); break;
		case "mood": outputArea.setPromptText("Parameters: MoodNumber, MoodName"); break;
		case "animation": outputArea.setPromptText("Parameters: AnimNumber, AnimName, AnimArgument\nAnimations: Enter(), "
				+ "MoveX(pos), MoveY(pos), Sway(), StopSine(), Close(), Retreat(), Leave(), Screen_Shake()."); break;
		case "background": outputArea.setPromptText("Parameters: BGName"); break;
		case "effect": outputArea.setPromptText("Parameters: EffectName \nEffects: Trippy, Mud, Hide"); break;
		case "music": outputArea.setPromptText("Parameters: MusicName"); break;
		case "display": outputArea.setPromptText("Parameters: DisplayName \nDisplays: Hide."); break;
		case "options": outputArea.setPromptText("Parameters: OptionsNumber, Option1, Option1Tag, Option2, Option2Tag, Option3, Option3Tag, Option4, Option4Tag"); break;
		case "void": outputArea.setPromptText("Parameters: None."); break;
		case "end": outputArea.setPromptText("Parameters: None."); break;
		case "title": outputArea.setPromptText("Parameters: TitleName."); break;
		case "goto": outputArea.setPromptText("Parameters: LayoutName."); break;
		case "extra": outputArea.setPromptText("Parameters: ExtraName."); break;

		}
	}

	/**
	 * <p>Run when the "Add Tag" button is clicked. Adds the chosen tag to the current line of the inputField, then goes back to the MainController.
	 * </p>
	 * @param e Used by the Controller.
	 */
	public void addTag(ActionEvent e) {
		StringBuilder finalText = new StringBuilder();
		inputText = new StringBuilder();
		parameters = outputArea.getText().split(", ");
		// Get InputArea Text
		inputText.append(MainController.oldInput);
		String[] lines = inputText.toString().split("\\n");
		// For each line of inputText...
		for (int i = 0; i < lines.length; i++) {
			finalText.append(lines[i]);
			if (i == MainController.caretValue) {
				switch(active) {
					case "character": finalText.append(addCharacter()); break;
					case "mood": finalText.append(addMood()); break;
					case "animation": finalText.append(addAnimation()); break;
					case "background": finalText.append(addBackground()); break;
					case "effect": finalText.append(addEffect()); break;
					case "music": finalText.append(addMusic()); break;
					case "display": finalText.append(addDisplay()); break;
					case "options": finalText.append(addOptions()); break;
					case "void": finalText.append(addVoid()); break;
					case "end": finalText.append(addEnd()); break;
					case "title":
						finalText.append(addTitle(finalText) + "\n");
						finalText.append("|endTitle:" + parameters[0]);
						break;
					case "goto": finalText.append(addGoto()); break;
					case "extra": finalText.append(addExtra()); break;
				}
			}
			finalText.append("\n");

		}

		if (MainController.caretValue == lines.length) {
			switch(active) {
				case "character": finalText.append(addCharacter()); break;
				case "mood": finalText.append(addMood()); break;
				case "animation": finalText.append(addAnimation()); break;
				case "background": finalText.append(addBackground()); break;
				case "effect": finalText.append(addEffect()); break;
				case "music": finalText.append(addMusic()); break;
				case "display": finalText.append(addDisplay()); break;
				case "options": finalText.append(addOptions()); break;
				case "void": finalText.append(addVoid()); break;
				case "end": finalText.append(addEnd()); break;
				case "goto": finalText.append(addGoto()); break;
				case "extra": finalText.append(addExtra()); break;
			}
		}

		if (active == "title" && MainController.caretValue >= lines.length) {
			finalText.append(addTitle(finalText) + "\n");
			finalText.append("|endTitle:" + parameters[0]);
		}

		//	 Append the current line to finalText.
		//	 If the current line == the caret's paragraph value, go through the switch
		// Append to InputArea
		MainController.oldInput = finalText.toString();
		changeView(e);
	}

	/**
	 * <p>Adds the Extra tag.
	 * </p>
	 */
	private String addExtra() {
		// One Parameter: ExtraName
		StringBuilder tag = new StringBuilder();
		tag.append("|extra:" + parameters[0]);
		return tag.toString();
	}
	/**
	 * <p>Adds the Goto tag.
	 * </p>
	 */
	private String addGoto() {
		// One Parameter: DisplayName
		StringBuilder tag = new StringBuilder();
		tag.append("|GoTo:" + parameters[0]);
		return tag.toString();
	}
	/**
	 * <p>Adds the Title tag.
	 * </p>
	 * @param finalText Unused String Builder. TODO remove.
	 */
	private String addTitle(StringBuilder finalText) {
		// One Parameter: DisplayName
		MainController.titleName = parameters[0];
		StringBuilder tag = new StringBuilder();
		tag.append("|title:" + parameters[0]);
		return tag.toString();
	}
	/**
	 * <p>Adds the End tag.
	 * </p>
	 */
	private String addEnd() {
		// NO PARAMETERS
		StringBuilder tag = new StringBuilder();
		tag.append("|End");
		return tag.toString();
	}
	/**
	 * <p>Adds the Void tag.
	 * </p>
	 */
	private String addVoid() {
		// NO PARAMETERS
		StringBuilder tag = new StringBuilder();
		tag.append("VOID:VOID");
		return tag.toString();

	}
	/**
	 * <p>Adds the Options tag.
	 * </p>
	 */
	private String addOptions() {
		StringBuilder tag = new StringBuilder();
		int OptionsNumber = Integer.parseInt(parameters[0]);
		// Nine Parameters: OptionsNumber, Option1, Option1Tag, Option2, Option2Tag, Option3, Option3Tag, Option4, Option4Tag
		// Minimum Three Parameters, Maximum Nine
		// If OptionsNumber = 1, Use Option1 and Option1Tag only. If OptionsNumber = 2, Use Option1 and Option2.
		tag.append("options:[");
		tag.append("[" + parameters[1] + "|" + parameters[2] + "]");
		if (OptionsNumber > 1) {
			tag.append(", [" + parameters[3] + "|" + parameters[4] + "]");
		}
		if (OptionsNumber > 2) {
			tag.append(", [" + parameters[5] + "|" + parameters[6] + "]");
		}
		if (OptionsNumber > 3) {
			tag.append(", [" + parameters[7] + "|" + parameters[8] + "]");
		}
		tag.append("]");
		return tag.toString();

	}
	/**
	 * <p>Adds the Display tag.
	 * </p>
	 */
	private String addDisplay() {
		// One Parameter: DisplayName
		StringBuilder tag = new StringBuilder();
		tag.append("|image:" + parameters[0]);
		return tag.toString();

	}
	/**
	 * <p>Adds the Music tag.
	 * </p>
	 */
	private String addMusic() {
		// One Parameter: MusicName
		StringBuilder tag = new StringBuilder();
		tag.append("|music:" + parameters[0]);
		return tag.toString();

	}
	/**
	 * <p>Adds the Effect tag.
	 * </p>
	 */
	private String addEffect() {
		// One Parameter: EffectName
		StringBuilder tag = new StringBuilder();
		tag.append("|effect:" + parameters[0]);
		return tag.toString();

	}
	/**
	 * <p>Adds the Background tag.
	 * </p>
	 */
	private String addBackground() {
		// One Parameter: BGName
		StringBuilder tag = new StringBuilder();
		tag.append("|background:" + parameters[0]);
		return tag.toString();

	}
	/**
	 * <p>Adds the Animation tag.
	 * </p>
	 */
	private String addAnimation() {
		// Three Parameters: AnimNumber, AnimName, AnimArgument
		StringBuilder tag = new StringBuilder();
		tag.append("|anim");
		if (!parameters[0].equals("0")) {
			tag.append(parameters[0]);
		}
		tag.append(":" + parameters[1]);
		if (parameters.length > 2) {
			tag.append(" " + parameters[2]);
		}
		return tag.toString();

	}
	/**
	 * <p>Adds the Mood tag.
	 * </p>
	 */
	private String addMood() {
		// Two Parameters: MoodNumber, MoodName
		StringBuilder tag = new StringBuilder();
		tag.append("|mood");
		if (!parameters[0].equals("0")) {
			tag.append(parameters[0]);
		}
		tag.append(":" + parameters[1]);
		return tag.toString();

	}
	/**
	 * <p>Adds the Character tag.
	 * </p>
	 */
	private String addCharacter() {
		// Two Parameters: CharaNumber, CharaName
		StringBuilder tag = new StringBuilder();
		tag.append("|chara");
		if (!parameters[0].equals("0")) {
			tag.append(parameters[0]);
		}
		tag.append(":" + parameters[1]);
		return tag.toString();

	}
	/**
	 * <p>Changes the view to the MainController.
	 * </p>
	 * @param e Used by the Controller.
	 */
	public void changeView(ActionEvent e) {
		try {
			/* Loads the secondary FXML Scene */
			Parent root = FXMLLoader.load(getClass().getResource("/FXML/MainFXML.fxml"));
			System.out.println("MainFXML.fxml has loaded successfully.");
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
