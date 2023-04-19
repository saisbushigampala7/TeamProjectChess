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
		
		JPanel left1 = new JPanel();
		alertLabel = new JLabel("Alert");
		alertButton = new JButton("AlertB");
		
		left1.add(alertLabel);
		left1.add(alertButton);
		top.add(left1);
		
		clockLabel = new JLabel("Clock");
		
		JPanel right1 = new JPanel();
		right1.add(clockLabel);
		top.add(right1);
		
		
		
	}
	
	public static void main(String[] args)
	{
		new BoardPanel();
	}
	
}
