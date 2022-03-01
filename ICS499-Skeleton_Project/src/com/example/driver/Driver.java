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
		ArrayList<String> wordList = new ArrayList<String>();
		ArrayList<Letter> logicalChars = new ArrayList<Letter>();
		wordReader reader = new wordReader("E:\\Folders\\Classes\\ICS499\\Files\\english_word_list.csv");
		reader.readFileIntoArray(wordList);

		SkeletonBoard board = new SkeletonBoard();
		board.getWords(wordList);

//		Collections.sort(board.getWordList());
		board.findBoardSize();
		board.fillBoardWithSpaces();

		Powerpoint ppt = new Powerpoint();
		int slideCount = 0;

		for (int i = 0; i < board.getWordList().size(); i++) {
			APIConnector connector = new APIConnector(board.getWordList().get(i));
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

					Word word = new Word(board.getWordList().get(i), logicalChars, -1, -1, "none");
					for (int k = 0; k < jsonArray.length(); k++) {
						Letter letter = new Letter(-1, -1, jsonArray.getString(k), word, false);
						logicalChars.add(letter);
					}

					board.insertToBoard(logicalChars, word);

					for (int j = 0; j < board.getBoard().length; j++) {
						for (int l = 0; l < board.getBoard().length; l++) {
							System.out.print("[" + board.getBoard()[j][l].getLetter() + "]");
						}
						System.out.println();
					}
					System.out.println();

				}
				connector.disconnect(connector.getConnection());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		HSLFSlide slide = ppt.createSlide(++slideCount);
		ppt.createTable(slide, board.getBoard().length, board.getBoard().length, board.getBoard());
		ppt.createAvailableLetters(slide, board);
		ppt.writeToFile(ppt.getPpt(), "english_skeleton_puzzle");
		/**
		 * sheesh elope enter
		 */

	}

}
