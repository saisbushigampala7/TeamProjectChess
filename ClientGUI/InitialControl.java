package ClientGUI;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InitialControl implements ActionListener
{
	// Private data field for storing the container.
	private JPanel container;
	private ChatClient client;
	
	// Constructor for the initial controller.
	public InitialControl(JPanel container, ChatClient client)
	{
		this.container = container;
		this.client = client;
	}
	
	// Handle button clicks.
	public void actionPerformed(ActionEvent ae)
	{
		// Get the name of the button clicked.
		String command = ae.getActionCommand();
		
		if (command.equals("Login")) {
			// The Login button takes the user to the login panel.
			LoginPanel loginPanel = (LoginPanel)container.getComponent(1);
			loginPanel.setError("");
			CardLayout cardLayout = (CardLayout)container.getLayout();
			cardLayout.show(container, "2");
		} else if (command.equals("Create")) {
			// The Create button takes the user to the create account panel.
			CreateAccountPanel createAccountPanel =
					(CreateAccountPanel)container.getComponent(2);
			createAccountPanel.setError("");
			CardLayout cardLayout = (CardLayout)container.getLayout();
			cardLayout.show(container, "3");
		} else if (command.equals("Game")) {
			// The Game button (TEST PURPOSES ONLY) takes the user to the game
			// panel.
			CardLayout cardLayout = (CardLayout)container.getLayout();
			cardLayout.show(container, "5");
		}
	}
}
