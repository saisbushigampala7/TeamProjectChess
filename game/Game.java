package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a game of chess.
 * <p>
 * This class is currently complete, but untested.
 * <p>
 * Much of the code in this class was inspired by Sebastian Lague's
 * implementation of chess in <a
 * href=https://www.youtube.com/watch?v=U4ogK0MIzqk>this video</a>.
 * 
 * @author Archer Murray
 */
public class Game
{
	/**
	 * Constant representing a blank square.
	 */
	public static final int NONE = 0;
	
	/**
	 * Constant representing a pawn.
	 */
	public static final int PAWN = 1;
	
	/**
	 * Constant representing a knight.
	 */
	public static final int KNIGHT = 2;
	
	/**
	 * Constant representing a bishop.
	 */
	public static final int BISHOP = 3;
	
	/**
	 * Constant representing a rook.
	 */
	public static final int ROOK = 4;
	
	/**
	 * Constant representing a queen.
	 */
	public static final int QUEEN = 5;
	
	/**
	 * Constant representing a king.
	 */
	public static final int KING = 6;
	
	/**
	 * Constant representing the white color.
	 * <p>
	 * This constant is added to one of the piece constants to represent a
	 * particular kind of white piece. For example, a white bishop would be
	 * represented by {@code Game.WHITE + Game.BISHOP}.
	 */
	public static final int WHITE = 8;
	
	/**
	 * Constant representing the black color.
	 * <p>
	 * This constant is added to one of the piece constants to represent a
	 * particular kind of black piece. For example, a black bishop would be
	 * represented by {@code Game.BLACK + Game.BISHOP}.
	 */
	public static final int BLACK = 16;
	
	/**
	 * Constant representing a drawn game.
	 * <p>
	 * This constant is only used when representing the result of a game; it is
	 * never used to represent a piece color, even though it fits the bitmask to
	 * test a piece's color.
	 */
	public static final int DRAW = 24;
	
	/**
	 * Bitmask to use when testing a piece's color.
	 */
	public static final int COLOR_MASK = 0b11000;
	
	/**
	 * Bitmask to use when testing a piece's kind.
	 */
	public static final int PIECE_MASK = 0b00111;
	
	// The board.
	private int[][] board;
	
	// Whose turn it is.
	private int colorToMove;
	
	// Each player's castling state.
	private boolean whiteCanCastleKingside, whiteCanCastleQueenside,
			blackCanCastleKingside, blackCanCastleQueenside;
	
	// The square on which an en passant capture can occur (both -1 if no en
	// passant capture is possible).
	private int enPassantRank, enPassantFile;
	
	// The 50-move rule counter.
	private int fmrCounter;
	
	// The current move number.
	private int moveNumber;
	
	// The result of the game (which color has won, if any).
	private int result;
	
	// The piece to promote the next promoted pawn to.
	private int promotionPiece;
	
	// -------- Various helper methods --------
	
	/**
	 * Throws a new {@code IllegalArgumentException} with the specified message.
	 * 
	 * @param message The exception message.
	 */
	private static void illegalArg(String message)
	{
		throw new IllegalArgumentException(message);
	}
	
	/**
	 * Returns {@code true} if the passed-in rank and file indices form a valid
	 * square on the board and {@code false} otherwise.
	 * 
	 * @param rank The rank index of the square to test.
	 * @param file The file index of the square to test.
	 * @return {@code true} if and only if the passed-in square is valid.
	 */
	private static boolean isValidSquare(int rank, int file)
	{
		return rank >= 0 && rank <= 7 && file >= 0 && file <= 7;
	}
	
	/**
	 * Returns an array of two ints, one for the rank index and one for the file
	 * index, of a square in algebraic notation.
	 * 
	 * @param algebraic The algebraic notation for a square.
	 * @return An array of two ints as described above.
	 */
	private static int[] algebraicToIndices(String algebraic)
	{
		String message = "Invalid algebraic notation '" + algebraic + "'";
		if (algebraic.length() != 2) {
			Game.illegalArg(message);
		}
		int[] ret = new int[2];
		ret[0] = algebraic.charAt(0) - 'a';
		ret[1] = algebraic.charAt(1) - '0';
		if (!Game.isValidSquare(ret[0], ret[1])) {
			Game.illegalArg(message);
		}
		return ret;
	}
	
	/**
	 * Returns the passed-in square as represented as a pair of indices in
	 * algebraic notation.
	 * 
	 * @param rank The rank index.
	 * @param file The file index.
	 * @return The algebraic notation for the square.
	 */
	private static String indicesToAlgebraic(int rank, int file)
	{
		if (!Game.isValidSquare(rank, file)) {
			Game.illegalArg("Invalid indices (" + rank + ", " + file + ")");
		}
		StringBuilder ret = new StringBuilder();
		ret.append((char)(rank + 'a'));
		ret.append(file);
		return ret.toString();
	}
	
