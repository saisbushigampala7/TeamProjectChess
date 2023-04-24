package game;

import org.junit.*;

public class GameTest
{
	private Game game;
	
	@Before
	public void setUp() throws Exception
	{
		game = new Game();
	}
	
	@Test
	public void testIsInCheck()
	{
		game.setFen("q6k/8/8/8/8/8/8/7K w - - 0 0");
		Assert.assertTrue("Check isInCheck", game.isInCheck());
	}
	
	@Test
	public void testGetAllLegalMoves()
	{
		Assert.assertEquals("Check getAllLegalMoves",
				game.getAllLegalMoves().size(), 20);
	}
	
	@Test
	public void testMakeMove()
	{
		game.makeMove("e2e4");
		int[][] board = game.getBoard();
		Assert.assertEquals("Check makeMove", board[1][4], Game.NONE);
		Assert.assertEquals("Check makeMove", board[3][4],
				Game.WHITE | Game.PAWN);
	}
}
