package com.example.driver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.APIConnector.APIConnector;
import com.example.powerpoint.Powerpoint;
import com.example.skeleton.Letter;
import com.example.skeleton.SkeletonBoard;
import com.example.skeleton.Word;
import com.example.wordReader.wordReader;

public class Driver {

	public static void main(String[] args) {

		/**
		 * These variables represent the settings for the board. wordMax set the
		 * maximum number of words on the board and language sets the language of the
		 * words between english and telugu.
		 */
		int wordMax = 3;
		String language = "english";

		/**
		 * wordList will contain the words that the reader object has read. logicalChars
		 * holds the Letter objects of each word
		 */
		ArrayList<String> wordList = new ArrayList<String>();
		ArrayList<Letter> logicalChars = new ArrayList<Letter>();

		/**
		 * wordReader object reads the words contained in the word_list and adds it to
		 * the arrayList wordList
		 */
		wordReader reader = new wordReader("E:\\Folders\\Classes\\ICS499\\Files\\" + language + "_word_list.csv");
		reader.readFileIntoArray(wordList);

		/**
		 * board represents the skeleton board that will be played on.
		 */
		SkeletonBoard board = new SkeletonBoard();

//		Collections.sort(board.getWordList());
		board.findBoardSize();
		board.fillBoardWithSpaces();

		Powerpoint ppt = new Powerpoint();
		int slideCount = 0;
		for (int i = 0; i < wordList.size(); i++) {
			APIConnector connector = new APIConnector(wordList.get(i));
			connector.getLogicalChars();
			try {
				int responseCode = connector.getConnection().getResponseCode();
				if (responseCode != 200) {
					throw new RuntimeException("HttpResponseCode: " + responseCode);
				} else {
					InputStream inputStream = connector.getConnection().getInputStream();
					Scanner scanner = new Scanner(inputStream);
					String line = new String();
					while (scanner.hasNextLine()) {
						line = scanner.nextLine();
						line = line.substring(line.indexOf("{"));
					}

					JSONObject jsonObj = new JSONObject(line);
					JSONArray jsonArray = jsonObj.getJSONArray("data");

					logicalChars = new ArrayList<Letter>();

					Word word = new Word(wordList.get(i), logicalChars, -1, -1, "none");
					for (int k = 0; k < jsonArray.length(); k++) {
						Letter letter = new Letter(-1, -1, jsonArray.getString(k), word, false);
						logicalChars.add(letter);
					}

					board.insertToBoard(logicalChars, word);
					if(board.getNumberOfWords() == wordMax) {
						HSLFSlide slide = ppt.createSlide(++slideCount);
						ppt.createTable(slide, board.getBoard().length, board.getBoard().length, board.getBoard());
						ppt.createAvailableLetters(slide, board);
						ppt.writeToFile(ppt.getPpt(), language + "_skeleton_puzzle");
						ppt.getPpt().close();
						printBoard(board);
						board = new SkeletonBoard();
						board.findBoardSize();
						board.fillBoardWithSpaces();
					}
					
					

				}
				connector.disconnect(connector.getConnection());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

//		HSLFSlide slide = ppt.createSlide(++slideCount);
//		ppt.createTable(slide, board.getBoard().length, board.getBoard().length, board.getBoard());
//		ppt.createAvailableLetters(slide, board);
//		ppt.writeToFile(ppt.getPpt(), language + "_skeleton_puzzle");
		/**
		 * sheesh elope enter
		 */

	}
	
	
	/**
	 * Print the board for testing.
	 */
	public static void printBoard(SkeletonBoard board) {
		for (int j = 0; j < board.getBoard().length; j++) {
			for (int l = 0; l < board.getBoard().length; l++) {
				System.out.print("[" + board.getBoard()[j][l].getLetter() + "]");
			}
			System.out.println();
		}
		System.out.println();
	}

}