	/**
	 * Returns the color of the passed-in piece.
	 * 
	 * @param piece The piece to test the color of.
	 * @return The color of that piece.
	 */
	private static int getColor(int piece)
	{
		return piece & Game.COLOR_MASK;
	}
	
	/**
	 * Returns the type of the passed-in piece.
	 * 
	 * @param piece The piece to test the type of.
	 * @return The type of that piece.
	 */
	private static int getPieceType(int piece)
	{
		return piece & Game.PIECE_MASK;
	}
	
	/**
	 * Converts the passed-in piece, represented as an integer, to a FEN
	 * character.
	 * 
	 * @param piece The piece, represented as an integer.
	 * @return The FEN character for that piece.
	 */
	private static char pieceIntToChar(int piece)
	{
		char pieceChar = 'P';
		String message = "Invalid piece number " + piece;
		switch (Game.getPieceType(piece)) {
		case Game.PAWN:
			pieceChar = 'P';
			break;
		case Game.KNIGHT:
			pieceChar = 'N';
			break;
		case Game.BISHOP:
			pieceChar = 'B';
			break;
		case Game.ROOK:
			pieceChar = 'R';
			break;
		case Game.QUEEN:
			pieceChar = 'Q';
			break;
		case Game.KING:
			pieceChar = 'K';
			break;
		default:
			Game.illegalArg(message);
		}
		switch (Game.getColor(piece)) {
		case Game.WHITE:
			pieceChar = Character.toUpperCase(pieceChar);
			break;
		case Game.BLACK:
			pieceChar = Character.toLowerCase(pieceChar);
			break;
		default:
			Game.illegalArg(message);
		}
		return pieceChar;
	}
	
	/**
	 * Returns the passed-in move as represented as an array of two arrays of
	 * indices, where the first array represents the starting square of the move
	 * and the second represents the destination square.
	 * 
	 * @param move The move.
	 * @return An array of arrays of indices as described above.
	 */
	private static int[][] moveToIndices(String move)
	{
		if (move.length() != 4) {
			Game.illegalArg("Invalid move '" + move + "'");
		}
		int[][] ret = new int[2][2];
		ret[0] = Game.algebraicToIndices(move.substring(0, 2));
		ret[1] = Game.algebraicToIndices(move.substring(2));
		return ret;
	}
	
	// -------- Constructors --------
	
	/**
	 * Creates a new Game with the initial board setup.
	 */
	public Game()
	{
		this.board = new int[8][8];
		this.colorToMove = Game.WHITE;
		this.whiteCanCastleKingside = true;
		this.whiteCanCastleQueenside = true;
		this.blackCanCastleKingside = true;
		this.blackCanCastleQueenside = true;
		this.enPassantRank = -1;
		this.enPassantFile = -1;
		this.fmrCounter = 0;
		this.moveNumber = 1;
		this.result = Game.NONE;
		this.promotionPiece = Game.QUEEN;
		this.setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	}
	
	/**
	 * Copy constructor.
	 * <p>
	 * Creates a new Game with exactly the same state as the passed-in Game.
	 * 
	 * @param g The Game to copy the state of.
	 */
	public Game(Game g)
	{
		this.board = g.getBoard();
		this.colorToMove = g.colorToMove;
		this.whiteCanCastleKingside = g.whiteCanCastleKingside;
		this.whiteCanCastleQueenside = g.whiteCanCastleQueenside;
		this.blackCanCastleKingside = g.blackCanCastleKingside;
		this.blackCanCastleQueenside = g.blackCanCastleQueenside;
		this.enPassantRank = g.enPassantRank;
		this.enPassantFile = g.enPassantFile;
		this.fmrCounter = g.fmrCounter;
		this.moveNumber = g.moveNumber;
		this.result = g.result;
		this.promotionPiece = g.promotionPiece;
	}
	
	// -------- FEN operations --------
	
