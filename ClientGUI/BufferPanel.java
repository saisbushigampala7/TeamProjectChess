package ClientGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BufferPanel extends JPanel{

	private JButton newGame;
	private JButton joinGame;
	private JButton logout;

	public BufferPanel()
	{
		newGame = new JButton("New Game");
		joinGame = new JButton("Join Game");
		logout = new JButton("Log Out");

		JPanel grid = new JPanel(new GridLayout(3, 1, 5, 5));
		grid.add(newGame);
		grid.add(joinGame);
		grid.add(logout);
		this.add(grid);

	}
	
	private class EventHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();
			
			if(command == "New Game")
			{
				// Starts a New Game
			}
			else if (command == "Join Game")
			{
				// Joins an Existing Game
			}
			else if (command == "Log Out")
			{
				// Redirects back to Initial Panel, and logs out. 
			}
			
		}
	}

}
