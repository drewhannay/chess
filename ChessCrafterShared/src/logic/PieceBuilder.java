package logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import logic.PieceMovements.MovementDirection;
import models.Board;
import models.Piece;
import models.Square;
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

		for (String pieceName : FileUtility.getCustomPieceArray())
		{
			mPieceTypes.put(pieceName, new PieceBuilder(pieceName));
		}
	
	}

	/**
	 * Constructor. Generic Constructor, initializing movement HashMap.
	 */
	public PieceBuilder()
	{
		// TODO: are both of these constructors necessary?
		mPieceMovements = new PieceMovements();
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
			mPieceMovements = new PieceMovements();
		else
			mPieceMovements = mPieceTypes.get(name).getPieceMovements();
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
	
	public static String[] getSortedArrayWithCustomPieces()
	{
		Set<String> pieceNames = PieceBuilder.getSet();

		String[] pieceNameArray = pieceNames.toArray(new String[pieceNames.size()]);
		Arrays.sort(pieceNameArray, new Comparator<String>()
		{
			@Override
			public int compare(String name1, String name2)
			{
				boolean firstIsStandard = PieceBuilder.isClassicPieceType(name1);
				boolean secondIsStandard = PieceBuilder.isClassicPieceType(name2);

				if (firstIsStandard)
				{
					if (secondIsStandard)
					{
						return name1.compareToIgnoreCase(name2);
					}
					else
					{
						return -1;
					}
				}
				else
				{
					if (secondIsStandard)
					{
						return 1;
					}
					else
					{
						return name1.compareToIgnoreCase(name2);
					}
				}
			}
		});
		return pieceNameArray;
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
	
	public static boolean isClassicPieceType(String name)
	{
		return (name.equals(Messages.getString("bishop")) || //$NON-NLS-1$
				name.equals(Messages.getString("king")) ||  //$NON-NLS-1$
				name.equals(Messages.getString("knight")) || //$NON-NLS-1$
				name.equals(Messages.getString("pawn")) || //$NON-NLS-1$
				name.equals(Messages.getString("queen")) || //$NON-NLS-1$
				name.equals(Messages.getString("rook"))); //$NON-NLS-1$
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
		PieceBuilder loadedBuilder = loadFromDisk(name);
		if (loadedBuilder == null)
		{
			loadedBuilder = new PieceBuilder(name);
		}
		if (!mPieceTypes.containsKey(name))
			mPieceTypes.put(name, loadedBuilder);
		return mPieceTypes.get(name).makePiece(isBlack, origin, board);
	}

	public static void savePieceType(PieceBuilder p)
	{
		writeToDisk(p);
		mPieceTypes.put(p.mName, p);
	}
	
	private static void writeToDisk(PieceBuilder p)
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

		if (mPieceTypes.containsKey(pieceName))
			return mPieceTypes.get(pieceName);

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

	public void addMovement(MovementDirection direction, int distance)
	{
		mPieceMovements.addMovement(direction, distance);
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
			return GameBuilder.createBishop(isBlack, origin, board);
		if (mName.equals(Messages.getString("king"))) //$NON-NLS-1$
			return GameBuilder.createKing(isBlack, origin, board);
		if (mName.equals(Messages.getString("knight"))) //$NON-NLS-1$
			return GameBuilder.createKnight(isBlack, origin, board);
		if (mName.equals(Messages.getString("pawn"))) //$NON-NLS-1$
			return GameBuilder.createPawn(isBlack, origin, board);
		if (mName.equals(Messages.getString("queen"))) //$NON-NLS-1$
			return GameBuilder.createQueen(isBlack, origin, board);
		if (mName.equals(Messages.getString("rook"))) //$NON-NLS-1$
			return GameBuilder.createRook(isBlack, origin, board);
		else
			return new Piece(mName, isBlack, origin, board, mPieceMovements, mCanJump);
	}

	public void setName(String name)
	{
		mName = name;
	}

	public String getName()
	{
		return mName;
	}

	public PieceMovements getPieceMovements()
	{
		return mPieceMovements;
	}

	public boolean canJump()
	{
		return mCanJump;
	}

	public void setCanJump(boolean mCanJump)
	{
		this.mCanJump = mCanJump;
	}

	public void clearBidirectionalMovements()
	{
		mPieceMovements.clearBidirectionalMovements();
	}

	public void addBidirectionalMovement(BidirectionalMovement movement)
	{
		mPieceMovements.addBidirectionalMovement(movement);
	}

	public static void removePieceType(String pieceName)
	{
		if (mPieceTypes != null && mPieceTypes.containsKey(pieceName))
			mPieceTypes.remove(pieceName);
	}
	
	private static final long serialVersionUID = -1351201562740885961L;

	private static Map<String, PieceBuilder> mPieceTypes;

	private boolean mCanJump;
	private String mName;
	private PieceMovements mPieceMovements;
}