	/**
	 * Returns the game state as a FEN (Forsyth-Edwards Notation) string.
	 * 
	 * @return The FEN string for the current game state.
	 */
	public String getFen()
	{
		StringBuilder fen = new StringBuilder();
		
		// Component 1: Board state
		for (int r = 7; r >= 0; r--) {
			int blankSquares = 0;
			for (int c = 0; c < 8; c++) {
				int piece = this.board[r][c];
				if (piece == Game.NONE) {
					blankSquares++;
				} else {
					if (blankSquares > 0) {
						fen.append(blankSquares);
						blankSquares = 0;
					}
					fen.append(Game.pieceIntToChar(piece));
				}
			}
			fen.append(blankSquares);
			if (r > 0) {
				fen.append("/");
			}
		}
		fen.append(" ");
		
		// Component 2: Current turn
		fen.append(this.colorToMove == Game.WHITE ? "w " : "b ");
		
		// Component 3: Castling legality
		StringBuilder castlingLegality = new StringBuilder();
		if (this.whiteCanCastleKingside) {
			castlingLegality.append('K');
		}
		if (this.whiteCanCastleQueenside) {
			castlingLegality.append('Q');
		}
		if (this.blackCanCastleKingside) {
			castlingLegality.append('k');
		}
		if (this.blackCanCastleQueenside) {
			castlingLegality.append('q');
		}
		String legalityStr = castlingLegality.toString();
		fen.append(legalityStr.isEmpty() ? "-" : legalityStr);
		fen.append(" ");
		
		// Component 4: En passant square
		if (this.enPassantRank == -1) {
			fen.append("-");
		} else {
			fen.append(Game.indicesToAlgebraic(this.enPassantRank,
					this.enPassantFile));
		}
		fen.append(" ");
		
		// Component 5: 50-move rule counter
		fen.append(this.fmrCounter);
		fen.append(" ");
		
		// Component 6: Move number
		fen.append(this.moveNumber);
		
		return fen.toString();
	}
	
	/**
	 * Loads the game state represented by the passed-in FEN string.
	 * 
	 * @param fen The FEN string to load.
	 */
	public void setFen(String fen)
	{
		// Split FEN string into components
		String[] components = fen.split(" ");
		if (components.length != 6) {
			Game.illegalArg("FEN must have exactly 6 components (detected " +
					components.length + ")");
		}
		
		// Component 1: Board state
		String[] ranks = components[0].split("/");
		if (ranks.length != 8) {
			Game.illegalArg("Board must have exactly 8 ranks (detected " +
					ranks.length + ")");
		}
		for (int r = 0; r < 8; r++) {
			// Iterate through each rank, inserting squares as specified
			String rkStr = ranks[7 - r];
			List<Integer> rk = new ArrayList<Integer>(8);
			String msgPrefix = "Rank " + (r + 1) + " is invalid: ";
			for (int ci = 0; ci < rkStr.length(); ci++) {
				char ch = rkStr.charAt(ci);
				String msgSuffix = "Invalid character '" + ch + "'";
				if (Character.isDigit(ch)) {
					// Insert blank squares equal to the digit
					int numSquares = ch - '0';
					for (int si = 0; si < numSquares; si++) {
						rk.add(Game.NONE);
					}
				} else if (Character.isAlphabetic(ch)) {
					// Insert a piece in the next square
					int color =
							Character.isUpperCase(ch) ? Game.WHITE : Game.BLACK;
					int pieceType = Game.NONE;
					switch (Character.toLowerCase(ch)) {
					case 'p':
						pieceType = Game.PAWN;
						break;
					case 'n':
						pieceType = Game.KNIGHT;
						break;
					case 'b':
						pieceType = Game.BISHOP;
						break;
					case 'r':
						pieceType = Game.ROOK;
						break;
					case 'q':
						pieceType = Game.QUEEN;
						break;
					case 'k':
						pieceType = Game.KING;
						break;
					default:
						Game.illegalArg(msgPrefix + msgSuffix);
					}
					rk.add(color | pieceType);
				} else {
					// Illegal character in board string
					Game.illegalArg(msgPrefix + msgSuffix);
				}
			}
			if (rk.size() == 8) {
				// Copy rank to board
				for (int c = 0; c < 8; c++) {
					this.board[r][c] = rk.get(c);
				}
			} else {
				// FEN specifies wrong number of squares in rank
				Game.illegalArg(msgPrefix + "Must have exactly 8 squares (" +
						rk.size() + " detected)");
			}
		}
		
		// Component 2: Current turn
		if (components[1].length() != 1) {
			Game.illegalArg("Current turn must be exactly 1 character");
		}
		char ch = components[1].charAt(0);
		switch (ch) {
		case 'w':
			this.colorToMove = Game.WHITE;
			break;
		case 'b':
			this.colorToMove = Game.BLACK;
			break;
		default:
			Game.illegalArg("Invalid current turn character '" + ch + "'");
		}
		
		// Component 3: Castling legality
		this.whiteCanCastleKingside = components[2].contains("K");
		this.whiteCanCastleQueenside = components[2].contains("Q");
		this.blackCanCastleKingside = components[2].contains("k");
		this.blackCanCastleQueenside = components[2].contains("q");
		
		// Component 4: En passant square
		if (components[3].equals("-")) {
			// No en passant square
			this.enPassantRank = -1;
			this.enPassantFile = -1;
		} else {
			try {
				int[] indices = Game.algebraicToIndices(components[3]);
				this.enPassantRank = indices[0];
				this.enPassantFile = indices[1];
			} catch (Exception e) {
				Game.illegalArg(
						"Invalid en passant square '" + components[3] + "'");
			}
		}
		
		// Component 5: 50-move rule counter
		try {
			this.fmrCounter = Integer.parseInt(components[4]);
		} catch (NumberFormatException e) {
			Game.illegalArg(
					"Invalid 50-move rule counter '" + components[4] + "'");
		}
		
		// Component 6: Move number
		try {
			this.moveNumber = Integer.parseInt(components[5]);
		} catch (NumberFormatException e) {
			Game.illegalArg("Invalid move number '" + components[5] + "'");
		}
	}
	
