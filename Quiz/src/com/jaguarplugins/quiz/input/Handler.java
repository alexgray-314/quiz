package com.jaguarplugins.quiz.input;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jaguarplugins.quiz.App;
import com.jaguarplugins.quiz.questionss.Question;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Handler {
	
	private ArrayList<Question> questions;
	private int q;
	private int score, totalScore;
	private StringBuilder hint = new StringBuilder();
	
	private ButtonHandler buttonHandler;
	private KeyHandler keyHandler;
	private MouseHandler mouseHandler;
	
	public Handler() {
		
		buttonHandler = new ButtonHandler();
		keyHandler = new KeyHandler();
		mouseHandler = new MouseHandler();
		
	}
	
//	Button Handler
	private class ButtonHandler implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent e) {
			
			if(e.getSource().equals(App.getOkBtn())) {
				
				nextQuestion();
			
			}
			
			if(e.getSource().equals(App.getLoadBtn())) {
				
				if(App.getSelector().getValue() == null) {
					return;
				}
				
				try {
					App.getMistakes().getItems().clear();
					App.getMistakes().getItems().add("");
					hint.delete(0, hint.length());
					questions = QFile.loadFile("quizes/" + App.getSelector().getValue() + ".txt");
					score = 0;
					totalScore = questions.size();
					App.updateScore(score, totalScore);
					q = (int) (Math.random() * questions.size());
					App.setText(questions.get(q).getQuestion(App.isEnglish()));
				} catch (IndexOutOfBoundsException error) {
					JOptionPane.showMessageDialog(null, App.getSelector().getValue() + ".txt " + "is empty");
				}
				
			}
			
			if(e.getSource().equals(App.getHelpBtn())) {
				
				try {
					App.sendHeld(questions.get(q).getQuestion(!App.isEnglish()));
					score -= 1;
					App.updateScore(score, totalScore);
				} catch (Exception ex) {
					App.sendHeld("I can't help you");
				}
				
			}
			
			if(e.getSource().equals(App.getHintBtn())) {
				
				try {
					hint.append(questions.get(q).getQuestion(!App.isEnglish()).charAt(hint.length()));
					App.sendHint(hint.toString());
				} catch (IndexOutOfBoundsException | NullPointerException e1) {
//					Deliberately Nothing
				}
				
			}
			
		}
		
	}
	
//	KeyHandler
	private class KeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent e) {
			
			if(e.getCode().equals(KeyCode.ENTER)) {		
				nextQuestion();
			}
			
		}
	
	}
	
//	Mouse Handler
	private class MouseHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent arg0) {
			
//			TODO display info
			
		}
	
	}
	
	private void nextQuestion() {
		
		if(questions == null) {
			JOptionPane.showMessageDialog(null, "Please load a quiz");
			return;
		}
			
		try {
			
			String userInput = App.getInput().getText();
			
			if(userInput.equalsIgnoreCase(questions.get(q).getQuestion(!App.isEnglish()))) {
//				CORRECT
				
				hint.delete(0, hint.length());
				score++;
				App.updateScore(score, totalScore);
				questions.remove(q);
				
				q = (int) (Math.random() * questions.size());
				App.setText(questions.get(q).getQuestion(App.isEnglish()));
			
			} else {
//				WRONG
				
				if(App.getMistakes().getItems().get(0).equals("")) {
					App.getMistakes().getItems().clear();
				}
				App.addMistake(userInput);
				
			}
			
			App.getInput().clear();
		
		} catch(IndexOutOfBoundsException e) {
			
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
	
	public MouseHandler getMouseHandler() {
		return mouseHandler;
	}
	
}
