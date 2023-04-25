package game;

import java.awt.event.*;
import java.io.IOException;

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
	
	public void setGame (Game chess)
	{
		GamePanel gp = (GamePanel)container.getComponent(4);
		
		gp.setGame(chess);
	}
	
	public ChatClient getClient ()
	{
		return client;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		GamePanel gp = (GamePanel)container.getComponent(4);
		String command = e.getActionCommand();
		
		// TODO: Integrate these with client/server
		if (command == "Submit") {
			if (client.getIsTurn())
			{
				gp.makeMove();
				client.setIsTurn(false);
				//Send to Server
				try
		 	     {
		   			client.sendToServer(gp.getGame());
		 	     }
		 	     catch (IOException er)
		 	     {
		 	    	 er.printStackTrace();
		 	    	  return;
		 	     }
			}
			
		} else if (command == "Draw") {
			Game game = gp.getGame();
			game.declareDraw();
			gp.setGame(game);
			
			if (client.getIsTurn())
			{
				gp.makeMove();
				client.setIsTurn(false);
				//Send to Server
				try
		 	     {
		   			client.sendToServer(gp.getGame());
		 	     }
		 	     catch (IOException er)
		 	     {
		 	    	 er.printStackTrace();
		 	    	  return;
		 	     }
			}
		} else if (command == "Resign") {
			Game game = gp.getGame();
			game.resign();
			gp.setGame(game);
			
			if (client.getIsTurn())
			{
				gp.makeMove();
				client.setIsTurn(false);
				//Send to Server
				try
		 	     {
		   			client.sendToServer(gp.getGame());
		 	     }
		 	     catch (IOException er)
		 	     {
		 	    	 er.printStackTrace();
		 	    	  return;
		 	     }
			}
		}
	}
}