	// -------- Game state getters --------
	
	/**
	 * Returns a copy of the board array.
	 * 
	 * @return A copy of the board array.
	 */
	public int[][] getBoard()
	{
		int[][] ret = new int[8][8];
		for (int i = 0; i < 8; i++) {
			System.arraycopy(this.board[i], 0, ret[i], 0, 8);
		}
		return ret;
	}
	
	/**
	 * Returns true if this game has ended; false otherwise.
	 * 
	 * @return True if this game has ended; false otherwise.
	 */
	public boolean gameEnded()
	{
		return this.result != Game.NONE;
	}
	
	/**
	 * Returns the result of this game.
	 * <p>
	 * The possible return values are as follows:
	 * <li>{@code Game.NONE}: Game has not yet ended.
	 * <li>{@code Game.WHITE}: Game has ended in a win for the white player.
	 * <li>{@code Game.BLACK}: Game has ended in a win for the black player.
	 * <li>{@code Game.DRAW}: Game has ended in a draw.
	 * 
	 * @return The result of this game, as described above.
	 */
	public int getResult()
	{
		return this.result;
	}
	
	// -------- Game state setters --------
	
	/**
	 * Sets the next promotion piece to the specified piece type.
	 * 
	 * @param piece The piece type to promote the next pawn to.
	 */
	public void setPromotionPiece(int piece)
	{
		this.promotionPiece = piece;
	}
	
	/**
	 * The player whose turn it is resigns the game, causing the game to end in
	 * a win for their opponent.
	 */
	public void resign()
	{
		this.result = this.getOpponentColor();
	}
	
	/**
	 * Ends the game in a draw.
	 */
	public void declareDraw()
	{
		this.result = Game.DRAW;
	}
	
	// -------- Checking pseudo-legal destinations --------
	
	/**
	 * Returns the color of the player whose turn it is not.
	 * 
	 * @return The color of the player whose turn it is not.
	 */
	private int getOpponentColor()
	{
		return this.colorToMove == Game.WHITE ? Game.BLACK : Game.WHITE;
	}
	
	/**
	 * Returns {@code true} if the square at the passed-in rank and file is
	 * empty; returns {@code false} otherwise.
	 * 
	 * @param rank The rank index of the square to test.
	 * @param file The file index of the square to test.
	 * @return A boolean as described above.
	 */
	private boolean isEmpty(int rank, int file)
	{
		return Game.isValidSquare(rank, file) &&
				this.board[rank][file] == Game.NONE;
	}
	
	/**
	 * Returns {@code true} if the square at the passed-in rank and file
	 * contains a piece of the active player's color; returns {@code false}
	 * otherwise.
	 * 
	 * @param rank The rank index of the square to test.
	 * @param file The file index of the square to test.
	 * @return A boolean as described above.
	 */
	private boolean isActiveColor(int rank, int file)
	{
		return Game.isValidSquare(rank, file) &&
				Game.getColor(this.board[rank][file]) == this.colorToMove;
	}
	
	/**
	 * Returns {@code true} if the square at the passed-in rank and file
	 * contains a piece of the opponent's color; returns {@code false}
	 * otherwise.
	 * 
	 * @param rank The rank index of the square to test.
	 * @param file The file index of the square to test.
	 * @return A boolean as described above.
	 */
	private boolean isOpponentColor(int rank, int file)
	{
		return Game.isValidSquare(rank, file) &&
				Game.getColor(this.board[rank][file]) ==
						this.getOpponentColor();
	}
	
