package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;

/**
 * Class that creates a visual representation of the board and allows the user
 * to type their moves.
 * 
 * @author Archer Murray
 */
public class GamePanel extends JPanel
{
	private static final long serialVersionUID = -1478100234224270432L;
	
	// Game-related fields
	private Game game;
	private List<String> legalMoves;
	
	// Panel-related fields
	private JPanel board;
	private JTextField moveField;
	private JButton enterMove;
	private JButton draw;
	private JButton resign;
	private JLabel horizontal;
	private JLabel vertical;
	private JLabel vertical2;
	
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
	
	/**
	 * Creates a new GamePanel.
	 */
	public GamePanel(GameControl gc)
	{
		// Set up window
		this.setBackground(Color.GRAY);
		this.setLayout(null);
		
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
		moveLabel.setBounds(50, 520, 150, 25);
		this.add(moveLabel);
		moveField = new JTextField("");
		moveField.setBounds(200, 520, 150, 25);
		this.add(moveField);
		enterMove = new JButton("Submit");
		enterMove.addActionListener(gc);
		enterMove.setBounds(350, 520, 150, 25);
		this.add(enterMove);
		
		draw = new JButton("Draw");
		draw.setBounds(125, 545, 150, 25);
		this.add(draw);
		draw.addActionListener(gc);
		
		resign = new JButton("Resign");
		resign.setBounds(275, 545, 150, 25);
		this.add(resign);
		resign.addActionListener(gc);
		
		horizontal = new JLabel("A - H");
		horizontal.setBounds(223, 493, 85, 25);
		this.add(horizontal);
		
		vertical = new JLabel("8");
		vertical.setBounds(548, 0, 28, 59);
		this.add(vertical);
		
		vertical2 = new JLabel("1");
		vertical2.setBounds(548, 428, 45, 33);
		this.add(vertical2);
		
		// Make window visible
		this.setSize(590, 620);
		this.setVisible(true);
		
		// Set up game
		this.setGame(new Game());
		
		
	}
	public JButton getDrawButton()
	{
			return draw;
	}
	public JButton getResignButton()
	{
			return resign;
	}
	public JButton getSubmitButton()
	{
			return enterMove;
	}
	public JTextField getMoveField()
	{
			return moveField;
	}
	/**
	 * Sets the game this GamePanel is based on to the passed-in game.
	 * 
	 * @param game The game to have this GamePanel based on.
	 */
	public void setGame(Game game)
	{
		this.game = game;
		this.refreshPanel();
	}
	
	/**
	 * Returns the game this GamePanel is based on.
	 * 
	 * @return The game this GamePanel is based on.
	 */
	public Game getGame()
	{
		return this.game;
	}
	
	/**
	 * Makes the move currently typed into the move text field, or displays a
	 * dialog if the move is illegal.
	 */
	public boolean makeMove()
	{
		String move = moveField.getText();
		if (legalMoves.contains(move)) {
			game.makeMove(move);
			this.refreshPanel();
			return true;
		}
		JOptionPane.showMessageDialog(null, "Illegal move.", "Illegal Move",
				JOptionPane.INFORMATION_MESSAGE);
		return false;
	}
	
	/**
	 * Refreshes this GamePanel with the current game state.
	 */
	private void refreshPanel()
	{
		this.legalMoves = game.getAllLegalMoves();
		this.board.repaint();
		
		// If the game has ended, display the result dialog
		if (this.game.gameEnded()) {
			int result = this.game.getResult();
			String message = "";
			switch (result) {
			case Game.WHITE:
				message = "White has won the game!";
				break;
			case Game.BLACK:
				message = "Black has won the game!";
				break;
			case Game.DRAW:
				message = "The game has ended in a draw!";
				break;
			}
			JOptionPane.showMessageDialog(null, message, "Game Result",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
