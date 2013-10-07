package logic;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

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
		mPieceTypes.put("Pawn", new PieceBuilder("Pawn"));
		mPieceTypes.put("Rook", new PieceBuilder("Rook"));
		mPieceTypes.put("Bishop", new PieceBuilder("Bishop"));
		mPieceTypes.put("Knight", new PieceBuilder("Knight"));
		mPieceTypes.put("Queen", new PieceBuilder("Queen"));
		mPieceTypes.put("King", new PieceBuilder("King"));
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
	 */
	public static Piece makePiece(String name, boolean isBlack, Square origin, Board board)
	{
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

	/**
	 * Add a movement to the HashMap
	 * 
	 * @param c Character representing the direction of movement
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
	 */
	private Piece makePiece(boolean isBlack, Square origin, Board board)
	{
		// TODO is it worth using reflection to get rid of that if/else?
		if (mName.equals("Bishop"))
			return Builder.createBishop(isBlack, origin, board);
		if (mName.equals("King"))
			return Builder.createKing(isBlack, origin, board);
		if (mName.equals("Knight"))
			return Builder.createKnight(isBlack, origin, board);
		if (mName.equals("Pawn"))
			return Builder.createPawn(isBlack, origin, board);
		if (mName.equals("Queen"))
			return Builder.createQueen(isBlack, origin, board);
		if (mName.equals("Rook"))
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

	private static final long serialVersionUID = -1351201562740885961L;

	private static Map<String, PieceBuilder> mPieceTypes;

	private String mName;
	private Map<Character, Integer> mMovements;
}
