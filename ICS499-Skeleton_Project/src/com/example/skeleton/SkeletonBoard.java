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
	private int numberOfWords;

	public SkeletonBoard() {
		this.wordList = new ArrayList<String>();
		this.wordsOnBoard = new ArrayList<Word>();
		this.lettersOnBoard = new ArrayList<Letter>();
		this.numberOfWords = 0;
	}

	public ArrayList<String> getWords(String word, ArrayList<String> mainWordList) {
		if (wordList.contains(word)) {
			return wordList;
		} else {
			wordList.add(word);
		}

		for (int k = 0; k < wordList.size(); k++) {
			mainWordList.removeAll(Collections.singleton(wordList.get(k)));
		}
		return wordList;
	}

	public void findBoardSize() {
		board = new Letter[10][10];

	}

	public void fillBoardWithSpaces() {
		for (int i = 0; i < board.length; i++) {
			for (int col = 0; col < board.length; col++) {
				Letter underScore = new Letter(i, col, " ", new Word(), false);
				board[i][col] = underScore;
			}
		}
	}

	public void insertToBoard(ArrayList<Letter> logicalChars, Word word) {
		if (wordsOnBoard.isEmpty()) {
			for (int i = 0; i < logicalChars.size(); i++) {
				logicalChars.get(i).setRow(board.length / 2);
				logicalChars.get(i).setColumn((board.length / 2) - 3 + i);
				word.setDirection("Horizontal");
				board[board.length / 2][(board.length / 2) - 3 + i] = logicalChars.get(i);
				lettersOnBoard.add(logicalChars.get(i));
				wordsOnBoard.add(word);
			}
			numberOfWords++;
		} else {
			if (!(findMatch(logicalChars, word) == null)) {
				/**
				 * match is the Letter object that is returned when you try to find a letter
				 * that matches a letter in the word object matchedIndex is the index that the
				 * word matches. direction is the direction that the word will be going.
				 */
				Letter match = findMatch(logicalChars, word);
				int matchedIndex = findIndexMatching(match, logicalChars);
				String direction = findDirection(match);
				System.out.println(direction);
				System.out.println(match.getRow() + "," + match.getColumn());

				if (isValid(logicalChars, word, match, direction, matchedIndex)) {
					System.out.println(word.getWord() + ":true");

					if (direction.equalsIgnoreCase("Vertical")) {
						for (int k = 0; k < logicalChars.size(); k++) {
							if (word.getRow() == -1 && word.getCol() == -1) {
								word.setRow(match.getRow());
								word.setCol(match.getColumn());
							}
							word.setDirection("Vertical");
							if (matchedIndex >= 0) {
								board[match.getRow() - matchedIndex + k][match.getColumn()] = logicalChars.get(k);
								logicalChars.get(k).setRow(match.getRow() - matchedIndex + k);
								logicalChars.get(k).setColumn(match.getColumn());
								lettersOnBoard.add(logicalChars.get(k));
							}
						}
						wordsOnBoard.add(word);
						numberOfWords++;
					} else if (direction.equalsIgnoreCase("Horizontal")) {
						for (int k = 0; k < logicalChars.size(); k++) {
							if (word.getRow() == -1 && word.getCol() == -1) {
								word.setRow(match.getRow());
								word.setCol(match.getColumn());
							}
							word.setDirection("Horizontal");
							logicalChars.get(k).setRow(match.getRow());
							logicalChars.get(k).setColumn(match.getColumn() + k);
							if (matchedIndex >= 0) {
								board[match.getRow()][match.getColumn() - matchedIndex + k] = logicalChars.get(k);
								logicalChars.get(k).setRow(match.getRow());
								logicalChars.get(k).setColumn(match.getColumn() - matchedIndex + k);
								lettersOnBoard.add(logicalChars.get(k));
							}

						}
						wordsOnBoard.add(word);
						numberOfWords++;
					}

//					if (direction.equalsIgnoreCase("Vertical")) {
//						if (board[word.getRow() + 1][word.getCol()] != null
//								|| board[word.getRow() - 1][word.getCol()] != null) {
//							match.setOccupied(true);
//						}
//					} else if (direction.equalsIgnoreCase("Horizontal")) {
//						if (board[word.getRow()][word.getCol() + 1] != null
//								|| board[word.getRow()][word.getCol() - 1] != null) {
//							match.setOccupied(true);
//						}
//					}

				} else {
					System.out.println(word.getWord() + ":false");
				}

			}

		}

	}

	/**
	 * This method verifies that the word will fit on the board.
	 * 
	 * @param logicalChars
	 * @param word
	 * @param matched
	 * @param direction
	 * @param matchedIndex
	 * @return
	 */
	public boolean isValid(ArrayList<Letter> logicalChars, Word word, Letter matched, String direction,
			int matchedIndex) {
		if (direction.equalsIgnoreCase("Vertical")) {
			for (int i = 0; i < logicalChars.size(); i++) {
				if (matchedIndex == 0) {
					if (matched.getRow() + logicalChars.size() <= board.length) {
						if (i == 0) {
							continue;
						} else {
							if ((matched.getColumn() - 1 >= 0) && (matched.getColumn() + 1 <= board.length - 1)) {
								if (!(board[matched.getRow() - matchedIndex + i][matched.getColumn() - 1].getLetter()
										.equals(" ")
										|| !board[matched.getRow() - matchedIndex + i][matched.getColumn() + 1]
												.getLetter().equals(" "))) {
									System.out.println(1);
									return false;
								} else if ((board[matched.getRow() - matchedIndex + i][matched.getColumn() - 1]
										.getLetter().equals(" ")
										&& board[matched.getRow() - matchedIndex + i][matched.getColumn() + 1]
												.getLetter().equals(" "))) {
									System.out.println(1);
									return true;
								}
							} else if (matched.getColumn() - 1 < 0) {
								if (board[matched.getRow() - matchedIndex + i][matched.getColumn() + 1].getLetter()
										.equals(" ")) {
									System.out.println(2);
									return true;
								}
							} else if (matched.getColumn() + 1 > board.length - 1) {
								if ((board[matched.getRow() - matchedIndex + i][matched.getColumn() - 1].getLetter()
										.equals(" "))) {
									System.out.println(3);
									return true;
								}
							}
						}
					}

				} else if (matchedIndex > 0) {
					if (matched.getRow() - matchedIndex >= 0
							&& matched.getRow() - matchedIndex + logicalChars.size() <= board.length) {
						if (matched.getRow() - matchedIndex + i == matched.getRow() - matchedIndex + matchedIndex) {
							continue;
						} else {
							if ((matched.getColumn() - 1 >= 0) && (matched.getColumn() + 1 <= board.length - 1)) {
								if (!(board[matched.getRow() - matchedIndex + i][matched.getColumn() - 1].getLetter()
										.equals(" ")
										|| !board[matched.getRow() - matchedIndex + i][matched.getColumn() + 1]
												.getLetter().equals(" "))) {
									System.out.println(4);
									return false;
								} else if ((board[matched.getRow() - matchedIndex + i][matched.getColumn() - 1]
										.getLetter().equals(" ")
										&& board[matched.getRow() - matchedIndex + i][matched.getColumn() + 1]
												.getLetter().equals(" "))) {
									System.out.println(4);
									return true;
								}
							} else if (matched.getColumn() - 1 < 0) {
								if (board[matched.getRow() - matchedIndex + i][matched.getColumn() + 1].getLetter()
										.equals(" ")) {
									System.out.println(5);
									return true;
								}
							} else if (matched.getColumn() + 1 > board.length - 1) {
								if ((board[matched.getRow() - matchedIndex + i][matched.getColumn() - 1].getLetter()
										.equals(" "))) {
									System.out.println(6);
									return true;
								}
							}
						}

					}
				}
			}
		}

		else if (direction.equalsIgnoreCase("Horizontal")) {
			for (int i = 0; i < logicalChars.size(); i++) {
				if (matchedIndex == 0) {
					if (matched.getColumn() + logicalChars.size() <= board.length) {
						if (i == 0) {
							continue;
						} else {
							if ((matched.getRow() - 1 >= 0) && (matched.getRow() + 1 <= board.length - 1)) {
								if (!(board[matched.getRow() - 1][matched.getColumn() - matchedIndex + i].getLetter()
										.equals(" ")
										|| !board[matched.getRow() + 1][matched.getColumn() - matchedIndex + i]
												.getLetter().equals(" "))) {
									System.out.println(7);
									return false;
								} else if ((board[matched.getRow() - 1][matched.getColumn() - matchedIndex + i]
										.getLetter().equals(" ")
										&& board[matched.getRow() + 1][matched.getColumn() - matchedIndex + i]
												.getLetter().equals(" "))) {
									System.out.println(7);
									return true;
								}
							} else if (matched.getRow() - 1 < 0) {
								if (board[matched.getRow() + 1][matched.getColumn() - matchedIndex + i].getLetter()
										.equals(" ")) {
									System.out.println(8);
									return true;
								} else if (matched.getRow() + 1 > board.length - 1) {
									if ((board[matched.getRow() - 1][matched.getColumn() - matchedIndex + i].getLetter()
											.equals(" "))) {
										System.out.println(9);
										return true;
									}
								}
							}
						}
					}

				} else if (matchedIndex > 0) {
					if (matched.getRow() - matchedIndex >= 0
							&& matched.getRow() - matchedIndex + logicalChars.size() <= board.length) {
						if (matched.getRow() - matchedIndex + i == matched.getRow() - matchedIndex + matchedIndex) {
							continue;
						} else {
							if ((matched.getRow() - 1 >= 0) && (matched.getRow() + 1 <= board.length - 1)) {
								if (board[matched.getRow() - 1][matched.getColumn() - matchedIndex + i].getLetter()
										.equals(" ")
										&& board[matched.getRow() + 1][matched.getColumn() - matchedIndex + i]
												.getLetter().equals(" ")) {
									System.out.println(10);
									return true;
								}
							} else if (matched.getRow() - 1 < 0) {
								if (matched.getRow() - 1 < 0) {
									if (board[matched.getRow() + 1][matched.getColumn() - matchedIndex + i].getLetter()
											.equals(" ")) {
										System.out.println(11);
										return true;
									}
								} else if (matched.getRow() + 1 > board.length - 1) {
									if ((board[matched.getRow() - 1][matched.getColumn() - matchedIndex + i].getLetter()
											.equals(" "))) {
										System.out.println(12);
										return true;
									}
								}
							}
						}

					}
				}
			}
		}
		return false;

	}

	public int findIndexMatching(Letter matching, ArrayList<Letter> logicalChars) {
		for (int i = 0; i < logicalChars.size(); i++) {
			if (logicalChars.get(i).getLetter().equalsIgnoreCase(matching.getLetter())) {
				return i;
			}
		}
		return -1;
	}

	public String findDirection(Letter matching) {
		if (matching.getWord().getDirection().equalsIgnoreCase("Horizontal")) {
			return "Vertical";
		} else if (matching.getWord().getDirection().equalsIgnoreCase("Vertical")) {
			return "Horizontal";
		}
		return null;

	}

	public Letter findMatch(ArrayList<Letter> charList, Word word) {
		for (int i = 0; i < charList.size(); i++) {
			for (int row = 0; row < board.length; row++) {
				for (int col = 0; col < board.length; col++) {
					if (charList.get(i).getLetter().equalsIgnoreCase(board[row][col].getLetter())) {
						if (board[row][col].isOccupied() == false) {
							return board[row][col];
						}
					}
				}
			}
		}
		return null;

	}

	public Letter findAnotherMatch(ArrayList<Letter> charList, Word word, int x, int y) {
		for (int i = 0; i < charList.size(); i++) {
			for (int row = x; row < board.length; row++) {
				for (int col = y; col < board.length; col++) {
					if (charList.get(i).getLetter().equalsIgnoreCase(board[row][col].getLetter())) {
						if (board[row][col].isOccupied() == false) {
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

	public int getNumberOfWords() {
		return numberOfWords;
	}

	public void setNumberOfWords(int numberOfWords) {
		this.numberOfWords = numberOfWords;
	}

}
