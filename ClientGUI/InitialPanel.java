package ClientGUI;

import java.awt.*;
import javax.swing.*;

public class InitialPanel extends JPanel
{
	private static final long serialVersionUID = -2600577305795625585L;
	
	// Constructor for the initial panel.
	public InitialPanel(InitialControl ic)
	{
		// Create the information label.
		JLabel label = new JLabel("Account Information", JLabel.CENTER);
		
		// Create the login button.
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(ic);
		JPanel loginButtonBuffer = new JPanel();
		loginButtonBuffer.add(loginButton);
		
		// Create the create account button.
		JButton createButton = new JButton("Create");
		createButton.addActionListener(ic);
		JPanel createButtonBuffer = new JPanel();
		createButtonBuffer.add(createButton);
		
		// TODO: TEST PURPOSES ONLY. Remove when login works properly.
		JButton gameButton = new JButton("Game");
		gameButton.addActionListener(ic);
		JPanel gameButtonBuffer = new JPanel();
		gameButtonBuffer.add(gameButton);
		
		// Arrange the components in a grid.
		JPanel grid = new JPanel(new GridLayout(4, 1, 5, 5));
		grid.add(label);
		grid.add(loginButtonBuffer);
		grid.add(createButtonBuffer);
		grid.add(gameButtonBuffer);
		this.add(grid);
	}
}
