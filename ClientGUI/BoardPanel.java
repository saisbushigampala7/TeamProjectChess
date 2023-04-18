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
		
		
	}
	
	
}
