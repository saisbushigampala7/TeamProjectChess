package game;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Before;

import ClientGUI.ChatClient;
import ClientGUI.CreateAccountControl;
import ClientGUI.CreateAccountPanel;
import ClientGUI.InitialControl;
import ClientGUI.InitialPanel;
import ClientGUI.LoginControl;
import ClientGUI.LoginPanel;

import org.junit.*;

public class GamePanelTest
{
	private GameControl gc;
	private GamePanel gp;
	
	@Before
	public void setupBeforeTest()
	{
		ChatClient client = new ChatClient();
		CardLayout cardLayout = new CardLayout();
		JPanel container = new JPanel(cardLayout);
		JFrame testdummy = new JFrame();
		gc = new GameControl(container, client);
		gp = new GamePanel(gc);
		InitialControl ic = new InitialControl(container, client);
		LoginControl lc = new LoginControl(container, client);
		CreateAccountControl cac = new CreateAccountControl(container, client);
		GameControl gc = new GameControl(container, client);
		
		// Set the client info
		client.setLoginControl(lc);
		client.setCreateAccountControl(cac);
		client.setGameControl(gc);
		
		// Create the four views. (need the controller to register with the
		// Panels
		JPanel view1 = new InitialPanel(ic);
		JPanel view2 = new LoginPanel(lc);
		JPanel view3 = new CreateAccountPanel(cac);
		JPanel view5 = new GamePanel(gc);
		
		// Add the views to the card layout container.
		container.add(view1, "1");
		container.add(view2, "2");
		container.add(view3, "3");
		container.add(view5, "5");
		container.add(gp, "4");
		
		cardLayout.show(container, "4");
		// Set the title and default close operation.
		testdummy.setTitle("Chess Client");
		testdummy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		testdummy.setLayout(null);
		container.setBounds(25, 25, 650, 650);
		testdummy.add(container);
		
		// Show the JFrame.
		testdummy.setSize(700, 700);
		testdummy.setVisible(true);
		Game g = new Game();
		gp.setGame(g);
	}
	
	@Test
	public void testDraw()
	{
		gp.getDrawButton().doClick();
		
		Robot bot;
		try {
			bot = new Robot();
			// Keys can be pressed
			bot.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(500);
			bot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals("check Gamepanel GUI", Game.DRAW,
				gp.getGame().getResult());
	}
	
	@Test
	public void testResign()
	{
		gp.getResignButton().doClick();
		
		try {
			Robot bot = new Robot();
			// Keys can be pressed
			bot.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(500);
			bot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals("check Gamepanel GUI", Game.BLACK,
				gp.getGame().getResult());
	}
	
	@Test
	public void testMove()
	{
		Game testGame = new Game();
		gc.getClient().setIsTurn(true);
		testGame.makeMove("a2a3");
		gp.getMoveField().setText("a2a3");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gp.getSubmitButton().doClick();
		assertArrayEquals("check Gamepanel GUI", testGame.getBoard(),
				gp.getGame().getBoard());
	}
}
