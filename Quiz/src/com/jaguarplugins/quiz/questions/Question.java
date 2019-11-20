package com.jaguarplugins.quiz.questions;

public class Question {

	private String[] questions;
	private String[] answers;
	
	public Question(String[] questions, String[] answers) {
		this.questions = questions;
		this.answers = answers;
	}

	public String[] getQuestion(boolean english) {
		
		if(english) {
			return questions;
		} else {
			return answers;
		}
	
	}
	
}