	/**
	 * Returns a list containing all pseudo-legal destination squares, in
	 * algebraic notation, for the piece on the passed-in rank and file.
	 * 
	 * @param rank The rank index of the piece.
	 * @param file The file index of the piece.
	 * @return A list of squares as described above.
	 */
	public List<String> getPseudoLegalDestinations(int rank, int file)
	{
		List<String> ret = new ArrayList<String>();
		int piece = this.board[rank][file];
		if (Game.getColor(piece) != this.colorToMove) {
			// Square is blank or contains the opponent's piece; return an empty
			// list
			return ret;
		}
		switch (Game.getPieceType(piece)) {
		case Game.PAWN:
			if (this.colorToMove == Game.WHITE) {
				ret.addAll(this.getWhitePawnMovesFrom(rank, file));
			} else if (this.colorToMove == Game.BLACK) {
				ret.addAll(this.getBlackPawnMovesFrom(rank, file));
			}
			break;
		case Game.KNIGHT:
			ret.addAll(this.getKnightMovesFrom(rank, file));
			break;
		case Game.BISHOP:
			ret.addAll(this.getBishopMovesFrom(rank, file));
			break;
		case Game.ROOK:
			ret.addAll(this.getRookMovesFrom(rank, file));
			break;
		case Game.QUEEN:
			ret.addAll(this.getBishopMovesFrom(rank, file));
			ret.addAll(this.getRookMovesFrom(rank, file));
			break;
		case Game.KING:
			ret.addAll(this.getKingMovesFrom(rank, file));
			break;
		}
		return ret;
	}
	
	/**
	 * Returns a list containing the destination squares, in algebraic notation,
	 * of all pseudo-legal white pawn moves from the passed-in rank and file.
	 * 
	 * @param rank The rank index of the pawn.
	 * @param file The file index of the pawn.
	 * @return A list of squares as described above.
	 */
	private List<String> getWhitePawnMovesFrom(int rank, int file)
	{
		List<String> ret = new ArrayList<String>();
		// Check the one-space pawn move
		if (rank < 7 && this.isEmpty(rank + 1, file)) {
			ret.add(Game.indicesToAlgebraic(rank + 1, file));
			// Check the two-space pawn move
			if (rank == 1 && this.isEmpty(rank + 2, file)) {
				ret.add(Game.indicesToAlgebraic(rank + 2, file));
			}
		}
		// Check captures
		if (rank < 7) {
			if (file > 0 && this.isOpponentColor(rank + 1, file - 1)) {
				ret.add(Game.indicesToAlgebraic(rank + 1, file - 1));
			}
			if (file < 7 && this.isOpponentColor(rank + 1, file + 1)) {
				ret.add(Game.indicesToAlgebraic(rank + 1, file + 1));
			}
			// Check en passant capture
			if (rank == this.enPassantRank - 1 &&
					Math.abs(file - this.enPassantFile) == 1) {
				ret.add(Game.indicesToAlgebraic(this.enPassantRank,
						this.enPassantFile));
			}
		}
		return ret;
	}
	
	/**
	 * Returns a list containing the destination squares, in algebraic notation,
	 * of all pseudo-legal black pawn moves from the passed-in rank and file.
	 * 
	 * @param rank The rank index of the pawn.
	 * @param file The file index of the pawn.
	 * @return A list of squares as described above.
	 */
	private List<String> getBlackPawnMovesFrom(int rank, int file)
	{
		List<String> ret = new ArrayList<String>();
		// Check the one-space pawn move
		if (rank > 0 && this.isEmpty(rank - 1, file)) {
			ret.add(Game.indicesToAlgebraic(rank - 1, file));
			// Check the two-space pawn move
			if (rank == 6 && this.isEmpty(rank - 2, file)) {
				ret.add(Game.indicesToAlgebraic(rank - 2, file));
			}
		}
		// Check captures
		if (rank > 0) {
			if (file > 0 && this.isOpponentColor(rank - 1, file - 1)) {
				ret.add(Game.indicesToAlgebraic(rank - 1, file - 1));
			}
			if (file < 7 && this.isOpponentColor(rank - 1, file + 1)) {
				ret.add(Game.indicesToAlgebraic(rank - 1, file + 1));
			}
			// Check en passant capture
			if (rank == this.enPassantRank + 1 &&
					Math.abs(file - this.enPassantFile) == 1) {
				ret.add(Game.indicesToAlgebraic(this.enPassantRank,
						this.enPassantFile));
			}
		}
		return ret;
	}
	
