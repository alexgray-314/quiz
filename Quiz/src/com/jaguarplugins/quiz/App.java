package com.jaguarplugins.quiz;

import com.jaguarplugins.quiz.input.Handler;
import com.jaguarplugins.quiz.input.QFile;

import javafx.application.Application;
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
import javafx.stage.Stage;

public class App extends Application {
	
	private Scene scene;
	private int width = 700, height = 400;
	
//	Used by handler
	private static Label text, scoreText, helpText;
	private static TextField input;
	private static Button loadBtn, okBtn, helpBtn;
	private static ComboBox<String> selector, target;
	private static ListView<String> mistakes;
	private Handler handler;
	
	public static void main(String[] args) {
		
//		VERSION 1.1
		launch();

	}
	
	@Override
	public void start(Stage primaryStage) {
		
		handler = new Handler();
		
//		TODO tick and cross
		
//		Centre Grid
		text = new Label();
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
		helpText = new Label();
		helpText.setId("small");
		
		helpBtn = new Button("Help!");
		helpBtn.getStyleClass().add("outline");
		helpBtn.setOnAction(handler.getButtonHandler());
		
		VBox right = new VBox(helpBtn, helpText);
		right.setPrefWidth(100);
		right.setMaxWidth(100);
		right.setAlignment(Pos.TOP_RIGHT);
		right.setPadding(new Insets(10));
		right.setSpacing(4);
		right.getStyleClass().add("rightside");
		
//		Left Pane
		scoreText = new Label("Score:\n" + " ");
		
//		TODO add mistakes list
		Label mistakeText = new Label("Mistakes:");
		mistakeText.setId("small");
		
		mistakes = new ListView<String>();
		mistakes.getItems().add("");
		mistakes.setId("blue-back");
		mistakes.setPrefHeight(200);
		
		VBox mistakeBox = new VBox(mistakeText, mistakes);
		mistakeBox.getStyleClass().add("box");
		mistakeBox.setPrefHeight(200);

		VBox left = new VBox(scoreText, mistakeBox);
		left.setPrefWidth(100);
		left.setMaxWidth(100);
		left.setAlignment(Pos.TOP_LEFT);
		left.setPadding(new Insets(10));
		left.setSpacing(4);
		left.getStyleClass().add("leftside");
		
//		Menu	
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
		
		HBox menu = new HBox(fileText, selector, target, loadBtn);
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
	
	public static void addMistake(String mistake) {
		mistakes.getItems().add(mistake);
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

	public static ComboBox<String> getSelector() {
		return selector;
	}
	
	public static ListView<String> getMistakes() {
		return mistakes;
	}

	public static boolean isEnglish() {
		
		if(target.getValue() == "Eng - ?") {
			return false;
		} else if(target.getValue() == "? - Eng") {
			return true;
		} else {
			return false;
		}
	
	}

}
