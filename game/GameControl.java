package game;

import java.awt.event.*;
import javax.swing.*;
import ClientGUI.*;

/**
 * Controller for GamePanel.
 * 
 * @author Archer Murray
 */
public class GameControl implements ActionListener
{
	private ChatClient client;
	private JPanel container;
	
	public GameControl(JPanel container, ChatClient client)
	{
		this.container = container;
	    this.client = client;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		GamePanel gp = (GamePanel)container.getComponent(4);
		String command = e.getActionCommand();
		
		// TODO: Integrate these with client/server
		if (command == "Submit") {
			gp.makeMove();
		} else if (command == "Draw") {
			Game game = gp.getGame();
			game.declareDraw();
			gp.setGame(game);
		} else if (command == "Resign") {
			Game game = gp.getGame();
			game.resign();
			gp.setGame(game);
		}
	}
}
