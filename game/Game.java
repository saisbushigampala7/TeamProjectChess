package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a game of chess.
 * <p>
 * This class is currently incomplete and being worked on.
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
	 * Constant representing a white piece.
	 * <p>
	 * This constant is added to one of the piece constants to represent a
	 * particular kind of white piece. For example, a white bishop would be
	 * represented by {@code Game.WHITE + Game.BISHOP}.
	 */
	public static final int WHITE = 8;
	
	/**
	 * Constant representing a black piece.
	 * <p>
	 * This constant is added to one of the piece constants to represent a
	 * particular kind of black piece. For example, a black bishop would be
	 * represented by {@code Game.BLACK + Game.BISHOP}.
	 */
	public static final int BLACK = 16;
	
	/**
	 * Bitmask to use when testing a square's color.
	 */
	public static final int COLOR_MASK = 0b11000;
	
	/**
	 * Bitmask to use when testing a square's kind of piece.
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
	
	// -------- Constructor --------
	
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
		this.setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
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
		fen.append(" ");
		
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
			ret.addAll(this.getPawnMovesFrom(rank, file));
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
	 * of all pseudo-legal pawn moves from the passed-in rank and file.
	 * 
	 * @param rank The rank index of the pawn.
	 * @param file The file index of the pawn.
	 * @return A list of squares as described above.
	 */
	private List<String> getPawnMovesFrom(int rank, int file)
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
		// TODO: Check castling
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
		List<String> ret = this.getAllPseudoLegalMoves();
		// TODO: Remove moves that are illegal because of check(mate)
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
		// TODO: Complete this
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
