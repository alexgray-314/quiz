package com.jaguarplugins.quiz.input;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.jaguarplugins.quiz.App;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
			
			if(e.getCode().equals(KeyCode.ENTER)) {
				
				App.formatEditor();
				
			}
			
		}
	
	}
	
	private class ButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			
			if(e.getSource().equals(App.getSaveBtn())) {
				
				File file = new File("quizzes/" + App.getEditorTitle().getText());
				
				if(!file.exists()) {

					saveAndClose(file);

				} else {
					Alert a = new Alert(AlertType.CONFIRMATION, "Are you sure you want to overide " + App.getEditorTitle().getText());
					Optional<ButtonType> result = a.showAndWait();
					
					if(result.get().equals(ButtonType.OK)) {
						
						saveAndClose(file);
						
					}
					
				}
				
			}
			
			if(e.getSource().equals(App.getCancelBtn())) {
				
				App.getArea().clear();
				App.getEditorStage().close();
				
				Alert a = new Alert(AlertType.WARNING, "File edit/creation cancelled\nChanges lost");
				a.setTitle("File Editor");
				a.showAndWait();
				
			}
			
			if(e.getSource().equals(App.getFormatBtn())) {
				
				App.formatEditor();
				
			}
			
		}
	
	}
	
	private void saveAndClose(File file) {
		
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
			
			Alert a = new Alert(AlertType.INFORMATION, App.getEditorTitle().getText() + " saved");
			a.setTitle("File Editor");
			a.showAndWait();
			
		} catch (IOException e1) {
			Alert a = new Alert(AlertType.ERROR, "I/O Exception\nFile could not be created");
			a.setTitle("File Editor");
			a.showAndWait();
		}
		
	}
	
	public ButtonHandler getButtonHandler() {
		return buttonHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

}
