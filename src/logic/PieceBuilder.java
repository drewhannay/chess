package logic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;

/**
 * PieceBuilder.java
 * 
 * Builder class to create Piece Types
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas
 * 
 * CSCI 335, Wheaton College, Spring 2011 Phase 2 April 7, 2011
 */
public class PieceBuilder implements Serializable
{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -1351201562740885961L;

	/**
	 * The HashMap of stored Piece types
	 */
	private static HashMap<String, PieceBuilder> pieceTypes = new HashMap<String, PieceBuilder>();
	/**
	 * The name of this Piece Type
	 */
	private String name;
	/**
	 * The Icon for this Piece Type (Light Team).
	 */
	private ImageIcon lightImage;
	/**
	 * The Icon for this Piece Type (Dark Team).
	 */
	private ImageIcon darkImage;
	/**
	 * HashMap for this Piece Type's movements.
	 */
	private HashMap<Character, Integer> movements;

	/**
	 * Static Initializer Read in the saved HashMap from a file. If that fails,
	 * build a new HashMap with Classic chess pieces and save that to a file.
	 */
	static
	{
		initPieceTypes();
	}

	/**
	 * Initialize types...O
	 */
	public static void initPieceTypes()
	{
		pieceTypes = new HashMap<String, PieceBuilder>();
		pieceTypes.put("Pawn", new PieceBuilder("Pawn"));
		pieceTypes.put("Rook", new PieceBuilder("Rook"));
		pieceTypes.put("Bishop", new PieceBuilder("Bishop"));
		pieceTypes.put("Knight", new PieceBuilder("Knight"));
		pieceTypes.put("Queen", new PieceBuilder("Queen"));
		pieceTypes.put("King", new PieceBuilder("King"));
	}

	/**
	 * Constructor. Generic Constructor, initializing movement HashMap.
	 */
	public PieceBuilder()
	{
		movements = new HashMap<Character, Integer>();
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
		this.name = name;
		if (!PieceBuilder.isPieceType(name))
		{

			movements = new HashMap<Character, Integer>();
		}
		else
		{
			movements = pieceTypes.get(name).movements;
		}
	}

	/**
	 * Get the Set of the names of Piece types
	 * 
	 * @return The Set of names of Piece types
	 */
	public static Set<String> getSet()
	{
		return pieceTypes.keySet();
	}

	/**
	 * Check if there is a Piece type of a given name
	 * 
	 * @param name The Piece type being searched for
	 * @return Whether or not there is a Piece type with the given name
	 */
	public static boolean isPieceType(String name)
	{
		return pieceTypes.containsKey(name);
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
		return pieceTypes.get(name).makePiece(isBlack, origin, board);
	}

	/**
	 * Save the given PieceBuilder in the HashMap
	 * 
	 * @param p The PieceBuilder to save
	 */
	public static void savePieceType(PieceBuilder p)
	{
		pieceTypes.put(p.name, p);
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
		movements.put(c, num);
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
		// TODO is it worth using reflection to get rid of that if/else? Doable,
		// but worth it?
		// I know how to do this so talk to me if we want to --Alisa
		if (name.equals("Bishop"))
			return Builder.createBishop(isBlack, origin, board);
		if (name.equals("King"))
			return Builder.createKing(isBlack, origin, board);
		if (name.equals("Knight"))
			return Builder.createKnight(isBlack, origin, board);
		if (name.equals("Pawn"))
			return Builder.createPawn(isBlack, origin, board);
		if (name.equals("Queen"))
			return Builder.createQueen(isBlack, origin, board);
		if (name.equals("Rook"))
			return Builder.createRook(isBlack, origin, board);
		else
			return new Piece(name, darkImage, lightImage, isBlack, origin, board, movements);
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

	/**
	 * Setter method for darkImage
	 * 
	 * @param darkImage The darkImage to set
	 */
	public void setDarkImage(ImageIcon darkImage)
	{
		this.darkImage = darkImage;
	}

	/**
	 * Setter method for lightImage
	 * 
	 * @param lightImage The lightImage to set
	 */
	public void setLightImage(ImageIcon lightImage)
	{
		this.lightImage = lightImage;
	}

	/**
	 * Setter method for the name of the Piece
	 * 
	 * @param name The name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}
