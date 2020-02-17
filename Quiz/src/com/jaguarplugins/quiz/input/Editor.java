package com.jaguarplugins.quiz.input;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.jaguarplugins.quiz.App;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Editor {

	private ButtonHandler buttonHandler;
	private KeyHandler keyHandler;

	public Editor() {

		buttonHandler = new ButtonHandler();
		keyHandler = new KeyHandler();

	}
	
	private class KeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent e) {
			
			if(e.getCode().equals(KeyCode.COMMA)) {
							

			}
			
		}
	
	}
	
	private class ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			
			if(e.getSource().equals(App.getSaveBtn())) {
				
				File file = new File("quizzes/" + Handler.getTd().getResult() + ".txt");
				
				if(!file.exists()) {

					try {
						
						OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
						out.append(App.getArea().getText());
						out.flush();
						out.close();
						
						App.getArea().clear();
						App.getEditorStage().close();
						
						file.createNewFile();
						
						App.getSelector().getItems().clear();
						App.getSelector().getItems().addAll(QFile.getFiles());
						
						Alert a = new Alert(AlertType.INFORMATION, Handler.getTd().getResult() + ".txt created");
						a.setTitle("New File");
						a.showAndWait();
						
					} catch (IOException e1) {
						Alert a = new Alert(AlertType.ERROR, "I/O Exception\nFile could not be created");
						a.setTitle("New File");
						a.showAndWait();
					}

				} else {
					Alert a = new Alert(AlertType.ERROR, "File already exists, please pick a new name or rename the existing file");
					a.setTitle("New File");
					a.showAndWait();
				}
				
			}
			
			if(e.getSource().equals(App.getCancelBtn())) {
				
				App.getArea().clear();
				App.getEditorStage().close();
				
				Alert a = new Alert(AlertType.WARNING, "File creation cancelled");
				a.setTitle("New File");
				a.showAndWait();
				
			}
			
		}
	
	}

	public ButtonHandler getButtonHandler() {
		return buttonHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

}
