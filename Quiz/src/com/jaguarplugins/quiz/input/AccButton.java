package com.jaguarplugins.quiz.input;

import com.jaguarplugins.quiz.App;

import javafx.scene.control.Button;

public class AccButton {

	private Button button;
	
	public AccButton(String acc) {
		
		button = new Button();
		button.setText(acc);
		button.setId("acc");
		button.setOnAction(e -> {
			App.getInput().appendText("");
			int pos = App.getInput().getCaretPosition();
			System.out.println(pos);
			App.getInput().insertText(pos, acc);
			App.getInput().requestFocus();
			App.getInput().deselect();
			App.getInput().positionCaret(pos + 1);
		});
		
	}
	
	public Button getButton() {
		return button;
	}
	
}