	/**
	 * Returns a list containing the destination squares, in algebraic notation,
	 * of all pseudo-legal knight moves from the passed-in rank and file.
	 * 
	 * @param rank The rank index of the knight.
	 * @param file The file index of the knight.
	 * @return A list of squares as described above.
	 */
	private List<String> getKnightMovesFrom(int rank, int file)
	{
		List<String> ret = new ArrayList<String>();
		int[] rankOffsets = {2, 1, -1, -2, -2, -1, 1, 2};
		int[] fileOffsets = {1, 2, 2, 1, -1, -2, -2, -1};
		for (int i = 0; i < 8; i++) {
			int newRank = rank + rankOffsets[i];
			int newFile = file + fileOffsets[i];
			if (!this.isActiveColor(newRank, newFile)) {
				ret.add(Game.indicesToAlgebraic(newRank, newFile));
			}
		}
		return ret;
	}
	
	/**
	 * Returns a list containing the destination squares, in algebraic notation,
	 * of all pseudo-legal bishop moves from the passed-in rank and file.
	 * 
	 * @param rank The rank index of the bishop.
	 * @param file The file index of the bishop.
	 * @return A list of squares as described above.
	 */
	private List<String> getBishopMovesFrom(int rank, int file)
	{
		List<String> ret = new ArrayList<String>();
		int[] rankOffsets = {1, -1, -1, 1};
		int[] fileOffsets = {1, 1, -1, -1};
		for (int i = 0; i < 4; i++) {
			int newRank = rank;
			int newFile = file;
			while (true) {
				newRank += rankOffsets[i];
				newFile += fileOffsets[i];
				if (this.isEmpty(newRank, newFile)) {
					ret.add(Game.indicesToAlgebraic(newRank, newFile));
					continue;
				}
				if (this.isOpponentColor(newRank, newFile)) {
					ret.add(Game.indicesToAlgebraic(newRank, newFile));
					break;
				}
				break;
			}
		}
		return ret;
	}
	
	/**
	 * Returns a list containing the destination squares, in algebraic notation,
	 * of all pseudo-legal rook moves from the passed-in rank and file.
	 * 
	 * @param rank The rank index of the rook.
	 * @param file The file index of the rook.
	 * @return A list of squares as described above.
	 */
	private List<String> getRookMovesFrom(int rank, int file)
	{
		List<String> ret = new ArrayList<String>();
		int[] rankOffsets = {1, 0, -1, 0};
		int[] fileOffsets = {0, 1, 0, -1};
		for (int i = 0; i < 4; i++) {
			int newRank = rank;
			int newFile = file;
			while (true) {
				newRank += rankOffsets[i];
				newFile += fileOffsets[i];
				if (this.isEmpty(newRank, newFile)) {
					ret.add(Game.indicesToAlgebraic(newRank, newFile));
					continue;
				}
				if (this.isOpponentColor(newRank, newFile)) {
					ret.add(Game.indicesToAlgebraic(newRank, newFile));
					break;
				}
				break;
			}
		}
		return ret;
	}
	
	/**
	 * Returns a list containing the destination squares, in algebraic notation,
	 * of all pseudo-legal king moves from the passed-in rank and file.
	 * 
	 * @param rank The rank index of the king.
	 * @param file The file index of the king.
	 * @return A list of squares as described above.
	 */
	private List<String> getKingMovesFrom(int rank, int file)
	{
		List<String> ret = new ArrayList<String>();
		int[] rankOffsets = {1, 1, 0, -1, -1, -1, 0, 1};
		int[] fileOffsets = {0, 1, 1, 1, 0, -1, -1, -1};
		for (int i = 0; i < 8; i++) {
			int newRank = rank + rankOffsets[i];
			int newFile = file + fileOffsets[i];
			if (!this.isActiveColor(newRank, newFile)) {
				ret.add(Game.indicesToAlgebraic(newRank, newFile));
			}
		}
		// Check castling
		if (rank == 0 && file == 4) {
			if (this.whiteCanCastleKingside && this.board[0][5] == Game.NONE &&
					this.board[0][6] == Game.NONE) {
				ret.add(Game.indicesToAlgebraic(0, 6));
			}
			if (this.whiteCanCastleQueenside && this.board[0][1] == Game.NONE &&
					this.board[0][2] == Game.NONE &&
					this.board[0][3] == Game.NONE) {
				ret.add(Game.indicesToAlgebraic(0, 2));
			}
		}
		if (rank == 7 && file == 4) {
			if (this.blackCanCastleKingside && this.board[7][5] == Game.NONE &&
					this.board[7][6] == Game.NONE) {
				ret.add(Game.indicesToAlgebraic(7, 6));
			}
			if (this.blackCanCastleQueenside && this.board[7][1] == Game.NONE &&
					this.board[7][2] == Game.NONE &&
					this.board[7][3] == Game.NONE) {
				ret.add(Game.indicesToAlgebraic(7, 2));
			}
		}
		return ret;
	}
	
