package ClientGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class BufferControl implements ActionListener
{
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
		
		if (command == "New Game") {
			// TODO: Integrate with client/server
			CardLayout cardLayout = (CardLayout)container.getLayout();
			cardLayout.show(container, "5");
			client.setIsTurn(false);
			try {
				client.sendToServer("GameStart");
				
			} catch (IOException er) {
				er.printStackTrace();
			}
			
		} else if (command == "Join Game") {
			// TODO: Integrate with client/server
			CardLayout cardLayout = (CardLayout)container.getLayout();
			cardLayout.show(container, "5");
			client.setIsTurn(false);
			try {
				client.sendToServer("GameStart");
				
			} catch (IOException er) {
				er.printStackTrace();
			}
			
		} else if (command == "Log Out") {
			CardLayout cardLayout = (CardLayout)container.getLayout();
			cardLayout.show(container, "1");
		}
	}
}
