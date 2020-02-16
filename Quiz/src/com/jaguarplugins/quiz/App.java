package com.jaguarplugins.quiz;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class App extends Application {

	private Scene scene;
	private int width = 800, height = 500;

//	Used by handler
	private static Text text, scoreText, helpText, hintText, mistakeDetail;
	private static TextField input;
	private static Button loadBtn, okBtn, helpBtn, hintBtn, skipBtn, newBtn;
	private static ComboBox<String> selector, target;
	private static ListView<Mistake> mistakes;
	private Handler handler;
	private ChangeListener<? super Mistake> changeListener;

	public static void main(String[] args) {

//		VERSION 1.7.1		
		launch();

	}

	@Override
	public void start(Stage primaryStage) {

		handler = new Handler();

//		TODO tick and cross

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

		HBox bottom = new HBox(input, okBtn);
		bottom.setAlignment(Pos.CENTER);
		bottom.setSpacing(4);
		bottom.setPadding(new Insets(6));
//		TODO ProgressBar

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
		
		Label fileText = new Label("File: ");

		selector = new ComboBox<String>();
		selector.getItems().addAll(QFile.getFiles());
		selector.setPrefWidth(150);
		selector.getStyleClass().add("menubutton");

		target = new ComboBox<String>();
		target.getItems().addAll("Eng - ?", "? - Eng");
		target.setPrefWidth(80);
		target.getStyleClass().add("menubutton");

		loadBtn = new Button("load");
		loadBtn.setOnAction(handler.getButtonHandler());
		loadBtn.getStyleClass().add("menubutton");

		HBox menu = new HBox(fileText, selector, target, loadBtn, new Label(" | "), newBtn);
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

		primaryStage.setScene(scene);
		primaryStage.setTitle("JaguarPlugins - Vocab Quiz");
		primaryStage.setWidth(width);
		primaryStage.setHeight(height);
		primaryStage.show();

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

	public static boolean isEnglish() {

		if (target.getValue() == "Eng - ?") {
			return false;
		} else if (target.getValue() == "? - Eng") {
			return true;
		} else {
			return false;
		}

	}

}
