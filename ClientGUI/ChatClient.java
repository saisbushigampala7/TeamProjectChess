package ClientGUI;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import game.Game;
import game.*;
import ocsf.client.AbstractClient;

public class ChatClient extends AbstractClient
{
  // Private data fields for storing the GUI controllers.
  private LoginControl loginControl;
  private CreateAccountControl createAccountControl;
  private GameControl gameControl;
  private Scanner in = new Scanner(System.in);	//I just need this for testing, once final product is ready DELETE this
  private Game chess;
  private boolean isTurn;
  // Setters for the GUI controllers.
  public void setLoginControl(LoginControl loginControl)
  {
    this.loginControl = loginControl;
  }
  public void setCreateAccountControl(CreateAccountControl createAccountControl)
  {
    this.createAccountControl = createAccountControl;
  }
  public void setGameControl (GameControl gamecontrol)
  {
	  this.gameControl = gamecontrol;
  }
  public void setIsTurn (boolean val)
  {
	  isTurn = val;
  }
  public boolean getIsTurn ()
  {
	  return isTurn;
  }

  // Constructor for initializing the client with default settings.
  public ChatClient()
  {
    super("localhost", 8300);
  }
  
  // Method that handles messages from the server.
  public void handleMessageFromServer(Object arg0)
  {
    // If we received a String, figure out what this event is.
    if (arg0 instanceof String)
    {
      // Get the text of the message.
      String message = (String)arg0;
      
      // If we successfully logged in, tell the login controller.
      if (message.equals("LoginSuccessful"))
      {
        loginControl.loginSuccess();
      }
      
      // If we successfully created an account, tell the create account controller.
      else if (message.equals("CreateAccountSuccessful"))
      {
        createAccountControl.createAccountSuccess();
      }
      else
      {
    	  System.out.print(message);
      }
    }
    
    // If we received an Error, figure out where to display it.
    else if (arg0 instanceof Error)
    {
      // Get the Error object.
      Error error = (Error)arg0;
      
      // Display login errors using the login controller.
      if (error.getCause().equals("Login"))
      {
        loginControl.displayError(error.getMessage());
      }
      
      // Display account creation errors using the create account controller.
      else if (error.getCause().equals("CreateAccount"))
      {
        createAccountControl.displayError(error.getMessage());
      }
    }
    
    else if (arg0 instanceof Game)
    {
    	//Set up board
    	chess = (Game)arg0;
    	gameControl.setGame(chess);
    	isTurn = true;
    	
		
		
		
    }
  }  
}
