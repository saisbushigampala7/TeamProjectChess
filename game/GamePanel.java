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
	private JButton draw;
	private JButton resign;
	private JLabel horizontal;
	private JLabel vertical;
	private JLabel vertical2;
	private JLabel drawLabel;

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
			String command = e.getActionCommand();
			if (command == "Submit")
			{
				String move = moveField.getText();
				if (legalMoves.contains(move)) {
					game.makeMove(move);
					legalMoves = game.getAllLegalMoves();
					moveField.setText("");
					board.repaint();
					endGameDialog();
				} else {
					JOptionPane.showMessageDialog(null, "Illegal move.",
							"Illegal Move", JOptionPane.INFORMATION_MESSAGE);
				}

			}
			else if (command == "Draw" )
			{
				drawLabel.setVisible(true);
				moveField.setEditable(false);
			}
			else if (command == "Resign")
			{
				// Directs back to BufferPanel
			}

		}
	}

	/**
	 * Creates a new GamePanel.
	 */
	public GamePanel()
	{
		this.getContentPane().setBackground(Color.GRAY);
		// Set up window
		this.getContentPane().setLayout(null);
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
		moveLabel.setBounds(50, 520, 150, 25);
		this.add(moveLabel);
		moveField = new JTextField("");
		moveField.setBounds(200, 520, 150, 25);
		this.add(moveField);
		enterMove = new JButton("Submit");
		enterMove.addActionListener(new EventHandler());
		enterMove.setBounds(350, 520, 150, 25);
		this.add(enterMove);
		
		drawLabel = new JLabel("IT'S A DRAW!");
		drawLabel.setBounds(200, 540, 150, 25);
		drawLabel.setVisible(false);
		this.add(drawLabel);

		draw = new JButton("Draw");
		draw.setBounds(21, 540, 85, 21);
		this.add(draw);
		draw.addActionListener(new EventHandler());

		resign = new JButton("Resign");
		resign.setBounds(21, 550, 85, 21);
		this.add(resign);
		
		horizontal = new JLabel("A - H");
		horizontal.setBounds(223, 493, 85, 25);
		this.add(horizontal);
		
		vertical = new JLabel("8");
		vertical.setBounds(548, 0, 28, 59);
		this.add(vertical);
		
		vertical2 = new JLabel("1");
		vertical2.setBounds(548, 428, 45, 33);
		this.add(vertical2);
		resign.addActionListener(new EventHandler());

		// Make window visible
		this.setSize(590, 620);
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
		this.endGameDialog();
	}

	/**
	 * Displays a dialog containing the game result, if the game has ended.
	 */
	public void endGameDialog()
	{
		if (!this.game.gameEnded()) {
			return;
		}
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
