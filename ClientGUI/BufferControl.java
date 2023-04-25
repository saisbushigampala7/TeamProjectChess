package ClientGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BufferControl implements ActionListener{

	private ChatClient client;
	private JPanel container;
	
	public BufferControl(JPanel container, ChatClient client)
	{
		this.container = container;
	    this.client = client;
	}
	
	
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
				CardLayout cardLayout = (CardLayout)container.getLayout();
			    cardLayout.show(container, "1");
			}
			
		}

	
	
}
