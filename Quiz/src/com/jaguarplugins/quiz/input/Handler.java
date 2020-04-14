package com.jaguarplugins.quiz.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JOptionPane;

import com.jaguarplugins.quiz.App;
import com.jaguarplugins.quiz.questions.Mistake;
import com.jaguarplugins.quiz.questions.Question;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Handler {

	private ArrayList<Question> questions;
	private int q;
	private int score, totalScore;
	private StringBuilder hint = new StringBuilder();
	
	private static TextInputDialog td;

	private ButtonHandler buttonHandler;
	private KeyHandler keyHandler;

	public Handler() {

		buttonHandler = new ButtonHandler();
		keyHandler = new KeyHandler();

	}

//	Button Handler
	private class ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {

			if (e.getSource().equals(App.getOkBtn())) {

				nextQuestion();

			}

			if (e.getSource().equals(App.getLoadBtn())) {

				App.getInput().clear();
				
				if (App.getSelector().getValue() == null) {
					return;
				}
								
				try {
					App.getMistakes().getItems().clear();
					App.getMistakes().getItems().add(Mistake.BLANK);
					hint.delete(0, hint.length());
					App.sendHeld(" ");
					App.sendHint(" ");
					questions = QFile.loadFile("quizzes/" + App.getSelector().getValue() + ".txt");
					score = 0;
					totalScore = questions.size();
					App.updateScore(score, totalScore);
					q = (int) (Math.random() * questions.size());
					App.setText(questions.get(q).getQuestion(App.isEnglish())[0]);
				} catch (IndexOutOfBoundsException error) {
					JOptionPane.showMessageDialog(null, App.getSelector().getValue() + ".txt " + "is empty");
				}

			}

			if (e.getSource().equals(App.getHelpBtn())) {

				try {
					App.sendHeld(questions.get(q).getQuestion(!App.isEnglish())[0]);
					score -= 1;
					App.updateScore(score, totalScore);

				} catch (Exception ex) {
					App.sendHeld("I can't help you");
				}

				if (App.getMistakes().getItems().get(0).equals(Mistake.BLANK)) {
					App.getMistakes().getItems().clear();
				}
				App.addMistake("[HELP]", questions.get(q).getQuestion(!App.isEnglish())[0],
						questions.get(q).getQuestion(App.isEnglish())[0]);

			}

			if (e.getSource().equals(App.getHintBtn())) {

				try {
					hint.append(questions.get(q).getQuestion(!App.isEnglish())[0].charAt(hint.length()));
					App.sendHint(hint.toString());
				} catch (IndexOutOfBoundsException | NullPointerException e1) {
//					Deliberately Nothing
				}

			}

			if (e.getSource().equals(App.getSkipButton())) {

				hint.delete(0, hint.length());
				App.sendHeld(" ");
				App.sendHint(" ");

				int newQ = (int) (Math.random() * questions.size());
				while (newQ == q) {
					newQ = (int) (Math.random() * questions.size());
				}

				q = newQ;
				App.setText(questions.get(q).getQuestion(App.isEnglish())[0]);

			}
			
			if(e.getSource().equals(App.getNewButton())) {
				
				td = new TextInputDialog("");
				td.setTitle("New File");
				td.setContentText("File Name:");
				td.setHeaderText("Create new file in quizzes folder");
				Optional<String> result = td.showAndWait();
				
				if(!result.equals(Optional.empty()) && td.getResult().length() > 0) {
					
					File file = new File("quizzes/" + td.getResult() + ".txt");
					
					if(!file.exists()) {
	
						App.getEditorTitle().setText(td.getResult() + ".txt");
						App.getEditorStage().show();

					} else {
						Alert a = new Alert(AlertType.ERROR, "File already exists, please pick a new name or rename the existing file");
						a.setTitle("New File");
						a.showAndWait();
					}
					
				} else {
					
					Alert a = new Alert(AlertType.WARNING, "File creation cancelled");
					a.setTitle("New File");
					a.showAndWait();
					
				}
			
			}
			
			if(e.getSource().equals(App.getEditBtn())) {
				
				File editFile = QFile.getEditFile();
				
				if(editFile != null) {
					
					try {
						
						BufferedReader reader = new BufferedReader(
								   new InputStreamReader(
						                      new FileInputStream(editFile), "UTF8"));
						
						App.getArea().clear();
						String line = reader.readLine();
						while (line != null) {
							App.getArea().appendText(line + "\n");
							line = reader.readLine();
						}
						App.getArea().deletePreviousChar();
						reader.close();
						
						App.getEditorTitle().setText(editFile.getName());
						App.getEditorStage().show();
						App.formatEditor();
						
					} catch (UnsupportedEncodingException e1) {
						Alert a = new Alert(AlertType.ERROR, "Please change file encoding type to UTF-8 and try again");
						a.showAndWait();
					} catch (FileNotFoundException e1) {
						Alert a = new Alert(AlertType.ERROR, "File could not be found");
						a.showAndWait();
					} catch (IOException e1) {
						Alert a = new Alert(AlertType.ERROR, "I/O exception");
						a.showAndWait();
					}
					
					
				
				} else {
					
					Alert a = new Alert(AlertType.WARNING, "File edit cancelled");
					a.setTitle("Edit File");
					a.showAndWait();
					
				}
				
			}
				
		}

	}

//	KeyHandler
	private class KeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent e) {

			if (e.getCode().equals(KeyCode.ENTER)) {
				nextQuestion();
			}
			
			if (e.getCode().equals(KeyCode.ESCAPE)) {
				if (App.getMainStage().getScene().equals(App.getScene())) {
					App.getMainStage().setScene(App.getOptionScene());
				} else {
					App.getMainStage().setScene(App.getScene());
				}
				App.reloadOptions();
			}

		}

	}

	private void nextQuestion() {

		if (questions == null) {
			JOptionPane.showMessageDialog(null, "Please load a quiz");
			return;
		}

		try {

			String userInput = App.getInput().getText();

			if (questions.get(q).anyCorrect(!App.isEnglish(), userInput)) {
//				CORRECT

				hint.delete(0, hint.length());
				score++;
				App.updateScore(score, totalScore);
				App.sendHeld(" ");
				App.sendHint(" ");
				questions.remove(q);

				q = (int) (Math.random() * questions.size());
				App.setText(questions.get(q).getQuestion(App.isEnglish())[0]);

			} else {
//				WRONG

				if (App.getMistakes().getItems().get(0).equals(Mistake.BLANK)) {
					App.getMistakes().getItems().clear();
				}
				App.addMistake(userInput, questions.get(q).getQuestion(!App.isEnglish())[0],
						questions.get(q).getQuestion(App.isEnglish())[0]);

			}

			App.getInput().clear();

		} catch (IndexOutOfBoundsException e) {

			App.setText("Quiz over");

		}

	}

//	private boolean getAllPossible(boolean isEnglish) {
//		
////		String[] answers = questions.get(q).getQuestion(!App.isEnglish()).split(",");
//		
//		return isEnglish;
//		
//	}

//	CLASS GETTERS
	public ButtonHandler getButtonHandler() {
		return buttonHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

}
