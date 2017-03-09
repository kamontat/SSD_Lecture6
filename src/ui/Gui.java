package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import game.Game;

public class Gui extends JFrame {
	
	public static final int WINDOW_SIZE = 300;
	
	private Game game;
	private JLabel currentPlayerText;
	private JPanel mainPanel;
	
	public Gui() {
		super("SSD - Tic Tac Toe ");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		
		game = new Game();
		
		initComponents();
	}
	
	public void start() {
		game.start();
		pack();
		setVisible(true);
	}
	
	private void initComponents() {
		
		setSize(WINDOW_SIZE, WINDOW_SIZE);
		mainPanel = new TablePanel(WINDOW_SIZE, game);
		mainPanel.setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
		mainPanel.addMouseListener(new MouseHandler());
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		currentPlayerText = new JLabel();
		add(currentPlayerText, BorderLayout.NORTH);
	}
	
	private int squareSize() {
		return WINDOW_SIZE / Game.BOARD_SIZE;
	}
	
	private void showGameOverMessage() {
		JOptionPane.showMessageDialog(this, game.getWinnerName() + " Win!");
	}
	
	private class MouseHandler extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent e) {
			int row = e.getY() / squareSize();
			int col = e.getX() / squareSize();
			
			game.currentPlayerTakesAction(row, col);
			repaint();
			
			if (game.isEnd()) {
				showGameOverMessage();
			}
		}
	}
	
	public static void main(String[] args) {
		Gui gui = new Gui();
		gui.start();
	}
	
}
