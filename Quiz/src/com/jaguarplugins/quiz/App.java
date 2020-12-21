package com.jaguarplugins.quiz;

import org.fxmisc.richtext.CodeArea;

import com.jaguarplugins.quiz.input.AccButton;
import com.jaguarplugins.quiz.input.Editor;
import com.jaguarplugins.quiz.input.Handler;
import com.jaguarplugins.quiz.input.QFile;
import com.jaguarplugins.quiz.questions.Mistake;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class App extends Application {

	private static Scene scene, optionScene;
	private static Stage mainStage;
	private int width = 800, height = 500;

//	Used by handler
	private static Text title, text, scoreText, helpText, hintText, mistakeDetail;
	private static TextField input;
	private static Button loadBtn, okBtn, helpBtn, hintBtn, skipBtn, newBtn, editBtn;
	private static ComboBox<String> selector, target;
	private static ListView<Mistake> mistakes;
	private Handler handler;
	private ChangeListener<? super Mistake> changeListener;

//	Editor
	private static Stage editorStage;
	private static CodeArea area;
	private static Button saveBtn, cancelBtn, formatBtn;
	
//	Accents
	private static AccButton eAc, eGr, eCr;
	private static AccButton aAc, aGr, aCr, aUm;
	private static AccButton iAc, iCr, iUm;
	private static AccButton oAc, oCr, oUm;
	private static AccButton uAc, uCr, uUm;
	private static AccButton cCd;
	private static AccButton nTl;
	private static AccButton eSt; //weird B in German
	private static HBox higherBottom;
	
//	Settings
	private static CheckBox french, spanish, german;
	private static CheckBox loseMistake, loseHelp, loseHint, loseSkip;
	
	private Image image = new Image("com/jaguarplugins/quiz/style/icon.jpg");
	
	public static void main(String[] args) {

		launch();

	}

	@Override
	public void start(Stage primaryStage) {

		handler = new Handler();

		setupOptions();
		
//		Centre Grid
		text = new Text();
		text.getStyleClass().add("largelabel");
		text.setFont(new Font("arial", 20));

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setMaxWidth(Double.MAX_VALUE);
		grid.setMaxHeight(Double.MAX_VALUE);

		grid.add(text, 0, 1);

//		Answer Region
		input = new TextField();
		input.setPadding(new Insets(10));
		input.setPrefWidth(400);
		input.getStyleClass().add("outline");

		okBtn = new Button();
		okBtn.setText("Submit");
		okBtn.setOnAction(handler.getButtonHandler());
		okBtn.setPadding(new Insets(8));
		okBtn.getStyleClass().add("outline");

		HBox lowerBottom = new HBox(input, okBtn);
		lowerBottom.setAlignment(Pos.CENTER);
		lowerBottom.setSpacing(4);
		lowerBottom.setPadding(new Insets(6));
		
		aAc = new AccButton("á");
		aGr = new AccButton("à");
		aCr = new AccButton("â");
		aUm = new AccButton("ä");
		eAc = new AccButton("é");
		eGr = new AccButton("è");
		eCr = new AccButton("ê");
		iAc = new AccButton("í");
		iCr = new AccButton("î");
		iUm = new AccButton("ï");
		oAc = new AccButton("ó");
		oCr = new AccButton("ô");
		oUm = new AccButton("ö");
		uAc = new AccButton("ú");
		uCr = new AccButton("û");
		uUm = new AccButton("ü");
		cCd = new AccButton("ç");
		nTl = new AccButton("ñ");
		eSt = new AccButton("ß");
		
		higherBottom = new HBox();
		higherBottom.setAlignment(Pos.CENTER);
		higherBottom.setSpacing(4);
		
		reloadOptions();
		
		VBox bottom = new VBox(higherBottom, lowerBottom);
		bottom.setAlignment(Pos.CENTER);
		
//		Right Pane		
		helpBtn = new Button("Help");
		helpBtn.setMinWidth(80);
		helpBtn.getStyleClass().add("outline");
		helpBtn.setAlignment(Pos.CENTER);
		helpBtn.setOnAction(handler.getButtonHandler());

		helpText = new Text();
		helpText.setId("small");
		helpText.setWrappingWidth(80);
		helpText.setTextAlignment(TextAlignment.CENTER);

		VBox helpBox = new VBox(helpBtn, helpText);
		helpBox.getStyleClass().add("box");
		helpBox.setAlignment(Pos.TOP_CENTER);

		hintBtn = new Button("Hint");
		hintBtn.setMinWidth(80);
		hintBtn.getStyleClass().add("outline");
		hintBtn.setAlignment(Pos.CENTER);
		hintBtn.setOnAction(handler.getButtonHandler());

		hintText = new Text();
		hintText.setId("small");
		hintText.setWrappingWidth(80);
		hintText.setTextAlignment(TextAlignment.CENTER);

		VBox hintBox = new VBox(hintBtn, hintText);
		hintBox.getStyleClass().add("box");
		hintBox.setAlignment(Pos.TOP_CENTER);

		skipBtn = new Button("Skip");
		skipBtn.setMinWidth(80);
		skipBtn.getStyleClass().add("outline");
		skipBtn.setAlignment(Pos.CENTER);
		skipBtn.setOnAction(handler.getButtonHandler());

		VBox skipBox = new VBox(skipBtn);
		skipBox.getStyleClass().add("box");

		VBox right = new VBox(helpBox, hintBox, skipBox);
		right.setPrefWidth(100);
		right.setMaxWidth(100);
		right.setAlignment(Pos.TOP_RIGHT);
		right.setPadding(new Insets(10));
		right.setSpacing(30);
		right.getStyleClass().add("rightside");

//		Left Pane
		scoreText = new Text("Score:\n" + " ");
		scoreText.setId("medtext");

		Label mistakeText = new Label("Mistakes:");
		mistakeText.setId("small");

		mistakes = new ListView<Mistake>();
		mistakes.getItems().add(Mistake.BLANK);
		mistakes.setId("blue-back");
		mistakes.getStyleClass().add("highlight");
		mistakes.setPrefHeight(155);
		mistakes.setMaxWidth(80);
		mistakes.getSelectionModel().getSelectedItem();

		mistakeDetail = new Text("Mistake:\n " + "\nAnswer:\n ");
		mistakeDetail.setId("small");
		mistakeDetail.setWrappingWidth(80);
		mistakeDetail.setTextAlignment(TextAlignment.LEFT);

		changeListener = new ChangeListener<Mistake>() {

			@Override
			public void changed(ObservableValue<? extends Mistake> observable, Mistake oldValue, Mistake newValue) {

				try {

					mistakeDetail.setText("Mistake:\n"
							+ App.getMistakes().getSelectionModel().getSelectedItem().getMistake() + "\nAnswer:\n"
							+ App.getMistakes().getSelectionModel().getSelectedItem().getAnswer());

				} catch (NullPointerException e) {
					mistakeDetail.setText("Mistake:\n " + "\nAnswer:\n ");
				}

			}
		};

		mistakes.getSelectionModel().selectedItemProperty().addListener(changeListener);

		VBox mistakeBox = new VBox(mistakeText, mistakes, mistakeDetail);
		mistakeBox.getStyleClass().add("box");
//		mistakeBox.setPrefHeight(230);

		VBox left = new VBox(scoreText, mistakeBox);
		left.setPrefWidth(100);
		left.setMaxWidth(100);
		left.setAlignment(Pos.TOP_LEFT);
		left.setPadding(new Insets(10));
		left.setSpacing(4);
		left.getStyleClass().add("leftside");

//		Menu				
		newBtn = new Button("New file");
		newBtn.setOnAction(handler.getButtonHandler());
		newBtn.getStyleClass().add("menubutton");
		
		editBtn = new Button("Edit file");
		editBtn.setOnAction(handler.getButtonHandler());
		editBtn.getStyleClass().add("menubutton");
		
		Label fileText = new Label("File: ");

		selector = new ComboBox<String>();
		selector.getItems().addAll(QFile.getFiles());
		selector.getItems().add("-- MISTAKES --");
		selector.setPrefWidth(150);
		selector.getStyleClass().add("menubutton");

		target = new ComboBox<String>();
		target.getItems().addAll("Eng - ?", "? - Eng");
		target.setPrefWidth(80);
		target.getStyleClass().add("menubutton");

		loadBtn = new Button("load");
		loadBtn.setOnAction(handler.getButtonHandler());
		loadBtn.getStyleClass().add("menubutton");

		HBox menu = new HBox(fileText, selector, target, loadBtn, new Label(" | "), newBtn, editBtn);
		menu.setAlignment(Pos.BOTTOM_CENTER);
		menu.setSpacing(10);
		menu.setPadding(new Insets(10));
		menu.getStyleClass().add("menu");

//		Main Setup
		BorderPane border = new BorderPane();
		border.setCenter(grid);
		border.setTop(menu);
		border.setBottom(bottom);
		border.setRight(right);
		border.setLeft(left);

		scene = new Scene(border);
		scene.getStylesheets().add("com/jaguarplugins/quiz/style/lake.css");
		scene.setOnKeyReleased(handler.getKeyHandler());

		setupEditor();
		
		primaryStage.getIcons().add(image);
		primaryStage.setScene(scene);
		primaryStage.setTitle("JaguarPlugins - Vocab Quiz");
		primaryStage.setWidth(width);
		primaryStage.setHeight(height);
		primaryStage.show();
		
		mainStage = primaryStage;
		
	}

	private void setupEditor() {
		
		Editor editor = new Editor();
		
		title = new Text("Quiz File - Editor");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setId("bold-title");
		
		area = new CodeArea();
		area.setPadding(new Insets(2));
		
		saveBtn = new Button("Save and Close");
		saveBtn.setOnAction(editor.getButtonHandler());
		saveBtn.setPadding(new Insets(4));
		
		cancelBtn = new Button("Cancel");
		cancelBtn.setOnAction(editor.getButtonHandler());
		cancelBtn.setPadding(new Insets(4));
		
		formatBtn = new Button("Format");
		formatBtn.setOnAction(editor.getButtonHandler());
		formatBtn.setPadding(new Insets(4));
		
		HBox buttonBox = new HBox(formatBtn, cancelBtn, saveBtn);
		buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
		buttonBox.setPadding(new Insets(5));
		buttonBox.setSpacing(20);
		
		BorderPane pane = new BorderPane();
		pane.setCenter(area);
		pane.setBottom(buttonBox);
		pane.setTop(title);
		
		Scene editorScene = new Scene(pane);
		editorScene.setOnKeyReleased(editor.getKeyHandler());
		editorScene.getStylesheets().add("com/jaguarplugins/quiz/style/edit.css");
		
		editorStage = new Stage();
		editorStage.getIcons().add(image);
		editorStage.setScene(editorScene);
		editorStage.setTitle("Quiz editor");
		editorStage.setWidth(width / 2);
		editorStage.setHeight(height);
		editorStage.toFront();
		
	}
	
	private void setupOptions() {
		
		Label accChars = new Label("Display Accented Characters");
		accChars.setId("subtitle");
		
		french = new CheckBox("French (á à â é è ê î ï ô û ç)");
		spanish = new CheckBox("Spanish (á é í ó ú ñ)");
		german = new CheckBox("German (ä ö ü ß)");
		
		Label score = new Label("Lose score for");
		score.setId("subtitle");
		
		loseMistake = new CheckBox("Mistakes");
		loseHelp = new CheckBox("Help");
		loseHint = new CheckBox("Hint");
		loseSkip = new CheckBox("Skip");
		
		VBox items = new VBox(accChars, french, spanish, german, score, loseMistake, loseHelp, loseHint, loseSkip);
		
		Label title = new Label("Settings:");
		title.setId("title");
		
		BorderPane root = new BorderPane();
		root.setTop(title);
		root.setCenter(items);
		
//		Default options
		french.setSelected(true);
		loseHelp.setSelected(true);
		
		optionScene = new Scene(root);
		optionScene.getStylesheets().add("com/jaguarplugins/quiz/style/option.css");
		optionScene.setOnKeyReleased(handler.getKeyHandler());

	}
	
	public static void reloadOptions() {
		
		higherBottom.getChildren().clear();
		
		if(french.isSelected() && spanish.isSelected() && german.isSelected()) {
			higherBottom.getChildren().addAll(aAc.getButton(), aGr.getButton(), aCr.getButton(), aUm.getButton(), eAc.getButton(), eGr.getButton(), 
				eCr.getButton(), iCr.getButton(), iUm.getButton(), oCr.getButton(), oUm.getButton(), uCr.getButton(), uUm.getButton(), cCd.getButton(), nTl.getButton(), eSt.getButton());
		}
		else if(french.isSelected() && spanish.isSelected()) {
			higherBottom.getChildren().addAll(aAc.getButton(), aGr.getButton(), aCr.getButton(), eAc.getButton(), eGr.getButton(), 
					eCr.getButton(), iAc.getButton(), iCr.getButton(), iUm.getButton(), oAc.getButton(), oCr.getButton(), uAc.getButton(), uCr.getButton(), cCd.getButton(), nTl.getButton());
		}
		else if(spanish.isSelected() && german.isSelected()) {
			higherBottom.getChildren().addAll(aAc.getButton(), aUm.getButton(), eAc.getButton(), iAc.getButton(), oAc.getButton(), oUm.getButton(), uAc.getButton(), uUm.getButton(), nTl.getButton(), eSt.getButton());
		}
		else if(french.isSelected() && german.isSelected()) {
			higherBottom.getChildren().addAll(aAc.getButton(), aGr.getButton(), aCr.getButton(), aUm.getButton(), eAc.getButton(), eGr.getButton(), 
					eCr.getButton(), iCr.getButton(), iUm.getButton(), oCr.getButton(), oUm.getButton(), uCr.getButton(), uUm.getButton(), cCd.getButton(), eSt.getButton());
		}
		else if(spanish.isSelected()) {
			higherBottom.getChildren().addAll(aAc.getButton(), eAc.getButton(), iAc.getButton(), oAc.getButton(), uAc.getButton(), nTl.getButton());
		}
		else if(german.isSelected()) {
			higherBottom.getChildren().addAll(aUm.getButton(), oUm.getButton(), uUm.getButton(), eSt.getButton());
		}
		else if(french.isSelected()) {
			higherBottom.getChildren().addAll(aAc.getButton(), aGr.getButton(), aCr.getButton(), eAc.getButton(), eGr.getButton(), 
					eCr.getButton(), iCr.getButton(), iUm.getButton(), oCr.getButton(), uCr.getButton(), cCd.getButton());
		}
		
	}
	
	public static void setText(String newText) {
		text.setText(newText);
	}

	public static void updateScore(int score, int totalScore) {
		scoreText.setText("Score:\n" + score + " / " + totalScore);
	}

	public static void sendHeld(String help) {
		helpText.setText(help);
	}

	public static void sendHint(String hint) {
		hintText.setText(hint);
	}

	public static void sendMistakeDetail(Mistake mistake) {
		mistakeDetail.setText("Mistake:\n" + mistake.getMistake() + "\nAnswer:\n" + mistake.getAnswer());
	}

	public static void addMistake(String mistake, String answer, String question) {
		mistakes.getItems().add(new Mistake(mistake, answer, question));
	}

	public static TextField getInput() {
		return input;
	}

	public static Button getOkBtn() {
		return okBtn;
	}

	public static Button getLoadBtn() {
		return loadBtn;
	}

	public static Button getHelpBtn() {
		return helpBtn;
	}

	public static Button getHintBtn() {
		return hintBtn;
	}

	public static Button getSkipButton() {
		return skipBtn;
	}
	
	public static Button getNewButton() {
		return newBtn;
	}

	public static ComboBox<String> getSelector() {
		return selector;
	}

	public static ListView<Mistake> getMistakes() {
		return mistakes;
	}
	
	public Scene getMainScene() {
		return scene;
	}
	
	public static CodeArea getArea() {
		return area;
	}

	public static Stage getEditorStage() {
		return editorStage;
	}
	
	public static Button getSaveBtn() {
		return saveBtn;
	}

	public static Button getCancelBtn() {
		return cancelBtn;
	}
	
	public static Button getFormatBtn() {
		return formatBtn;
	}
	
	public static void formatEditor() {
		
		int language = 0;
//		0 = Foreign, 1 = English
		
		char[] contents = App.getArea().getText().toCharArray();
		area.clear();
		
		for(char c : contents) {
			
			if(c == ':') {
				
				area.append(Character.toString(c), "colon");
				language = 1;
				
			} else if(c == '\n') {
				
				area.appendText("\n");
				language = 0;
				
			} else if(c == ',') {
				
				area.append(Character.toString(c), "comma");
				
			} else if(language == 0) {
				
				area.append(Character.toString(c), "foreign");
				
			} else if(language == 1) {
				
				area.append(Character.toString(c), "english");
				
			} else {
				area.appendText(Character.toString(c));
			}
			
		}
		
	}

	public static Button getEditBtn() {
		return editBtn;
	}
	
	public static Text getEditorTitle() {
		return title;
	}
	
	public static boolean isEnglish() {

		if (target.getValue() == "Eng - ?") {
			return false;
		} else if (target.getValue() == "? - Eng") {
			return true;
		} else {
			return false;
		}

	}

	public static Stage getMainStage() {
		return mainStage;
	}

	public static Scene getScene() {
		return scene;
	}

	public static Scene getOptionScene() {
		return optionScene;
	}

	public static CheckBox getLoseMistake() {
		return loseMistake;
	}

	public static CheckBox getLoseHelp() {
		return loseHelp;
	}

	public static CheckBox getLoseHint() {
		return loseHint;
	}

	public static CheckBox getLoseSkip() {
		return loseSkip;
	}

}
