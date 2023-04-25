package ClientGUI;

import java.awt.*;
import javax.swing.*;

public class BufferPanel extends JPanel
{
	private static final long serialVersionUID = -8532221569751838464L;
	
	private JButton newGame;
	private JButton joinGame;
	private JButton logout;
	
	public BufferPanel(BufferControl bc)
	{
		newGame = new JButton("New Game");
		newGame.addActionListener(bc);
		joinGame = new JButton("Join Game");
		joinGame.addActionListener(bc);
		logout = new JButton("Log Out");
		logout.addActionListener(bc);
		
		JPanel grid = new JPanel(new GridLayout(3, 1, 5, 5));
		grid.add(newGame);
		grid.add(joinGame);
		grid.add(logout);
		this.add(grid);
	}
}
