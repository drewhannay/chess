package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * AlgebraicConverter.java Class to convert Move objects to Algebraic Chess
 * Notation and vice versa.
 * @author Drew Hannay & Alisa Maas CSCI 335, Wheaton College, Spring 2011 Phase
 *         2 April 7, 2011
 */
public final class AlgebraicConverter {

	/**
	 * Static final String[] used for converting integers to letters for ACN
	 */
	private static final String columns = "%abcdefgh";

	/**
	 * HashMap to move from the char representation of a Class to the actual
	 * Class
	 */
	private static HashMap<Character, String> map = new HashMap<Character, String>();

	/**
	 * Static initializer to build the HashMap
	 */
	static {
		map.put('B', "Bishop");
		map.put('K', "King");
		map.put('N', "Knight");
		map.put('Q', "Queen");
		map.put('R', "Rook");
	}

	/**
	 * Convert a single string of ACN to a Move object. Use the Pattern Matcher
	 * to decide what properties to give the Move and then return the created
	 * Move object
	 * @param s The String of ACN to convert to a Move.
	 * @param board The Board on which this Move is to be played
	 * @return The completed Move object
	 * @throws Exception Creating a new move requires an exception handler
	 */
	private static Move algToMove(String s, Board board) throws Exception {
		Move move = null;
		Matcher result = null;
		String pieceKlass = null;
		String promo = null;
		int origRow = 0;
		int origCol = 0;

		Square orig = null;
		Square dest = null;

		result = getPattern().matcher(s);
		if (result.find()) {
			if (result.group(1).equals("O-O-O")
					|| result.group(1).equals("0-0-0")) {
				move = new Move(board, Move.CASTLE_QUEEN_SIDE);
			} else if (result.group(1).equals("O-O")
					|| result.group(1).equals("0-0")) {
				move = new Move(board, Move.CASTLE_KING_SIDE);
			} else {
				if (result.group(2) != null) {
					pieceKlass = map.get(result.group(2).charAt(0)); 
				}

				if (result.group(3) != null) {
					origCol = columns
					.indexOf((result.group(3).charAt(0) + "")
							.toLowerCase());
				}

				if (result.group(4) != null) {
					origRow = Integer.parseInt(result.group(4).charAt(0)
							+ "");
				}

				dest = board.getSquare(Integer.parseInt(result.group(7)
						.charAt(0)
						+ ""), columns
						.indexOf((result.group(6).charAt(0) + "")
								.toLowerCase()));

				if (origCol < 1 || origRow < 1) {
					if(pieceKlass == null){
						pieceKlass = "Pawn";
					}
					orig = board.getOriginSquare(pieceKlass, origCol,
							origRow, dest);
				} else {
					orig = board.getSquare(origRow, origCol);
				}

				if (result.group(8) != null) { // promotion
					if (result.group(8).contains("=")
							|| result.group(8).contains("(")) {
						promo = map.get(result.group(8).charAt(1)); // 0 is
						// '='
						// or
						// '('
					} else {
						promo = map.get(result.group(8).charAt(0));
					}
				}
				if (promo == null) {
					move = new Move(board, orig, dest);
				} else {
					move = new Move(board, orig, dest, promo);
				}
			}
		}
		return move;
	}

	/**
	 * Convert a File of Algebraic Chess Notation to a Game object Read in the
	 * file, move by move, and convert each String to a Move object and add it
	 * to the history of the Game. Make sure as you're moving through the file
	 * that you haven't reached the end and gotten null Moves.
	 * @param g The Game in which to play the Moves
	 * @param f The File from which to read in ACN
	 * @return The Game object with it's history added
	 * @throws Exception when dealing with files, handle exceptions.
	 */
	public static Game convert(Game g, File f) throws Exception{
		Game game = g;
		Board board = game.getBoards()[0];// Since this is Classic chess, it'll
		// always be Boards[0]
		try {
			StringTokenizer st;
			Scanner in = new Scanner(f);
			Move temp = null;
			while (in.hasNext()) {
				st = new StringTokenizer(in.nextLine());
				st.nextToken();// Move past the turn number...we don't care
				// about that
				String whiteMove = st.nextToken();
				if (!st.hasMoreTokens()) {
					break;
				}
				String blackMove = st.nextToken();
				temp = algToMove(whiteMove, board);
				if (temp == null) {
					break;
				}
				g.playMove(temp);// Play the Move on the Board so it'll be added
				// the history

				temp = algToMove(blackMove, board);
				if (temp == null) {
					break;
				}
				g.playMove(temp);
			}
			in.close();
		} catch (Exception e) {
			return null;
		}
		return game;
	}

	/** q
	 * Convert a List of Moves to a text file of Algebraic Chess Notation Open
	 * the file to write to, then iterate through the list of Moves, printing
	 * each one in the proper format.
	 * @param moves The List of Moves to convert to ACN
	 * @param pathName The path to where to save the output File
	 */
	public static void convert(List<Move> moves, String pathName) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(
					pathName)));
			String toWrite = "";
			for (int i = 0, j = 1; i < moves.size(); i++) {
				String turn = moves.get(i).toString();
				if (moves.get(i).result != null) {
					turn = moves.get(i)
					+ (i % 2 == 0 ? (" " + moves.get(i).result) : ("\n"
							+ (j + 1) + " " + moves.get(i).result));
				}
				if (i % 2 == 1 || moves.get(i).result != null) {
					out.write(j + " " + toWrite + " " + turn + '\n');
					toWrite = "";
					j++;
				} else {
					toWrite += turn;
				}

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a Pattern object to match against the files read in Put together a
	 * String using regular expressions and then use Pattern.compile(pat) to
	 * return the Pattern
	 * @return The Pattern against which the Strings will be matched
	 */
	public static Pattern getPattern() {
		String pat = "";
		pat += "([O0]-[O0]-[O0]|[O0]-[O0]";// Check for castling (Group 1)
		pat += "|^([KNQRB])?";// Check for the type of piece that's moving, null
		// if pawn
		pat += "([A-Ha-h])?";// Check for the origin column of the moving piece
		pat += "([1-8])?";// Check for the origin row of the moving piece
		pat += "([x:])?";// Check if the move is a capture
		pat += "([A-Ha-h])([1-8])";// Get the destination column and row.
		pat += "(=[NBRQ]?|\\([NBRQ]?\\)|[NBRQ])?)";// Check for several
		// different styles of piece
		// promotion
		pat += "(e.p)?";// Check for enpassant (might not be marked)
		pat += "(\\+)?";// Check for check
		pat += "(\\+)?";// Check for double check
		pat += "(#)?";// Check if the game is over
		// ([O0]-[O0]-[O0]|[O0]-[O0]|^([KNQRB])?([A-Ha-h])?([1-8])?([x:])?([A-Ha-h])([1-8])(=[NBRQ]?|\([NBRQ]?\)|[NBRQ])?)(e.p)?(\+)?(\+)?(#)?
		// Yay for regular expressions!
		return Pattern.compile(pat);
	}

}
