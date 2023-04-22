package ClientGUI;

import javax.swing.*;
import java.awt.*;
import game.*;


/*
 *  Part of the code came is inspired from the following:
 *  https://www.roseindia.net/java/example/java/swing/chess-application-swing.shtml
 */

public class BoardPanel extends JFrame{

	private JPanel boardArea;
	private JLabel alertLabel;
	private JLabel clockLabel;
	private JButton resignButton;
	private JButton drawButton;
	private JButton alertButton;
	private JPanel mainPanel;
	private JPanel board;
	private JLabel turn;
	private ImageIcon icon;
	private JPanel panel;
	private JLabel piece;
	


	public BoardPanel()
	{
		mainPanel = new JPanel(new BorderLayout());
		
		// set Window
		this.setTitle("Client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(mainPanel);
		this.setVisible(true);
		this.setSize(550, 600);

		// bottom panel for resign and draw buttons
		JPanel bottom = new JPanel(new FlowLayout());
		mainPanel.add(bottom, BorderLayout.SOUTH);
		mainPanel.setVisible(true);

		drawButton = new JButton("Draw");

		JPanel left2 = new JPanel();
		left2.add(drawButton);
		bottom.add(left2);

		resignButton = new JButton("Resign");

		JPanel right2 = new JPanel();
		right2.add(resignButton);
		bottom.add(right2);
		
		// top panel for alerts and clock stuff
		JPanel top = new JPanel(new FlowLayout());
		mainPanel.add(top, BorderLayout.NORTH);

		JPanel left1 = new JPanel(new GridLayout(1, 1, 2, 2));
		alertLabel = new JLabel("Alert");
		alertButton = new JButton("AlertB");

		left1.add(alertLabel);
		left1.add(alertButton);
		left1.setAlignmentX(LEFT_ALIGNMENT);
		top.add(left1);

		clockLabel = new JLabel("Clock");

		JPanel right1 = new JPanel(new GridLayout(1, 1, 2, 2));
		right1.add(clockLabel);
		top.add(right1);

		// Actual Board code
		boardArea = new JPanel(new BorderLayout());
		mainPanel.add(boardArea, BorderLayout.CENTER);
		turn = new JLabel(" Turn");
		turn.setVisible(true);
		boardArea.add(turn, BorderLayout.NORTH);
		
		// Chess Board code
		board = new JPanel(new GridLayout(8, 8));
		board.setBackground(Color.DARK_GRAY);
		
		
		for (int i = 0; i < 64; i++) 
		{
			JPanel positions = new JPanel();
			board.add(positions);
			positions.setVisible(true);
			
			int row = (i / 8) % 2;
			if (row == 0)
				positions.setBackground( i % 2 == 0 ? Color.black : Color.gray );
			else
				positions.setBackground( i % 2 == 0 ? Color.gray : Color.black );
			
		}
		board.setVisible(true);
		boardArea.add(board, BorderLayout.CENTER);
		
		// Board Color code
		mainPanel.setBackground(Color.GRAY);
		top.setBackground(Color.DARK_GRAY);
		bottom.setBackground(Color.DARK_GRAY);
		
		// Adding Pieces code
		String[] pics = new String [] {"/pawn.png", "/bpawn.png", "/wrook.png", "/brook.png", "/wknight.png", 
				"/bknight.png", "/bbishop.png", "/wbishop.png", "/bqueen.png", "/wqueen.png", "/bking.png", "/wking.png"};
		
		int index = 0;
		for (int j = 0; j < 64; j++)
		{
			if (j == 0 || j == 7)
			{
				index = 2;
			}
			else if (j == 1 || j == 6)
			{
				index = 4;
			}
			else if (j == 1 || j == 6)
			{
				index = 4;
			}
			else if (j == 2 || j == 5)
			{
				index = 7;
			}
			else if (j == 3)
			{
				index = 11;
			}
			else if (j == 4)
			{
				index = 9;
			}
			else if (j >= 8 && j <= 15)
			{
				index = 0;
			}
			else if (j >= 48 && j <= 55)
			{
				index = 1;
			}
			else if (j == 56 || j == 63)
			{
				index = 3;
			}
			else if (j == 57 || j == 62)
			{
				index = 5;
			}
			else if(j == 58 || j == 61)
			{
				index = 6;
			}
			else if (j == 59)
			{
				index = 10;
			}
			else if (j == 60)
			{
				index = 8;
			}
			else
			{
				continue;
			}
				
			icon = new ImageIcon(getClass().getResource(pics[index]));
			icon.setImage(icon.getImage().getScaledInstance(40, 50, Image.SCALE_DEFAULT));
			piece = new JLabel(icon);
			panel =(JPanel)board.getComponent(j);
			panel.add(piece);
			panel.setVisible(true);
			panel.validate();
		}
		
		// Piece Movable code
		
		
		
		
	}

	public static void main(String[] args)
	{
		new BoardPanel();
	}

}
