package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Board;
import models.Game;
import models.Square;

import utility.FileUtility;

import com.google.common.collect.Maps;

/**
 * AlgebraicConverter.java Class to convert Move objects to Algebraic Chess
 * Notation and vice versa.
 * 
 * @author Drew Hannay & Alisa Maas CSCI 335, Wheaton College, Spring 2011 Phase
 * 2 April 7, 2011
 */
public final class AlgebraicConverter
{

	/**
	 * Static final String[] used for converting integers to letters for ACN
	 */
	private static final String columns = "%abcdefgh"; //$NON-NLS-1$

	public static final char KNIGHT = 'N';
	public static final char BISHOP = 'B';
	public static final char KING = 'K';
	public static final char QUEEN = 'Q';
	public static final char ROOK = 'R';
	/**
	 * HashMap to move from the char representation of a Class to the actual
	 * Class
	 */
	private static Map<Character, String> map = Maps.newHashMap();

	static
	{
		map.put(BISHOP, Messages.getString("bishop")); //$NON-NLS-1$
		map.put(KING, Messages.getString("king")); //$NON-NLS-1$
		map.put(KNIGHT, Messages.getString("knight")); //$NON-NLS-1$
		map.put(QUEEN, Messages.getString("queen")); //$NON-NLS-1$
		map.put(ROOK, Messages.getString("rook")); //$NON-NLS-1$
	}

