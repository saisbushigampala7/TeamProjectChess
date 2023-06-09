package Server;

import java.awt.*;
import javax.swing.*;

import ClientGUI.CreateAccountData;
import game.Game;
import ClientGUI.*;

import java.io.IOException;
import java.sql.SQLException;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ChatServer extends AbstractServer
{
	// Data fields for this chat server.
	private JTextArea log;
	private JLabel status;
	private boolean running = false;
	// private Database database; //DATABASE STUFF, I'll uncomment once
	// implemented
	private ConnectionToClient player1;
	private ConnectionToClient player2;
	private Game chess;
	private ConnectionToClient currentPlayer;
	private Database database;
	private boolean useDatabase = false;
	
	// Constructor for initializing the server with default settings.
	public ChatServer() throws IOException
	{
		super(12345);
		this.setTimeout(500);
		if (useDatabase) {
			database = new Database();
			database.setConnection("Server/db.properties");
		}
	}
	
	// DATABASE STUFF, I'll uncomment once implemented
	void setDatabase(Database database)
	{
		this.database = database;
	}
	
	// Getter that returns whether the server is currently running.
	public boolean isRunning()
	{
		return running;
	}
	
	// Setters for the data fields corresponding to the GUI elements.
	public void setLog(JTextArea log)
	{
		this.log = log;
	}
	
	public void setStatus(JLabel status)
	{
		this.status = status;
	}
	
	// When the server starts, update the GUI.
	public void serverStarted()
	{
		running = true;
		status.setText("Listening");
		status.setForeground(Color.GREEN);
		log.append("Server started\n");
	}
	
	// When the server stops listening, update the GUI.
	public void serverStopped()
	{
		status.setText("Stopped");
		status.setForeground(Color.RED);
		log.append(
				"Server stopped accepting new clients - press Listen to start accepting new clients\n");
	}
	
	// When the server closes completely, update the GUI.
	public void serverClosed()
	{
		running = false;
		status.setText("Close");
		status.setForeground(Color.RED);
		log.append(
				"Server and all current clients are closed - press Listen to restart\n");
	}
	
	// When a client connects or disconnects, display a message in the log.
	public void clientConnected(ConnectionToClient client)
	{
		log.append("Client " + client.getId() + " connected\n");
	}
	
	// When a message is received from a client, handle it.
	public void handleMessageFromClient(Object arg0, ConnectionToClient arg1)
	{
		// If we received LoginData, verify the account information.
		if (arg0 instanceof LoginData) {
			// Check the username and password with the database.
			LoginData data = (LoginData)arg0;
			Object result = null;
			// DATABASE STUFF, I'll uncomment once implemented
			if (!useDatabase || database.verifyAccount(data.getUsername(),
					data.getPassword())) {
				result = "LoginSuccessful";
				log.append("Client " + arg1.getId() +
						" successfully logged in as " + data.getUsername() +
						"\n");
			} else {
				result = new Error("The username and password are incorrect.",
						"Login");
				log.append("Client " + arg1.getId() + " failed to log in\n");
			}
			
			// Send the result to the client.
			try {
				arg1.sendToClient(result);
			} catch (IOException e) {
				return;
			}
		}
		
		else if (arg0 instanceof String) {
			// Get the text of the message.
			String message = (String)arg0;
			if (message.equals("GameStart")) {
				if (player1 == null) {
					player1 = arg1;
					try {
						player1.sendToClient(
								"You are player 1!, waiting for second player\n");
					} catch (IOException e) {
						return;
					}
				} else if (player2 == null) {
					chess = new Game();
					// Player 2 send message then game
					player2 = arg1;
					try {
						player2.sendToClient(
								"Your opponent is Client " + player1.getId() +
										" game begin!(Player 1's turn\n");
					} catch (IOException e) {
						return;
					}
					// Player 1 send message
					try {
						player1.sendToClient("Your opponent is Client " +
								player2.getId() + " game begin!(Your turn)\n");
					} catch (IOException e) {
						return;
					}
					// Send game
					try {
						currentPlayer = player1;
						player1.sendToClient(chess);
					} catch (IOException e) {
						return;
					}
				} else {
					try {
						arg1.sendToClient("Sorry, server full");
					} catch (IOException e) {
						return;
					}
				}
			} else if (message.equals("Draw")) {
			}
		}
		
		// If we received CreateAccountData, create a new account.
		else if (arg0 instanceof CreateAccountData) {
			// Try to create the account.
			CreateAccountData data = (CreateAccountData)arg0;
			Object result = null;
			// DATABASE STUFF, I'll uncomment once implemented
			try {
				if (!useDatabase || database.createNewAccount(
						data.getUsername(), data.getPassword())) {
					result = "CreateAccountSuccessful";
					log.append("Client " + arg1.getId() +
							" created a new account called " +
							data.getUsername() + "\n");
				} else {
					result = new Error("The username is already in use.",
							"CreateAccount");
					log.append("Client " + arg1.getId() +
							" failed to create a new account\n");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Send the result to the client.
			try {
				arg1.sendToClient(result);
			} catch (IOException e) {
				return;
			}
		} else if (arg0 instanceof Game) {
			if (arg1.equals(currentPlayer)) {
				chess = (Game)arg0;
				// swap to other player's turn
				if (currentPlayer.equals(player1)) {
					currentPlayer = player2;
				} else {
					currentPlayer = player1;
				}
				// Send to the player who's turn it is
				try {
					currentPlayer.sendToClient(chess);
				} catch (IOException e) {
					return;
				}
			} else if (arg1.equals(player1)) {
				try {
					arg1.sendToClient(">:(   It's not your turn yet pal!");
				} catch (IOException e) {
					return;
				}
			} else {
				try {
					arg1.sendToClient(">:(   It's not your turn yet pal!");
				} catch (IOException e) {
					return;
				}
			}
		}
	}
	
	// Method that handles listening exceptions by displaying exception
	// information.
	public void listeningException(Throwable exception)
	{
		running = false;
		status.setText("Exception occurred while listening");
		status.setForeground(Color.RED);
		log.append("Listening exception: " + exception.getMessage() + "\n");
		log.append("Press Listen to restart server\n");
	}
}
