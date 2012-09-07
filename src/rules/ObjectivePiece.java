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

		m_objectivePieceName = objectivePieceName;
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
		m_game = game;
	}

	public String getObjectivePieceName()
	{
		return m_objectivePieceName;
	}

	private Piece classicObjectivePiece(boolean isBlack)
	{
		if (isBlack)
		{
			for (Piece piece : m_game.getBlackTeam())
			{
				if (piece.getName().equals("King"))
					return piece;
			}
		}
		else
		{
			for (Piece piece : m_game.getWhiteTeam())
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
			for (Piece piece : m_game.getBlackTeam())
			{
				if (piece.getName().equals(m_objectivePieceName))
					return piece;
			}
		}
		else
		{
			for (Piece piece : m_game.getWhiteTeam())
			{
				if (piece.getName().equals(m_objectivePieceName))
					return piece;
			}
		}

		return null;
	}

	private Game m_game;
	private String m_objectivePieceName;
}
