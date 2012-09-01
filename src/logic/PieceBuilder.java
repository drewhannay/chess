package logic;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;

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
		m_pieceTypes = Maps.newHashMap();
		m_pieceTypes.put("Pawn", new PieceBuilder("Pawn"));
		m_pieceTypes.put("Rook", new PieceBuilder("Rook"));
		m_pieceTypes.put("Bishop", new PieceBuilder("Bishop"));
		m_pieceTypes.put("Knight", new PieceBuilder("Knight"));
		m_pieceTypes.put("Queen", new PieceBuilder("Queen"));
		m_pieceTypes.put("King", new PieceBuilder("King"));
	}

	/**
	 * Constructor. Generic Constructor, initializing movement HashMap.
	 */
	public PieceBuilder()
	{
		m_movements = Maps.newHashMap();
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
		m_name = name;
		if (!PieceBuilder.isPieceType(name))
			m_movements = Maps.newHashMap();
		else
			m_movements = m_pieceTypes.get(name).m_movements;
	}

	/**
	 * Get the Set of the names of Piece types
	 * 
	 * @return The Set of names of Piece types
	 */
	public static Set<String> getSet()
	{
		return m_pieceTypes.keySet();
	}

	/**
	 * Check if there is a Piece type of a given name
	 * 
	 * @param name The Piece type being searched for
	 * @return Whether or not there is a Piece type with the given name
	 */
	public static boolean isPieceType(String name)
	{
		return m_pieceTypes.containsKey(name);
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
		return m_pieceTypes.get(name).makePiece(isBlack, origin, board);
	}

	/**
	 * Save the given PieceBuilder in the HashMap
	 * 
	 * @param p The PieceBuilder to save
	 */
	public static void savePieceType(PieceBuilder p)
	{
		m_pieceTypes.put(p.m_name, p);
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
		m_movements.put(c, num);
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
		if (m_name.equals("Bishop"))
			return Builder.createBishop(isBlack, origin, board);
		if (m_name.equals("King"))
			return Builder.createKing(isBlack, origin, board);
		if (m_name.equals("Knight"))
			return Builder.createKnight(isBlack, origin, board);
		if (m_name.equals("Pawn"))
			return Builder.createPawn(isBlack, origin, board);
		if (m_name.equals("Queen"))
			return Builder.createQueen(isBlack, origin, board);
		if (m_name.equals("Rook"))
			return Builder.createRook(isBlack, origin, board);
		else
			return new Piece(m_name, m_darkImage, m_lightImage, isBlack, origin, board, m_movements);
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

	public void setDarkImage(ImageIcon darkImage)
	{
		m_darkImage = darkImage;
	}

	public void setLightImage(ImageIcon lightImage)
	{
		m_lightImage = lightImage;
	}

	public void setName(String name)
	{
		m_name = name;
	}

	private static final long serialVersionUID = -1351201562740885961L;

	private static Map<String, PieceBuilder> m_pieceTypes;

	private String m_name;
	private ImageIcon m_lightImage;
	private ImageIcon m_darkImage;
	private Map<Character, Integer> m_movements;

}
