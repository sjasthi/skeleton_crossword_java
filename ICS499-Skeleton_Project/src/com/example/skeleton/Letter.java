package com.example.skeleton;

public class Letter {
	private int row;
	private int column;
	private String letter;
	private Word word;
	private boolean occupied;
	
	
	public Letter(int row, int column, String letter, Word word, boolean occupied) {
		this.row = row;
		this.column = column;
		this.letter = letter;
		this.word = word;
		this.occupied = occupied;
	}
	
	public Letter(String letter, Word word) {
		this.letter = letter;
		this.word = word;
	}
	
	
	public boolean doesContain(String letter) {
		if(letter.equalsIgnoreCase(this.letter)) {
			return true;
		}
		return false;
	}


	public int getRow() {
		return row;
	}


	public void setRow(int x) {
		this.row = x;
	}


	public int getColumn() {
		return column;
	}


	public void setColumn(int y) {
		this.column = y;
	}




	public Word getWord() {
		return word;
	}


	public void setWord(Word word) {
		this.word = word;
	}


	public String getLetter() {
		return letter;
	}


	public void setLetter(String letter) {
		this.letter = letter;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	
	
}
