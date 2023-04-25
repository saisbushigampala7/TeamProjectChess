package ClientGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import game.GamePanel;

public class BufferPanel extends JPanel{

	private JButton newGame;
	private JButton joinGame;
	private JButton logout;
	private ChatClient client;
	private JPanel container;

	public BufferPanel(BufferControl bc)
	{


		newGame = new JButton("New Game");
		joinGame = new JButton("Join Game");
		logout = new JButton("Log Out");
		newGame.addActionListener(bc);
		joinGame.addActionListener(bc);
		logout.addActionListener(bc);
		JPanel grid = new JPanel(new GridLayout(3, 1, 5, 5));
		grid.add(newGame);
		grid.add(joinGame);
		grid.add(logout);
		this.add(grid);

	}
	
	

}
