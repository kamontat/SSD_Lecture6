package ui;

import java.io.InputStream;
import java.util.*;

import game.Game;

public class Terminal {
	
	private Game game;
	private Scanner scanner;
	
	public Terminal(InputStream inputStream, Game game) {
		scanner = new Scanner(inputStream);
		this.game = game;
	}
	
	private int[] input() {
		System.out.println(String.format("Player %s's turn.", game.getCurrentPlayerName()));
		try {
			System.out.print("Please select row: ");
			int row = scanner.nextInt() - 1;
			
			System.out.print("Please select column: ");
			int col = scanner.nextInt() - 1;
			
			return new int[]{row, col};
		} catch (InputMismatchException e) {
			return null;
		} finally {
			scanner.nextLine();
		}
	}
	
	public void run() {
		game.start();
		
		renderBoard(game);
		while (!game.isEnd()) {
			int[] location = input();
			game.currentPlayerTakesAction(location[0], location[1]);
			renderBoard(game);
		}
		
		System.out.printf("%s is winner.\n", game.getWinnerName());
	}
	
	private void renderBoard(Game game) {
		int size = game.getBoardSize();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				String s = game.getSymbolOnBoard(row, col);
				if (s == null) {
					s = "_";
				}
				System.out.print(s);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Terminal ui = new Terminal(System.in, new Game());
		ui.run();
	}
	
}
