package game;

import java.util.*;

/**
 * Class created to test Game class empirically.
 * <p>
 * This class runs a game, displaying all legal moves and letting the user type
 * moves for both sides, allowing the user to make sure the game is running
 * correctly.
 * 
 * @author Archer Murray
 */
public class GameRunner
{
	public static void main(String[] args)
	{
		Game g = new Game();
		System.out.println(g);
		Scanner in = new Scanner(System.in);
		while (g.getResult() == Game.NONE) {
			if (g.isInCheck()) {
				System.out.println("Check!");
			}
			List<String> moves = g.getAllLegalMoves();
			System.out.println("Legal moves: " + moves);
			String move = "";
			while (!moves.contains(move)) {
				System.out.print("Enter move: ");
				move = in.nextLine();
				if (move.equals("quit")) {
					System.exit(0);
				}
			}
			g.makeMove(move);
			System.out.println(g);
		}
		if (g.getResult() == Game.WHITE) {
			System.out.println("White has won the game!");
		} else if (g.getResult() == Game.BLACK) {
			System.out.println("Black has won the game!");
		} else {
			System.out.println("The game ends in a draw!");
		}
		in.close();
	}
}
