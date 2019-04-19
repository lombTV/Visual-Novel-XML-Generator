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
	private String active;
	private StringBuilder inputText;
	private String[] parameters;

	@FXML
	private TextArea outputArea;
	@FXML
	private Button showDataButton;
	@FXML
	private Button changeViewButton, addTagButton;
	@FXML
	private RadioButton radioCharacter, radioMood, radioAnimation, radioBackground, radioEffect,
	radioMusic, radioDisplay, radioOptions, radioVoid, radioEnd, radioTitle, radioGoto;

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

	}

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

		}
	}


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
					case "title": finalText.append(addTitle(finalText)); break;
					case "goto": finalText.append(addGoto()); break;
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
				case "title": finalText.append(addTitle(finalText)); break;
				case "goto": finalText.append(addGoto()); break;
			}
		}

		//	 Append the current line to finalText.
		//	 If the current line == the caret's paragraph value, go through the switch
		// Append to InputArea
		MainController.oldInput = finalText.toString();
		changeView(e);
	}


	private String addGoto() {
		// One Parameter: DisplayName
		StringBuilder tag = new StringBuilder();
		tag.append("|GoTo:" + parameters[0]);
		return tag.toString();
	}

	private String addTitle(StringBuilder finalText) {
		// One Parameter: DisplayName
		finalText.insert(0,  "|title: " + parameters[0] + "\n");
		MainController.titleName = parameters[0];
		StringBuilder tag = new StringBuilder();
		tag.append("|endTitle:" + parameters[0]);
		return tag.toString();
	}

	private String addEnd() {
		// NO PARAMETERS
		StringBuilder tag = new StringBuilder();
		tag.append("|End");
		return tag.toString();
	}

	private String addVoid() {
		// NO PARAMETERS
		StringBuilder tag = new StringBuilder();
		tag.append("VOID:VOID");
		return tag.toString();

	}

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

	private String addDisplay() {
		// One Parameter: DisplayName
		StringBuilder tag = new StringBuilder();
		tag.append("|image:" + parameters[0]);
		return tag.toString();

	}

	private String addMusic() {
		// One Parameter: MusicName
		StringBuilder tag = new StringBuilder();
		tag.append("|music:" + parameters[0]);
		return tag.toString();

	}

	private String addEffect() {
		// One Parameter: EffectName
		StringBuilder tag = new StringBuilder();
		tag.append("|effect:" + parameters[0]);
		return tag.toString();

	}

	private String addBackground() {
		// One Parameter: BGName
		StringBuilder tag = new StringBuilder();
		tag.append("|background:" + parameters[0]);
		return tag.toString();

	}

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
