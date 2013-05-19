package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import rules.Rules;
import utility.FileUtility;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Builder.java Builder class to create Game Types
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas CSCI 335, Wheaton College,
 * Spring 2011 Phase 2 April 7, 2011
 */
public class Builder implements Serializable
{
	/**
	 * Constructor
	 * 
	 * @param name The name of this Game type
	 */
	public Builder(String name)
	{
		mName = name;
		mWhiteTeam = Lists.newArrayList();
		mBlackTeam = Lists.newArrayList();
	}

	/**
	 * Constructor
	 * 
	 * @param name The name of this Game type
	 * @param boards The Board[] for this Game type
	 * @param whiteTeam The List of white pieces
	 * @param blackTeam The List of black pieces
	 * @param blackRules The rules for the black team
	 * @param whiteRules The rules for the white team
	 */
	public Builder(String name, Board[] boards, List<Piece> whiteTeam, List<Piece> blackTeam, Rules whiteRules, Rules blackRules)
	{
		mName = name;
		mBoards = boards;
		mWhiteTeam = whiteTeam;
		mBlackTeam = blackTeam;
		mWhiteRules = whiteRules;
		mBlackRules = blackRules;
	}

	/**
	 * Build the classic game of chess for the user.
	 */
	private static void buildClassic()
	{
		Builder classic = new Builder("Classic");// Name is Classic chess
		classic.setBoards(new Board[] { new Board(8, 8, false) });
		Board b = classic.mBoards[0];
		for (int i = 1; i < 9; i++)
		{
			classic.mWhiteTeam.add(createPawn(false, b.getSquare(2, i), b));
			classic.mBlackTeam.add(createPawn(true, b.getSquare(7, i), b));
		}
		classic.mWhiteTeam.add(createRook(false, b.getSquare(1, 1), b));
		classic.mWhiteTeam.add(createKnight(false, b.getSquare(1, 2), b));
		classic.mWhiteTeam.add(createBishop(false, b.getSquare(1, 3), b));
		classic.mWhiteTeam.add(createQueen(false, b.getSquare(1, 4), b));
		classic.mWhiteTeam.add(createKing(false, b.getSquare(1, 5), b));
		classic.mWhiteTeam.add(createBishop(false, b.getSquare(1, 6), b));
		classic.mWhiteTeam.add(createKnight(false, b.getSquare(1, 7), b));
		classic.mWhiteTeam.add(createRook(false, b.getSquare(1, 8), b));

		classic.mBlackTeam.add(createRook(true, b.getSquare(8, 1), b));
		classic.mBlackTeam.add(createKnight(true, b.getSquare(8, 2), b));
		classic.mBlackTeam.add(createBishop(true, b.getSquare(8, 3), b));
		classic.mBlackTeam.add(createQueen(true, b.getSquare(8, 4), b));
		classic.mBlackTeam.add(createKing(true, b.getSquare(8, 5), b));
		classic.mBlackTeam.add(createBishop(true, b.getSquare(8, 6), b));
		classic.mBlackTeam.add(createKnight(true, b.getSquare(8, 7), b));
		classic.mBlackTeam.add(createRook(true, b.getSquare(8, 8), b));

		classic.writeFile(new Rules(false), new Rules(true));
	}

	/**
	 * Create a piece that represents a bishop, without requiring a concrete
	 * bishop class.
	 * 
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed bishop.
	 */
	public static Piece createBishop(boolean isBlack, Square square, Board board)
	{
		Map<Character, Integer> bishopMovement = Maps.newHashMap();
		bishopMovement.put('R', -1);
		bishopMovement.put('r', -1);
		bishopMovement.put('L', -1);
		bishopMovement.put('l', -1);
		return new Piece("Bishop", isBlack, square, board, bishopMovement);
	}

	/**
	 * Create a piece that represents a king, without requiring a concrete king
	 * class.
	 * 
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed king.
	 */
	public static Piece createKing(boolean isBlack, Square square, Board board)
	{
		Map<Character, Integer> kingMovement = Maps.newHashMap();
		kingMovement.put('N', 1);
		kingMovement.put('S', 1);
		kingMovement.put('E', 1);
		kingMovement.put('W', 1);
		kingMovement.put('R', 1);
		kingMovement.put('r', 1);
		kingMovement.put('L', 1);
		kingMovement.put('l', 1);
		return new Piece("King", isBlack, square, board, kingMovement);
	}

