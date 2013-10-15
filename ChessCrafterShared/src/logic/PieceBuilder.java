package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import utility.FileUtility;

import com.google.common.collect.Maps;

public class PieceBuilder implements Serializable
{
	/**
	 * Static Initializer Read in the saved HashMap from a file. If that fails,
	 * build a new HashMap with Classic chess pieces and save that to a file.
	 */
	static
	{
		initPieceTypes();
	}

	/**
	 * Initialize types...
	 */
	public static void initPieceTypes()
	{
		mPieceTypes = Maps.newHashMap();
		mPieceTypes.put(Messages.getString("pawn"), new PieceBuilder(Messages.getString("pawn"))); //$NON-NLS-1$ //$NON-NLS-2$
		mPieceTypes.put(Messages.getString("rook"), new PieceBuilder(Messages.getString("rook"))); //$NON-NLS-1$ //$NON-NLS-2$
		mPieceTypes.put(Messages.getString("bishop"), new PieceBuilder(Messages.getString("bishop"))); //$NON-NLS-1$ //$NON-NLS-2$
		mPieceTypes.put(Messages.getString("knight"), new PieceBuilder(Messages.getString("knight"))); //$NON-NLS-1$ //$NON-NLS-2$
		mPieceTypes.put(Messages.getString("queen"), new PieceBuilder(Messages.getString("queen"))); //$NON-NLS-1$ //$NON-NLS-2$
		mPieceTypes.put(Messages.getString("king"), new PieceBuilder(Messages.getString("king"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Constructor. Generic Constructor, initializing movement HashMap.
	 */
	public PieceBuilder()
	{
		mMovements = Maps.newHashMap();
	}

	/**
	 * Constructor. Initializes name and movement HashMap based on given
	 * parameter.
	 * 
	 * @param name Name of desired Piece.
	 */
	public PieceBuilder(String name)
	{
		/*
		 * Changes made here. PieceBuilders need to check if they already have
		 * an instantiation before making another copy of movements.
		 */
		mName = name;
		if (!PieceBuilder.isPieceType(name))
			mMovements = Maps.newHashMap();
		else
			mMovements = mPieceTypes.get(name).mMovements;
	}

	/**
	 * Get the Set of the names of Piece types
	 * 
	 * @return The Set of names of Piece types
	 */
	public static Set<String> getSet()
	{
		return mPieceTypes.keySet();
	}

	/**
	 * Check if there is a Piece type of a given name
	 * 
	 * @param name The Piece type being searched for
	 * @return Whether or not there is a Piece type with the given name
	 */
	public static boolean isPieceType(String name)
	{
		return mPieceTypes.containsKey(name);
	}

	/**
	 * Make a new instance of a Piece type
	 * 
	 * @param name The name of the Piece to make
	 * @param isBlack The team for the Piece
	 * @param origin The Square the Piece occupies
	 * @param board The Board the Piece occupies
	 * @return The new Piece
	 * @throws IOException
	 */
	public static Piece makePiece(String name, boolean isBlack, Square origin, Board board) throws IOException
	{
		if (!mPieceTypes.containsKey(name))
			mPieceTypes.put(name, new PieceBuilder(name));
		return mPieceTypes.get(name).makePiece(isBlack, origin, board);
	}

	/**
	 * Save the given PieceBuilder in the HashMap
	 * 
	 * @param p The PieceBuilder to save
	 */
	public static void savePieceType(PieceBuilder p)
	{
		mPieceTypes.put(p.mName, p);
	}

	public static void writeToDisk(PieceBuilder p)
	{
		try
		{
			FileOutputStream f_out = new FileOutputStream(FileUtility.getPieceFile(p.mName));
			ObjectOutputStream out = new ObjectOutputStream(f_out);
			out.writeObject(p);
			out.close();
			f_out.close();
		}
		catch (Exception e)
		{
		}
	}

	public static PieceBuilder loadFromDisk(String pieceName)
	{
		PieceBuilder toReturn;
		try
		{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(FileUtility.getPieceFile(pieceName)));
			toReturn = (PieceBuilder) in.readObject();
			in.close();
			return toReturn;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Add a movement to the HashMap
	 * 
	 * @param c Character representing the direction of movement (
	 * @param num The number of spaces permissible to move in that direction, -1
	 * for infinity.
	 */
	public void addMove(char c, int num)
	{
		// TODO Check that c is valid.
		mMovements.put(c, num);
	}

	/**
	 * Make a new instance of a Piece type
	 * 
	 * @param isBlack The team of the Piece
	 * @param origin The Square this Piece is on
	 * @param board the Board this Piece is on
	 * @return The created Piece object
	 * @throws IOException
	 */
	private Piece makePiece(boolean isBlack, Square origin, Board board) throws IOException
	{
		// TODO is it worth using reflection to get rid of that if/else?
		if (mName.equals(Messages.getString("bishop"))) //$NON-NLS-1$
			return Builder.createBishop(isBlack, origin, board);
		if (mName.equals(Messages.getString("king"))) //$NON-NLS-1$
			return Builder.createKing(isBlack, origin, board);
		if (mName.equals(Messages.getString("knight"))) //$NON-NLS-1$
			return Builder.createKnight(isBlack, origin, board);
		if (mName.equals(Messages.getString("pawn"))) //$NON-NLS-1$
			return Builder.createPawn(isBlack, origin, board);
		if (mName.equals(Messages.getString("queen"))) //$NON-NLS-1$
			return Builder.createQueen(isBlack, origin, board);
		if (mName.equals(Messages.getString("rook"))) //$NON-NLS-1$
			return Builder.createRook(isBlack, origin, board);
		else
			return new Piece(mName, isBlack, origin, board, mMovements);
		// try {
		//
		// Class<?> klazz = Class.forName("logic." + name);
		// Constructor<?> con = klazz.getConstructor(boolean.class,
		// Square.class, Board.class);
		// return (Piece) con.newInstance(isBlack, origin, board);
		// } catch (Exception e) {
		// //TODO Make sure darkImage and lightImage are not null. Or does that
		// matter?
		// return new Piece(name, darkImage, lightImage, isBlack, origin, board,
		// movements);
		// }
	}

	public void setName(String name)
	{
		mName = name;
	}

	public String getName()
	{
		return mName;
	}

	public Map<Character, Integer> getMovements()
	{
		return mMovements;
	}

	public boolean canJump()
	{
		return mCanJump;
	}

	public void setCanJump(boolean mCanJump)
	{
		this.mCanJump = mCanJump;
	}

	public void setIsKnightLike(boolean isKnightLike)
	{
		mIsKnightLike = isKnightLike;
	}

	public boolean getIsKnightLike()
	{
		return mIsKnightLike;
	}

	private static final long serialVersionUID = -1351201562740885961L;

	private static Map<String, PieceBuilder> mPieceTypes;

	public static final char NORTH = 'n';
	public static final char SOUTH = 's';
	public static final char EAST = 'e';
	public static final char WEST = 'w';
	public static final char NORTHWEST = 'f';
	public static final char NORTHEAST = 'g';
	public static final char SOUTHWEST = 'a';
	public static final char SOUTHEAST = 'd';
	public static final char KNIGHT_ONE = 'x';
	public static final char KNIGHT_TWO = 'y';

	private boolean mCanJump;
	private boolean mIsKnightLike;

	private String mName;
	private Map<Character, Integer> mMovements;
}
