package rules;

import java.io.Serializable;

import logic.Game;
import logic.Piece;

public class ObjectivePiece implements Serializable
{
	public enum ObjectivePieceTypes
	{
		CLASSIC, NO_OBJECTIVE, CUSTOM_OBJECTIVE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1220279533086251233L;

	public ObjectivePiece(ObjectivePieceTypes type, String objectivePieceName)
	{
		mObjectivePieceType = type;
		mObjectivePieceName = objectivePieceName;
	}

	public ObjectivePiece(ObjectivePieceTypes type)
	{
		this(type, Messages.getString("king")); //$NON-NLS-1$
	}

	public ObjectivePiece()
	{
		this(ObjectivePieceTypes.NO_OBJECTIVE, null);
	}

	public ObjectivePieceTypes getObjectivePieceType()
	{
		return mObjectivePieceType;
	}

	public Piece getObjectivePiece(boolean isBlack)
	{
		switch (mObjectivePieceType)
		{
		case CLASSIC:
			return classicObjectivePiece(isBlack);
		case CUSTOM_OBJECTIVE:
			return customObjectivePiece(isBlack);
		case NO_OBJECTIVE:
		default:
			return null;
		}
	}

	public void setGame(Game game)
	{
		mGame = game;
	}

	public String getObjectivePieceName()
	{
		return mObjectivePieceName;
	}

	private Piece classicObjectivePiece(boolean isBlack)
	{
		if (isBlack)
		{
			for (Piece piece : mGame.getBlackTeam())
			{
				if (piece.getName().equals(Messages.getString("king"))) //$NON-NLS-1$
					return piece;
			}
		}
		else
		{
			for (Piece piece : mGame.getWhiteTeam())
			{
				if (piece.getName().equals(Messages.getString("king"))) //$NON-NLS-1$
					return piece;
			}
		}

		return null;
	}

	private Piece customObjectivePiece(boolean isBlack)
	{
		if (isBlack)
		{
			for (Piece piece : mGame.getBlackTeam())
			{
				if (piece.getName().equals(mObjectivePieceName))
					return piece;
			}
		}
		else
		{
			for (Piece piece : mGame.getWhiteTeam())
			{
				if (piece.getName().equals(mObjectivePieceName))
					return piece;
			}
		}

		return null;
	}

	private Game mGame;
	private String mObjectivePieceName;
	private ObjectivePieceTypes mObjectivePieceType;
}
