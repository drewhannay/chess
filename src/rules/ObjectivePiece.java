package rules;

import com.google.common.base.Preconditions;

import logic.Game;
import logic.Piece;

public enum ObjectivePiece
{
	CLASSIC,
	NO_OBJECTIVE,
	CUSTOM_OBJECTIVE;

	public ObjectivePiece setObjectivePieceName(String objectivePieceName)
	{
		Preconditions.checkState(this != CLASSIC);

		mObjectivePieceName = objectivePieceName;
		return this;
	}

	public Piece getObjectivePiece(boolean isBlack)
	{
		switch (this)
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
				if (piece.getName().equals("King"))
					return piece;
			}
		}
		else
		{
			for (Piece piece : mGame.getWhiteTeam())
			{
				if (piece.getName().equals("King"))
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
}
