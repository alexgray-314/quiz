package com.jaguarplugins.quiz.questionss;

public class Mistake {

	private String mistake, answer;

	public Mistake(String mistake, String answer) {
		this.mistake = mistake;
		this.answer = answer;
	}

	public String getMistake() {
		return mistake;
	}

	public void setMistake(String mistake) {
		this.mistake = mistake;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
//	TODO add a way of using this for the ListView
	
}
