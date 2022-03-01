package com.example.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class SkeletonBoard {
	private ArrayList<String> wordList;
	private Letter[][] board;
	private ArrayList<Letter> lettersOnBoard;
	private ArrayList<Word> wordsOnBoard;

	public SkeletonBoard() {
		wordList = new ArrayList<String>();
		wordsOnBoard = new ArrayList<Word>();
		lettersOnBoard = new ArrayList<Letter>();
	}

	public ArrayList<String> getWords(ArrayList<String> words) {
		for (int i = 0; i < 3; i++) {
			if (words.get(i) != null) {
				if (wordList.contains(words.get(i)) == true) {
					wordList.add(words.get(i + 1));
				} else {
					wordList.add(words.get(i));
				}

			}
		}
		for (int k = 0; k < wordList.size(); k++) {
			words.removeAll(Collections.singleton(wordList.get(k)));
		}
		return wordList;
	}



	public void findBoardSize() {
		int size = this.wordList.get(0).length();
		board = new Letter[10][10];

	}

	public void fillBoardWithSpaces() {
		for (int i = 0; i < board.length; i++) {
			for (int col = 0; col < board.length; col++) {
				Letter underScore = new Letter(i, col, " ", new Word(),false);
				board[i][col] = underScore;
			}
		}
	}

	public void insertToBoard(ArrayList<Letter> logicalChars, Word word) {
		if (wordsOnBoard.isEmpty()) {
			for (int i = 0; i < logicalChars.size(); i++) {
				logicalChars.get(i).setRow(board.length/2 - 1);
				logicalChars.get(i).setColumn((board.length/2) - 3 + i);
				word.setDirection("Horizontal");
				board[board.length/2 - 1][(board.length/2) - 3 + i] = logicalChars.get(i);
				lettersOnBoard.add(logicalChars.get(i));
				wordsOnBoard.add(word);
			}
		}else {
			if(!(findMatch(logicalChars,word) == null)) {
				/**
				 * match is the Letter object that is returned when you try to find a letter that matches a letter in the word object
				 * matchedIndex is the index that the word matches.
				 * direction is the direction that the word will be going.
				 */
				Letter match = findMatch(logicalChars, word);
				System.out.println(match.getLetter());
				System.out.println(match.getRow());
				System.out.println(match.getColumn());
				int matchedIndex = findIndexMatching(match, logicalChars);
				String direction = findDirection(match);
				if(direction.equalsIgnoreCase("Vertical")) {
					for(int k = 0; k < logicalChars.size(); k++) {
						if(word.getRow() == -1 && word.getCol() == -1) {
							word.setRow(match.getRow());
							word.setCol(match.getColumn());
						}
						word.setDirection("Vertical");
						if(matchedIndex >= 0) {
							board[match.getRow() - matchedIndex + k][match.getColumn()] = logicalChars.get(k);
							logicalChars.get(k).setRow(match.getRow() - matchedIndex + k);
							logicalChars.get(k).setColumn(match.getColumn());
							lettersOnBoard.add(logicalChars.get(k));
						}else {
							board[match.getRow() + k][match.getColumn()] = logicalChars.get(k);
							logicalChars.get(k).setRow(match.getRow() + k);
							logicalChars.get(k).setColumn(match.getColumn());
							lettersOnBoard.add(logicalChars.get(k));
						}
						wordsOnBoard.add(word);
						
					}
				}else if(direction.equalsIgnoreCase("Horizontal")) {
					for(int k = 0; k < logicalChars.size(); k++) {
						if(word.getRow() == -1 && word.getCol() == -1) {
							word.setRow(match.getRow());
							word.setCol(match.getColumn());
						}
						word.setDirection("Horizontal");
						logicalChars.get(k).setRow(match.getRow());
						logicalChars.get(k).setColumn(match.getColumn() + k);
						if(matchedIndex >= 0) {
							board[match.getRow()][match.getColumn() - matchedIndex + k] = logicalChars.get(k);
							logicalChars.get(k).setRow(match.getRow());
							logicalChars.get(k).setColumn(match.getColumn() - matchedIndex + k);
							lettersOnBoard.add(logicalChars.get(k));
						}else {
							board[match.getRow()][match.getColumn() - matchedIndex + k] = logicalChars.get(k);
							lettersOnBoard.add(logicalChars.get(k));
						}
						wordsOnBoard.add(word);
						
					}
				}
				
				if(direction.equalsIgnoreCase("Vertical")) {
					if(board[word.getRow() + 1][word.getCol()] != null || board[word.getRow() - 1][word.getCol()] != null) {
						match.setOccupied(true);
					}
				}else if(direction.equalsIgnoreCase("Horizontal")){
					if(board[word.getRow()][word.getCol() + 1] != null || board[word.getRow()][word.getCol() - 1] != null) {
						match.setOccupied(true);
					}
				}
				

			}
			
		}

	}
	
	/**
	 * This method verifies that the word will fit on the board.
	 * @param logicalChars
	 * @param word
	 * @param matched
	 * @param direction
	 * @param matchedIndex
	 * @return
	 */
	public boolean isValid(ArrayList<Letter> logicalChars, Word word, Letter matched, String direction, int matchedIndex) {
		if(direction.equalsIgnoreCase("Horizontal")) {
			if(matchedIndex > 0) {
				
			}
		}
		return false;
	}
	
	
	public int findIndexMatching(Letter matching, ArrayList<Letter> logicalChars) {
		for(int i = 0; i < logicalChars.size(); i++) {
			if(logicalChars.get(i).getLetter().equalsIgnoreCase(matching.getLetter())) {
				return i;
			}
		}
		return -1;
	}

	public String findDirection(Letter matching) {
		if(matching.getWord().getDirection().equalsIgnoreCase("Horizontal")) {
			return "Vertical";
		}else if(matching.getWord().getDirection().equalsIgnoreCase("Vertical")){
			return "Horizontal";
		}
		return null;


	}

	public Letter findMatch(ArrayList<Letter> charList, Word word) {
		for(int i = 0; i < charList.size(); i++) {
			for(int row = 0; row < board.length; row++) {
				for(int col = 0; col < board.length; col++) {
					if(charList.get(i).getLetter().equalsIgnoreCase(board[row][col].getLetter())) {
						if(board[row][col].isOccupied() == false) {
							return board[row][col];
						}
					}
				}
			}
		}
		return null;

	}
	




	public void removeWordFromList(String word) {
		wordList.remove(word);
	}

	public ArrayList<String> getWordList() {
		return wordList;
	}

	public void setWordList(ArrayList<String> board) {
		this.wordList = board;
	}

	public Letter[][] getBoard() {
		return board;
	}

	public ArrayList<Letter> getLettersOnBoard() {
		return lettersOnBoard;
	}

	public void setLettersOnBoard(ArrayList<Letter> lettersOnBoard) {
		this.lettersOnBoard = lettersOnBoard;
	}

	public ArrayList<Word> getWordsOnBoard() {
		return wordsOnBoard;
	}

	public void setWordsOnBoard(ArrayList<Word> wordsOnBoard) {
		this.wordsOnBoard = wordsOnBoard;
	}

	public void setBoard(Letter[][] board) {
		this.board = board;
	}
	
	

}
