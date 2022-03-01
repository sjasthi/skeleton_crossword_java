package com.example.powerpoint;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTable;
import org.apache.poi.hslf.usermodel.HSLFTableCell;
import org.apache.poi.hslf.usermodel.HSLFTextBox;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.sl.usermodel.VerticalAlignment;

import com.example.skeleton.Letter;
import com.example.skeleton.SkeletonBoard;

import org.apache.poi.sl.usermodel.TableCell.BorderEdge;

public class Powerpoint {
	private HSLFSlideShow ppt;
	
	
	public Powerpoint() {
		ppt = new HSLFSlideShow();
	}
	
	public HSLFSlide createSlide(int count) {
		HSLFSlide slide = this.ppt.createSlide();
		HSLFTextBox title = slide.createTextBox();
		HSLFTextParagraph p = title.getTextParagraphs().get(0);
		HSLFTextRun r = p.getTextRuns().get(0);
		r.setFontColor(Color.black);
		r.setText("Skeleton Puzzle #" + count);
		r.setFontSize(24.0);
		title.setAnchor(new Rectangle(160, 10, 400, 100));
		return slide;
	}
	
	public HSLFTable createTable(HSLFSlide slide, int row, int col, Letter[][] board) {
		HSLFTable table = slide.createTable(row, col);
		table.moveTo(210, 100);
		for (int i = 0; i < table.getNumberOfColumns(); i++) {
			table.setColumnWidth(i, 30);
		}

		for (int i = 0; i < table.getNumberOfRows(); i++) {
			table.setRowHeight(i,30);
		}
		
		for(int i = 0; i < table.getNumberOfRows(); i++) {
			for(int k = 0; k < table.getNumberOfColumns(); k++) {
				HSLFTableCell cell = table.getCell(i, k);
				cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
				cell.setHorizontalCentered(true);
				cell.setText(board[i][k].getLetter());
				if(!cell.getText().equalsIgnoreCase(" ")) {
					cell.setBorderColor(BorderEdge.top, Color.black);
					cell.setBorderColor(BorderEdge.right, Color.black);
					cell.setBorderColor(BorderEdge.bottom, Color.black);
					cell.setBorderColor(BorderEdge.left, Color.black);
				}

			}
		}
		return table;
	}
	
	public void createAvailableLetters(HSLFSlide slide, SkeletonBoard board) {
		Collections.shuffle(board.getLettersOnBoard());
		HSLFTextBox textBox = slide.createTextBox();
		for(int letterObject = 0; letterObject < board.getLettersOnBoard().size(); letterObject++) {
			textBox.appendText(board.getLettersOnBoard().get(letterObject).getLetter(), false);
		}
		textBox.setAnchor(new Rectangle(100,100,500,100));
		textBox.moveTo(110, 410);
		textBox.setAlignToBaseline(true);
		
	}
	


	public void writeToFile(HSLFSlideShow ppt,String fileName) {
		try {
			FileOutputStream out = new FileOutputStream("E:\\Folders\\Classes\\ICS499\\Powerpoints\\"+fileName+".ppt");
			ppt.write(out);
			out.close();
			ppt.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	public HSLFSlideShow getPpt() {
		return ppt;
	}

	public void setPpt(HSLFSlideShow ppt) {
		this.ppt = ppt;
	}
	
}