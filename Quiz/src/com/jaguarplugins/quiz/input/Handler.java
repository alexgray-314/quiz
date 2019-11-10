package com.jaguarplugins.quiz.input;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jaguarplugins.quiz.App;
import com.jaguarplugins.quiz.Question;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Handler {
	
	private ArrayList<Question> questions;
	private int q;
	private int score;
	
	private ButtonHandler buttonHandler;
	private KeyHandler keyHandler;
	
	public Handler() {
		
		buttonHandler = new ButtonHandler();
		keyHandler = new KeyHandler();
		
	}
	
//	Button Handler
	private class ButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent e) {
			
			if(e.getSource().equals(App.getOkBtn())) {
				
				nextQuestion();
			
			}
			
			if(e.getSource().equals(App.getLoadBtn())) {
				
				if(App.getSelector().getValue() == null) {
					return;
				}
				
				try {
					score = 0;
					App.updateScore(score);
					questions = QFile.loadFile("quizes/" + App.getSelector().getValue() + ".txt");
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
					App.updateScore(score);
				} catch (Exception ex) {
					App.sendHeld("I can't help you");
				}
				
			}
			
		}
		
	}
	
//	KeyHandler
	private class KeyHandler implements EventHandler<KeyEvent> {

		public void handle(KeyEvent e) {
			
			if(e.getCode().equals(KeyCode.ENTER)) {		
				nextQuestion();
			}
			
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
				
				System.out.println("Correct");
				score++;
				App.updateScore(score);
				questions.remove(q);
				
				q = (int) (Math.random() * questions.size());
				App.setText(questions.get(q).getQuestion(App.isEnglish()));
			
			} else {
				
				System.out.println("Wrong");
				
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
	
}