	/**
	 * Convert a single string of ACN to a Move object. Use the Pattern Matcher
	 * to decide what properties to give the Move and then return the created
	 * Move object
	 * 
	 * @param s The String of ACN to convert to a Move.
	 * @param board The Board on which this Move is to be played
	 * @return The completed Move object
	 * @throws Exception Creating a new move requires an exception handler
	 */
	private static Move algToMove(String s, Board board) throws Exception
	{
		Move move = null;
		Matcher result = null;
		String pieceKlass = null;
		String promo = null;
		int origRow = 0;
		int origCol = 0;

		Square orig = null;
		Square dest = null;

		result = getPattern().matcher(s);
		if (result.find())
		{
			if (result.group(1).equals("O-O-O") || result.group(1).equals("0-0-0")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				move = new Move(board, Move.CASTLE_QUEEN_SIDE);
			}
			else if (result.group(1).equals("O-O") || result.group(1).equals("0-0")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				move = new Move(board, Move.CASTLE_KING_SIDE);
			}
			else
			{
				if (result.group(2) != null)
				{
					pieceKlass = map.get(result.group(2).charAt(0));
				}

				if (result.group(3) != null)
				{
					origCol = columns.indexOf((result.group(3).charAt(0) + "").toLowerCase()); //$NON-NLS-1$
				}

				if (result.group(4) != null)
				{
					origRow = Integer.parseInt(result.group(4).charAt(0) + ""); //$NON-NLS-1$
				}

				dest = board.getSquare(Integer.parseInt(result.group(7).charAt(0) + ""), //$NON-NLS-1$
						columns.indexOf((result.group(6).charAt(0) + "").toLowerCase())); //$NON-NLS-1$

				if (origCol < 1 || origRow < 1)
				{
					if (pieceKlass == null)
					{
						pieceKlass = Messages.getString("pawn"); //$NON-NLS-1$
					}
					orig = board.getOriginSquare(pieceKlass, origCol, origRow, dest);
				}
				else
				{
					orig = board.getSquare(origRow, origCol);
				}

				if (result.group(8) != null)
				{ // promotion
					if (result.group(8).contains("=") || result.group(8).contains("(")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						promo = map.get(result.group(8).charAt(1)); // 0 is '='
																	// or '('
					}
					else
					{
						promo = map.get(result.group(8).charAt(0));
					}
				}
				if (promo == null)
				{
					move = new Move(board, orig, dest);
				}
				else
				{
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
	 * 
	 * @param game The Game in which to play the Moves
	 * @param f The File from which to read in ACN
	 * @return The Game object with it's history added
	 * @throws Exception when dealing with files, handle exceptions.
	 */
	public static Game convert(Game game, File f) throws Exception
	{
		// Since this is Classic chess, it'll always be Boards[0]
		Board board = game.getBoards()[0];
		game.setIsPlayback(true);

		StringTokenizer st;
		Scanner in = new Scanner(f);
		while (in.hasNext())
		{
			st = new StringTokenizer(in.nextLine());
			// Move past the turn number...we don't care about that
			st.nextToken();
			String whiteMove = st.nextToken();
			if (!st.hasMoreTokens())
			{
				break;
			}
			String blackMove = st.nextToken();
			Move temp = algToMove(whiteMove, board);
			if (temp == null)
			{
				break;
			}

			temp.execute();
			game.getHistory().add(temp);
			game.setBlackMove(true);

			temp = null;
			temp = algToMove(blackMove, board);
			if (temp == null)
			{
				break;
			}

			temp.execute();
			game.getHistory().add(temp);
			game.setBlackMove(false);
		}
		in.close();
		return game;
	}

	/**
	 * q Convert a List of Moves to a text file of Algebraic Chess Notation Open
	 * the file to write to, then iterate through the list of Moves, printing
	 * each one in the proper format.
	 * 
	 * @param moves The List of Moves to convert to ACN
	 * @param pathName The path to where to save the output File
	 */
	public static void convert(List<Move> moves, String pathName)
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(FileUtility.getCompletedGamesFile(pathName)));
			String toWrite = ""; //$NON-NLS-1$
			for (int i = 0, j = 1; i < moves.size(); i++)
			{
				String turn = moves.get(i).toString();
				if (moves.get(i).result != null)
				{
					turn = moves.get(i) + (i % 2 == 0 ? (" " + moves.get(i).result) : ("\n" + (j + 1) + " " + moves.get(i).result)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				if (i % 2 != 0 || moves.get(i).result != null)
				{
					out.write(j + " " + toWrite + " " + turn + '\n'); //$NON-NLS-1$ //$NON-NLS-2$
					toWrite = ""; //$NON-NLS-1$
					j++;
				}
				else
				{
					toWrite += turn;
				}

			}
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create a Pattern object to match against the files read in Put together a
	 * String using regular expressions and then use Pattern.compile(pat) to
	 * return the Pattern
	 * 
	 * @return The Pattern against which the Strings will be matched
	 */
	public static Pattern getPattern()
	{
		String pat = ""; //$NON-NLS-1$
		pat += "([O0]-[O0]-[O0]|[O0]-[O0]";// Check for castling (Group 1) //$NON-NLS-1$
		pat += "|^([KNQRB])?";// Check for the type of piece that's moving, null //$NON-NLS-1$
								// if pawn
		pat += "([A-Ha-h])?";// Check for the origin column of the moving piece //$NON-NLS-1$
		pat += "([1-8])?";// Check for the origin row of the moving piece //$NON-NLS-1$
		pat += "([x:])?";// Check if the move is a capture //$NON-NLS-1$
		pat += "([A-Ha-h])([1-8])";// Get the destination column and row. //$NON-NLS-1$
		pat += "(=[NBRQ]?|\\([NBRQ]?\\)|[NBRQ])?)";// Check for several //$NON-NLS-1$
		// different styles of piece
		// promotion
		pat += "(e.p)?";// Check for enpassant (might not be marked) //$NON-NLS-1$
		pat += "(\\+)?";// Check for check //$NON-NLS-1$
		pat += "(\\+)?";// Check for double check //$NON-NLS-1$
		pat += "(#)?";// Check if the game is over //$NON-NLS-1$
		// ([O0]-[O0]-[O0]|[O0]-[O0]|^([KNQRB])?([A-Ha-h])?([1-8])?([x:])?([A-Ha-h])([1-8])(=[NBRQ]?|\([NBRQ]?\)|[NBRQ])?)(e.p)?(\+)?(\+)?(#)?
		// Yay for regular expressions!
		return Pattern.compile(pat);
	}
}
