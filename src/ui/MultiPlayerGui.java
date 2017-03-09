package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import game.Game;
import game.network.GameClient;
import game.network.GameServer;
import game.network.Network;

public class MultiPlayerGui extends JFrame implements Observer {
	
	public static final int WINDOW_SIZE = 300;
	
	private Game game;
	private GameServer gameServer;
	private GameClient gameClient;
	
	private boolean isServer;
	private boolean isClient;
	
	private JLabel infoText;
	private JPanel mainPanel;
	private JButton startServerButton;
	private JButton startClientButton;
	
	public MultiPlayerGui() {
		super("SSD - Tic Tac Toe Multiplayer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		
		game = new Game();
		
		initComponents();
	}
	
	public void start() {
		gameServer = new GameServer();
		gameServer.addObserver(this);
		gameClient = new GameClient();
		gameClient.addObserver(this);
		
		pack();
		setVisible(true);
	}
	
	public void startServer() {
		gameServer.start();
		isServer = true;
		infoText.setText("Server Started");
	}
	
	public void startClient() {
		gameClient.connect();
		isClient = true;
		infoText.setText("Client Connected");
	}
	
	private void initComponents() {
		setSize(WINDOW_SIZE, WINDOW_SIZE);
		mainPanel = new TablePanel(WINDOW_SIZE, game);
		
		mainPanel.setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
		mainPanel.addMouseListener(new MouseHandler());
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		
		infoText = new JLabel(" Welcome to Tic-Tac-Toe");
		infoText.setFont(new Font(infoText.getName(), Font.PLAIN, 20));
		add(infoText, BorderLayout.NORTH);
		
		add(new JPanel() {
			{
				setLayout(new FlowLayout(FlowLayout.CENTER));
				startServerButton = new JButton("Start Server");
				startServerButton.addActionListener(e -> startServer());
				add(startServerButton);
				startClientButton = new JButton("Start Client");
				startClientButton.addActionListener(e -> startClient());
				add(startClientButton);
			}
		}, BorderLayout.SOUTH);
	}
	
	private int squareSize() {
		return WINDOW_SIZE / Game.BOARD_SIZE;
	}
	
	private int[] toPosition(MouseEvent e) {
		int row = e.getY() / squareSize();
		int col = e.getX() / squareSize();
		return new int[]{row, col};
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals(Network.CONNECT)) {
			game.start();
			refreshGui();
		}
		
		if (Game.class == arg.getClass()) {
			this.game = (Game) arg;
		}
		
		refreshGui();
	}
	
	public void refreshGui() {
		((TablePanel) mainPanel).newGame(game);
		
		if (!(isServer && game.isP1Turn()) || (isClient && game.isP2Turn())) {
			infoText.setText("Your Turn");
			mainPanel.setEnabled(false);
		} else {
			infoText.setText("Your Opponent's Turn");
		}
		mainPanel.repaint();
		
		if (game.isEnd()) {
			JOptionPane.showMessageDialog(this, game.getWinnerName() + " Win!");
			game = new Game();
		}
	}
	
	private class MouseHandler extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (!(isServer && game.isP1Turn()) || (isClient && game.isP2Turn())) {
				return;
			}
			if (game.isEnd()) {
				return;
			}
			
			int[] pos = toPosition(e);
			try {
				game.currentPlayerTakesAction(pos[0], pos[1]);
			} catch (NullPointerException ne) {
				System.out.println("game not start");
			}
			
			refreshGui();
			gameServer.send(game);
			gameClient.send(game);
		}
	}
	
	public static void main(String[] args) {
		MultiPlayerGui gui = new MultiPlayerGui();
		gui.start();
	}
	
}
