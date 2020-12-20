package com.jaguarplugins.quiz.input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import com.jaguarplugins.quiz.questions.Question;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class QFile {

	public static ArrayList<String> getFiles() {

		String path = "quizzes";

		File file = new File(path);
		
		File dataFile = new File("data");
		if (!dataFile.exists()) { 
			dataFile.mkdir();
		}
		
		if(!file.exists()) {
			
			file.mkdir();
			File exampleTxt = new File(path + "/example.txt");
			
			File readme = new File(path + "/README.txt");
			
			try {
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(exampleTxt), StandardCharsets.UTF_8);
				out.append("fre1,fre2:eng1,eng2,eng3\n");
				out.append("marrant,content:happy,joyful,pleased\n");
				out.append("hola:hello\n");
				out.append("θεος:god");
				out.flush();
				out.close();
				
				out = new OutputStreamWriter(new FileOutputStream(readme), StandardCharsets.UTF_8);
				out.append("To create a new quiz file, click 'New File' in the program.\n\n"
						+ "Each line represents a different question.\n"
						+ "Type all the possible ways of saying the word/phrase in the foreign language, separated by a ','.\n"
						+ "Then type a ':' to show that you are now switching to the other language.\n"
						+ "Finally type all the possible ways of saying the word/phrase in English, separated by a ','.\n"
						+ "Repeat on a new line for each question.\n\n"
						+ "DONOT surround ':' or ',' with spaces or leave any blank lines.\n"
						+ "Press 'Format' to highlight different languages in different colours\n\n"
						+ "Please see example.txt for examples.\n\n"
						+ "To run any quiz files, open the quiz.jar file and select the file you want from the drop down menu.");
				out.flush();
				out.close();
			} catch (IOException e) {
				Alert b = new Alert(AlertType.WARNING, "Example.txt could not be created");
				b.showAndWait();
			}
			
			Alert a = new Alert(AlertType.INFORMATION, "Quizzes file created.\nPlease reload the program");
			a.showAndWait();
			System.exit(0);
		}

		ArrayList<String> output = new ArrayList<String>();
		for (String s : file.list()) {
			String newQuiz = s.split("\\.")[0];
			if(!newQuiz.equalsIgnoreCase("README")) {
				output.add(newQuiz);
			}
		}

		return output;

	}

//	TODO make backwards compatible as well
	public static ArrayList<Question> loadFile(String path) {

		ArrayList<Question> contents = new ArrayList<Question>();

		try {

			File file = new File(path);
			BufferedReader reader = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(file), "UTF8"));

			String line = reader.readLine();
			while (line != null) {
				String[] data = line.split(":");
				contents.add(new Question(data[0], data[1]));
				line = reader.readLine();
			}

			reader.close();

		} catch (FileNotFoundException e) {
			Alert a = new Alert(AlertType.ERROR, "File could not be found");
			a.showAndWait();
			return null;
		} catch (UnsupportedEncodingException e) {
			Alert a = new Alert(AlertType.ERROR, "Please change file encoding type to UTF-8 and try again");
			a.showAndWait();
			return null;
		} catch (IOException e) {
			Alert a = new Alert(AlertType.ERROR, "I/O exception");
			a.showAndWait();
			return null;
		}

		if (contents.size() > 0) {
			return contents;
		}
		
		return null;
	}
	
	public static File getEditFile() {
		
		File startDir = new File("quizzes");
		
		JFileChooser fc = new JFileChooser(startDir);
		int result = fc.showOpenDialog(null);
		File file = null;
		
		if(result == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
		}
		
		return file;
	
	}
	
	public static void saveMistake(Question q) {
		
		String line;
		int counter;
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("data/mistakes.txt", true)));
				
			line = "";
			counter = q.getQuestion(true).length;
			for (String qs : q.getQuestion(true)) {
				line += qs;
				counter--;
				if (counter > 0) {
					line += ",";
				}
			}
			line += ":";
			counter = q.getQuestion(false).length;
			for (String as : q.getQuestion(false)) {
				line += as;
				counter--;
				if (counter > 0) {
					line += ",";
				}
			}

			out.println(line);
				
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			Alert a = new Alert(AlertType.ERROR, "I/O Exception\nMistake could not be saved");
			a.setTitle("File Error #2");
			a.showAndWait();
		}
		
	}
	
}
