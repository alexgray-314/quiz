package com.jaguarplugins.quiz.questions;

public class Mistake {

	public static Mistake BLANK = new Mistake(" ", " ", " ");

	private String mistake, answer, question;

	public Mistake(String mistake, String answer, String question) {
		this.mistake = mistake;
		this.answer = answer;
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public String getMistake() {
		return this.mistake;
	}

	@Override
	public String toString() {
//		This not not need to be implemented because it's already in java.lang.object
		return this.question;
	}

}
