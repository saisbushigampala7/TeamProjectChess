package ClientGUI;

import javax.swing.*;
import java.awt.*;
import game.*;

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




	public BoardPanel()
	{
		mainPanel = new JPanel(new BorderLayout());

		this.setTitle("Client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(mainPanel);
		this.setVisible(true);
		this.setSize(400, 400);


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

		board = new JPanel(new GridLayout(8, 8));
		for (int i = 0; i < 64; i++) {
			JPanel square = new JPanel();
			board.add(square);
			square.setVisible(true);

			int row = (i / 8) % 2;
			if (row == 0)
				square.setBackground( i % 2 == 0 ? Color.black : Color.white );
			else
				square.setBackground( i % 2 == 0 ? Color.white : Color.black );
		}
		board.setVisible(true);
		boardArea.add(board, BorderLayout.CENTER);
		mainPanel.setBackground(Color.GRAY);
		
	}

	public static void main(String[] args)
	{
		new BoardPanel();
	}

}