	/**
	 * Create a piece that represents a knight, without requiring a concrete
	 * knight class.
	 * 
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed knight.
	 */
	public static Piece createKnight(boolean isBlack, Square square, Board board)
	{
		Map<Character, Integer> knightMovement = Maps.newHashMap();
		knightMovement.put('x', 1);
		knightMovement.put('y', 2);
		return new Piece("Knight", isBlack, square, board, knightMovement);
	}

	/**
	 * Create a piece that represents a pawn, without requiring a concrete pawn
	 * class.
	 * 
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed pawn.
	 */
	public static Piece createPawn(boolean isBlack, Square square, Board board)
	{
		Piece pawn = new Piece("Pawn", isBlack, square, board, null);
		List<String> promotesTo = Lists.newArrayList();
		promotesTo.add("Queen");
		promotesTo.add("Bishop");
		promotesTo.add("Knight");
		promotesTo.add("Rook");
		pawn.setPromotesTo(promotesTo);
		return pawn;
	}

	/**
	 * Create a piece that represents a queen, without requiring a concrete
	 * queen class.
	 * 
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed queen.
	 */
	public static Piece createQueen(boolean isBlack, Square square, Board board)
	{
		Map<Character, Integer> queenMovement = Maps.newHashMap();
		queenMovement.put('N', -1);
		queenMovement.put('S', -1);
		queenMovement.put('W', -1);
		queenMovement.put('E', -1);
		queenMovement.put('R', -1);
		queenMovement.put('r', -1);
		queenMovement.put('L', -1);
		queenMovement.put('l', -1);
		return new Piece("Queen", isBlack, square, board, queenMovement);
	}

	/**
	 * Create a piece that represents a rook, without requiring a concrete rook
	 * class.
	 * 
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed rook.
	 */
	public static Piece createRook(boolean isBlack, Square square, Board board)
	{
		Map<Character, Integer> rookMovement = Maps.newHashMap();
		rookMovement.put('N', -1);
		rookMovement.put('S', -1);
		rookMovement.put('W', -1);
		rookMovement.put('E', -1);
		return new Piece("Rook", isBlack, square, board, rookMovement);
	}

	/**
	 * Get the Set of names of Game types
	 * 
	 * @return A Set containing the names of Game types
	 */
	public static String[] getVariantFileArray()
	{
		String[] vars = FileUtility.getVariantsFileArray();
		for (String s : vars)
		{
			if (s.equals("Classic"))
				return vars;
		}
		buildClassic();
		return getVariantFileArray();
	}

	/**
	 * Make a new instance of a Game type
	 * 
	 * @param name The name of the Game type to return
	 * @return The created Game object
	 */
	public static Game newGame(String name)
	{
		String[] vars = FileUtility.getVariantsFileArray();
		for (String s : vars)
		{
			if (s.equals(name))
			{
				try
				{
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(FileUtility.getVariantsFile(name)));
					Builder b = (Builder) in.readObject();
					Game toReturn = new Game(name, b.mBoards, b.mWhiteRules, b.mBlackRules);
					toReturn.setWhiteTeam(b.mWhiteTeam);
					toReturn.setBlackTeam(b.mBlackTeam);
					in.close();

					return toReturn;
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Getter method for the Board[]
	 * 
	 * @return The Board[] of this type
	 */
	public Board[] getBoards()
	{
		return mBoards;
	}

	/**
	 * Setter method for the Board[]
	 * 
	 * @param boards The Board[] to set
	 */
	public void setBoards(Board[] boards)
	{
		mBoards = boards;
	}

	/**
	 * Write the gameTypes array to disk
	 * 
	 * @param blackRules Rules for the black team
	 * @param whiteRules Rules for the white team
	 */
	public void writeFile(Rules whiteRules, Rules blackRules)
	{
		try
		{
			FileOutputStream f_out = new FileOutputStream(FileUtility.getVariantsFile(mName));
			ObjectOutputStream out = new ObjectOutputStream(f_out);
			out.writeObject(new Builder(mName, mBoards, mWhiteTeam, mBlackTeam, whiteRules, blackRules));
			out.close();
			f_out.close();
		}
		catch (Exception e)
		{
		}
	}

	public void setName(String name)
	{
		mName = name;
	}

	private static final long serialVersionUID = 2099226533521671457L;

	private String mName;
	private Board[] mBoards;
	public List<Piece> mWhiteTeam;
	public List<Piece> mBlackTeam;
	private Rules mWhiteRules;
	private Rules mBlackRules;

}
