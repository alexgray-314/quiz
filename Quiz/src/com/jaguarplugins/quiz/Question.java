package com.jaguarplugins.quiz;

public class Question {

	private String question;
	private String answer;
	
	public Question(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}

	public String getQuestion(boolean english) {
		
		if(english) {
			return question;
		} else {
			return answer;
		}
	
	}
	
}
