package ClientGUI;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel{

	private JPanel boardArea;
	private JLabel alertLabel;
	private JLabel clockLabel;
	private JButton resignButton;
	private JButton drawButton;
	private JButton alertButton;
	private JPanel mainPanel;
	private JPanel bottom;
	
	public BoardPanel()
	{
		mainPanel = new JPanel(new BorderLayout());
		bottom = new JPanel(new FlowLayout());
		mainPanel.add(bottom, BorderLayout.SOUTH);
		
		JPanel left2 = new JPanel();
		left2.add(drawButton);
		bottom.add(left2, FlowLayout.LEFT);
		
		JPanel right2 = new JPanel();
		right2.add(resignButton);
		bottom.add(right2, FlowLayout.RIGHT);
		
		
	}
	
	
}
