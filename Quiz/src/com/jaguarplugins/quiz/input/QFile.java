package com.jaguarplugins.quiz.input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jaguarplugins.quiz.questions.Question;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class QFile {

	public static ArrayList<String> getFiles() {

		String path = "quizzes";

		File file = new File(path);
		
		if(!file.exists()) {
			
			file.mkdir();
			File exampleTxt = new File(path + "/example.txt");
			
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(exampleTxt)), true);
				out.println("fre1,fre2:eng1,eng2,eng3");
				out.println("marrant,content:happy,joyful,pleased");
				out.print("hola:hello");
				out.flush();
				out.close();
			} catch (IOException e) {
				Alert b = new Alert(AlertType.WARNING, "Example.txt could not be created");
				b.showAndWait();
			}
			
			Alert a = new Alert(AlertType.INFORMATION, "Quizzes file created.\nPlease put a quiz file into the folder and reload the program");
			a.showAndWait();
			System.exit(0);
		}

		ArrayList<String> output = new ArrayList<String>();
		for (String s : file.list()) {
			output.add(s.split("\\.")[0]);
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
			JOptionPane.showMessageDialog(null, "File could not be found");
			return null;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "I/O Exception");
			return null;
		}

		return contents;
	}

}
