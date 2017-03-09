package ui;

import game.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bubblebitoey on 3/9/2017 AD.
 */
public class TablePanel extends JPanel {
	private int size;
	private Game game;
	
	public TablePanel(int size, Game game) {
		this.game = game;
		this.size = size;
	}
	
	public void newGame(Game game) {
		this.game = game;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawBackground(g);
		drawLines(g);
		drawSymbols(g);
	}
	
	private void drawBackground(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, size, size);
	}
	
	private void drawLines(Graphics g) {
		int gap = squareSize();
		g.setColor(Color.black);
		for (int i = 0; i < Game.BOARD_SIZE; i++) {
			g.drawLine(0, i * gap, size, i * gap);
			g.drawLine(i * gap, 0, i * gap, size);
		}
	}
	
	private void drawSymbols(Graphics g) {
		int gap = squareSize();
		for (int row = 0; row < game.getBoardSize(); row++) {
			for (int col = 0; col < game.getBoardSize(); col++) {
				int y = row * gap + gap / 2;
				int x = col * gap + gap / 2;
				String symbol = game.getSymbolOnBoard(row, col);
				if (symbol != null) {
					g.drawString(symbol, x, y);
				}
			}
		}
	}
	
	private int squareSize() {
		return size / Game.BOARD_SIZE;
	}
}
