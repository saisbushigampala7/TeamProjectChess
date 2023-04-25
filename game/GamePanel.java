package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;

/**
 * Class that creates a visual representation of the board and allows the user
 * to type their moves.
 * 
 * @author Archer Murray
 */
public class GamePanel extends JFrame
{
	private static final long serialVersionUID = -1478100234224270432L;
	
	// Game-related fields
	private Game game;
	private List<String> legalMoves;
	
	// Panel-related fields
	private JPanel board;
	private JTextField moveField;
	private JButton enterMove;
	
	// Images
	private BufferedImage[] images;
	
	private class BoardPanel extends JPanel
	{
		private static final long serialVersionUID = -5253230713362265205L;
		
		@Override
		public void paintComponent(Graphics page)
		{
			super.paintComponent(page);
			
			int[][] board = game.getBoard();
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					// Set x/y
					int x = 60 * j;
					int y = 60 * (7 - i);
					// If i + j is odd, make the square black
					page.setColor((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
					page.fillRect(x, y, 60, 60);
					// Draw the piece, if any
					if (board[i][j] == Game.NONE) {
						continue;
					}
					page.drawImage(images[board[i][j]], x, y, 60, 60, null);
				}
			}
		}
	}
	
	private class EventHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String move = moveField.getText();
			if (legalMoves.contains(move)) {
				game.makeMove(move);
				legalMoves = game.getAllLegalMoves();
				moveField.setText("");
				board.repaint();
			}
		}
	}
	
	/**
	 * Creates a new GamePanel.
	 */
	public GamePanel()
	{
		// Set up window
		this.setLayout(null);
		this.setTitle("Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Load images
		// Code from: http://compsci.ca/v3/viewtopic.php?t=28197
		images = new BufferedImage[Game.COLOR_MASK];
		try {
			images[Game.WHITE | Game.PAWN] =
					ImageIO.read(new File("game/wpawn.png"));
			images[Game.WHITE | Game.KNIGHT] =
					ImageIO.read(new File("game/wknight.png"));
			images[Game.WHITE | Game.BISHOP] =
					ImageIO.read(new File("game/wbishop.png"));
			images[Game.WHITE | Game.ROOK] =
					ImageIO.read(new File("game/wrook.png"));
			images[Game.WHITE | Game.QUEEN] =
					ImageIO.read(new File("game/wqueen.png"));
			images[Game.WHITE | Game.KING] =
					ImageIO.read(new File("game/wking.png"));
			images[Game.BLACK | Game.PAWN] =
					ImageIO.read(new File("game/bpawn.png"));
			images[Game.BLACK | Game.KNIGHT] =
					ImageIO.read(new File("game/bknight.png"));
			images[Game.BLACK | Game.BISHOP] =
					ImageIO.read(new File("game/bbishop.png"));
			images[Game.BLACK | Game.ROOK] =
					ImageIO.read(new File("game/brook.png"));
			images[Game.BLACK | Game.QUEEN] =
					ImageIO.read(new File("game/bqueen.png"));
			images[Game.BLACK | Game.KING] =
					ImageIO.read(new File("game/bking.png"));
		} catch (IOException e) {
			System.out.println("Error loading image: " + e);
		}
		
		// Create board
		board = new BoardPanel();
		board.setBounds(35, 0, 480, 480);
		this.add(board);
		
		// Create move entry text field and button
		JLabel moveLabel = new JLabel("Enter move:", JLabel.RIGHT);
		moveLabel.setBounds(50, 500, 150, 25);
		this.add(moveLabel);
		moveField = new JTextField("");
		moveField.setBounds(200, 500, 150, 25);
		this.add(moveField);
		enterMove = new JButton("Submit");
		enterMove.addActionListener(new EventHandler());
		enterMove.setBounds(350, 500, 150, 25);
		this.add(enterMove);
		
		// Make window visible
		this.setSize(550, 600);
		this.setVisible(true);
		
		// Set up game
		this.setGame(new Game());
	}
	
	/**
	 * Sets the game this GamePanel is based on to the passed-in game.
	 * 
	 * @param game The game to have this GamePanel based on.
	 */
	public void setGame(Game game)
	{
		this.game = game;
		this.legalMoves = game.getAllLegalMoves();
		this.board.repaint();
	}
	
	/**
	 * Displays a GamePanel for testing purposes.
	 * 
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args)
	{
		new GamePanel();
	}
}
