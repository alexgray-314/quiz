package com.jaguarplugins.quiz.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jaguarplugins.quiz.questions.Question;

public class QFile {

	public static ArrayList<String> getFiles() {
		
		String path = "quizes";
		
		File file = new File(path);
		
		ArrayList<String> output = new ArrayList<String>();
		for(String s : file.list()) {
			output.add(s.split("\\.")[0]);
		}
		
		return output;
		
	}
	
	public static ArrayList<Question> loadFile(String path) {
		
//		NOTE: max number of question options is 3
		
		ArrayList<Question> contents = new ArrayList<Question> ();
//		TODO maybe fix
		String[][] data = new String[1][2];
		
		try {
		
			File file = new File(path);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line = reader.readLine();
			String[] splitLine = new String[1];
			
			while(line != null) {
				
				splitLine = line.split(":");
				data[0] = splitLine[0].split(",");
				data[1] = splitLine[1].split(",");
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