	/**
	 * Returns a list containing all pseudo-legal moves (legal moves ignoring
	 * check and checkmate).
	 * <p>
	 * Each move is a string in the format "a1b2", where a1 is the starting
	 * square of the move and b2 is the destination square of the move, with
	 * both squares in algebraic notation.
	 * 
	 * @return A list containing all pseudo-legal moves.
	 */
	public List<String> getAllPseudoLegalMoves()
	{
		List<String> ret = new ArrayList<String>();
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				String square = Game.indicesToAlgebraic(rank, file);
				List<String> l = this.getPseudoLegalDestinations(rank, file);
				for (String s: l) {
					ret.add(square + s);
				}
			}
		}
		return ret;
	}
	
	/**
	 * Returns true if the player to move is in check; false otherwise.
	 * 
	 * @return True if the player to move is in check; false otherwise.
	 */
	public boolean isInCheck()
	{
		Game g = new Game(this);
		g.colorToMove = this.getOpponentColor();
		// See if the opponent can capture the king
		List<String> moves = g.getAllPseudoLegalMoves();
		for (String m: moves) {
			int[] dest = Game.moveToIndices(m)[1];
			if (this.board[dest[0]][dest[1]] ==
					(this.colorToMove | Game.KING)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns a list containing all legal moves.
	 * <p>
	 * Each move is a string in the format "a1b2", where a1 is the starting
	 * square of the move and b2 is the destination square of the move, with
	 * both squares in algebraic notation.
	 * 
	 * @return A list containing all legal moves.
	 */
	public List<String> getAllLegalMoves()
	{
		List<String> allMoves = this.getAllPseudoLegalMoves();
		List<String> ret = new ArrayList<String>(allMoves);
		// Remove moves that are illegal because of check(mate)
		for (String m: allMoves) {
			Game g = new Game(this);
			g.makeMove(m);
			g.colorToMove = this.colorToMove;
			if (g.isInCheck()) {
				ret.remove(m);
			}
		}
		allMoves.clear();
		allMoves.addAll(ret);
		Game g = new Game(this);
		g.colorToMove = this.getOpponentColor();
		// Remove castling moves made illegal by check(mate)
		if (this.whiteCanCastleKingside && allMoves.contains("e1g1")) {
			// See if the opponent can capture on e1 or f1
			List<String> moves = g.getAllPseudoLegalMoves();
			for (String m: moves) {
				int[] dest = Game.moveToIndices(m)[1];
				if (dest[0] == 0 && (dest[1] == 4 || dest[1] == 5)) {
					ret.remove("e1g1");
					break;
				}
			}
		}
		if (this.whiteCanCastleQueenside && allMoves.contains("e1c1")) {
			// See if the opponent can capture on e1 or d1
			List<String> moves = g.getAllPseudoLegalMoves();
			for (String m: moves) {
				int[] dest = Game.moveToIndices(m)[1];
				if (dest[0] == 0 && (dest[1] == 4 || dest[1] == 3)) {
					ret.remove("e1c1");
					break;
				}
			}
		}
		if (this.blackCanCastleKingside && allMoves.contains("e8g8")) {
			// See if the opponent can capture on e8 or f8
			List<String> moves = g.getAllPseudoLegalMoves();
			for (String m: moves) {
				int[] dest = Game.moveToIndices(m)[1];
				if (dest[0] == 7 && (dest[1] == 4 || dest[1] == 5)) {
					ret.remove("e8g8");
					break;
				}
			}
		}
		if (this.blackCanCastleQueenside && allMoves.contains("e8c8")) {
			// See if the opponent can capture on e8 or d8
			List<String> moves = g.getAllPseudoLegalMoves();
			for (String m: moves) {
				int[] dest = Game.moveToIndices(m)[1];
				if (dest[0] == 7 && (dest[1] == 4 || dest[1] == 3)) {
					ret.remove("e8c8");
					break;
				}
			}
		}
		return ret;
	}
	
	// -------- Making moves --------
	
	/**
	 * Makes the passed-in move, in the form of the starting square followed by
	 * the destination square, both in algebraic notation.
	 * 
	 * @param move The move to make.
	 */
	public void makeMove(String move)
	{
		// Change position of piece
		int[][] indices = Game.moveToIndices(move);
		int[] start = indices[0];
		int[] dest = indices[1];
		int piece = this.board[start[0]][start[1]];
		boolean captureMade = this.board[dest[0]][dest[1]] != Game.NONE;
		this.board[dest[0]][dest[1]] = piece;
		this.board[start[0]][start[1]] = Game.NONE;
		
		// Handle castling
		if (start[0] == 0 && start[1] == 4) {
			// Starting square of white king
			this.whiteCanCastleKingside = false;
			this.whiteCanCastleQueenside = false;
			if (dest[1] == 2) {
				// Castling queenside
				this.board[0][3] = this.board[0][0];
				this.board[0][0] = Game.NONE;
			}
			if (dest[1] == 6) {
				// Castling kingside
				this.board[0][5] = this.board[0][7];
				this.board[0][7] = Game.NONE;
			}
		}
		if (start[0] == 0 && start[1] == 0) {
			// Starting square of white queenside rook
			this.whiteCanCastleQueenside = false;
		}
		if (start[0] == 0 && start[1] == 7) {
			// Starting square of white kingside rook
			this.whiteCanCastleKingside = false;
		}
		if (start[0] == 7 && start[1] == 4) {
			// Starting square of black king
			this.blackCanCastleKingside = false;
			this.blackCanCastleQueenside = false;
			if (dest[1] == 2) {
				// Castling queenside
				this.board[7][3] = this.board[7][0];
				this.board[7][0] = Game.NONE;
			}
			if (dest[1] == 6) {
				// Castling kingside
				this.board[7][5] = this.board[7][7];
				this.board[7][7] = Game.NONE;
			}
		}
		if (start[0] == 7 && start[1] == 0) {
			// Starting square of black queenside rook
			this.blackCanCastleQueenside = false;
		}
		if (start[0] == 7 && start[1] == 7) {
			// Starting square of black kingside rook
			this.blackCanCastleKingside = false;
		}
		
		// Handle captures with regards to castling
		if (dest[0] == 0 && dest[1] == 0) {
			// Starting square of white queenside rook
			this.whiteCanCastleQueenside = false;
		}
		if (dest[0] == 0 && dest[1] == 7) {
			// Starting square of white kingside rook
			this.whiteCanCastleKingside = false;
		}
		if (dest[0] == 7 && dest[1] == 0) {
			// Starting square of black queenside rook
			this.blackCanCastleQueenside = false;
		}
		if (dest[0] == 7 && dest[1] == 7) {
			// Starting square of black kingside rook
			this.blackCanCastleKingside = false;
		}
		
		// Handle en passant
		if (Game.getPieceType(piece) == Game.PAWN &&
				Math.abs(dest[0] - start[0]) == 2) {
			// Two-space pawn move; create en passant square
			this.enPassantRank = (start[0] + dest[0]) / 2;
			this.enPassantFile = start[1];
		} else {
			if (Game.getPieceType(piece) == Game.PAWN &&
					dest[0] == this.enPassantRank &&
					dest[1] == this.enPassantFile) {
				// Make en passant capture
				if (this.colorToMove == Game.WHITE) {
					this.board[dest[0] - 1][dest[1]] = Game.NONE;
				} else if (this.colorToMove == Game.BLACK) {
					this.board[dest[0] + 1][dest[1]] = Game.NONE;
				}
				captureMade = true;
			}
			// Remove en passant square
			this.enPassantRank = -1;
			this.enPassantFile = -1;
		}
		
		// Handle promotion
		if (Game.getPieceType(piece) == Game.PAWN &&
				dest[0] == (this.colorToMove == Game.WHITE ? 7 : 0)) {
			this.board[dest[0]][dest[1]] =
					this.colorToMove | this.promotionPiece;
		}
		
		// Increment counters
		if (this.colorToMove == Game.BLACK) {
			this.moveNumber++;
		}
		if (Game.getPieceType(piece) == Game.PAWN || captureMade) {
			this.fmrCounter = 0;
		} else {
			this.fmrCounter++;
		}
		
		// Switch color to move
		if (this.colorToMove == Game.WHITE) {
			this.colorToMove = Game.BLACK;
		} else if (this.colorToMove == Game.BLACK) {
			this.colorToMove = Game.WHITE;
		}
		
		// If no legal moves, end game
		List<String> moves = this.getAllLegalMoves();
		if (moves.isEmpty()) {
			if (this.isInCheck()) {
				// Checkmate, end in a loss for next player
				this.resign();
			} else {
				// Stalemate, end in a draw
				this.declareDraw();
			}
		}
	}
	
	// -------- toString --------
	
	/**
	 * Returns a String representation of this game.
	 * 
	 * @return A String representation of this game.
	 */
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		String rowSeparator = "+-+-+-+-+-+-+-+-+\n";
		for (int i = 0; i < 8; i++) {
			ret.append(rowSeparator);
			for (int j = 0; j < 8; j++) {
				ret.append('|');
				ret.append(Game.pieceIntToChar(this.board[i][j]));
			}
			ret.append("|\n");
		}
		ret.append(rowSeparator);
		ret.append(this.colorToMove == Game.WHITE ? "White" : "Black");
		ret.append(" to move");
		return ret.toString();
	}
}
