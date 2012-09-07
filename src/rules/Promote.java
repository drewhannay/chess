package rules;

import javax.swing.JOptionPane;

import logic.Game;
import logic.Piece;
import logic.PieceBuilder;

public enum Promote
{
	CLASSIC,
	NO_PROMOTIONS;

	public Piece promotePiece(Piece pieceToPromote, boolean pieceCanBePromoted, String pieceTypeToPromoteFrom)
	{
		if (m_lastPromotedFromPieceName == null)
			m_lastPromotedFromPieceName = m_resetLastPromoted;

		Piece promotedPiece;
		switch (this)
		{
		case CLASSIC:
			promotedPiece = classicPromotion(pieceToPromote, pieceCanBePromoted, pieceTypeToPromoteFrom);
			break;
		case NO_PROMOTIONS:
			promotedPiece = pieceToPromote;
			break;
		default:
			promotedPiece = null;
		}

		m_resetLastPromoted = m_lastPromotedFromPieceName;
		return promotedPiece;
	}

	public Piece undo(Piece pieceToUnpromote)
	{
		switch (this)
		{
		case CLASSIC:
			return classicUndo(pieceToUnpromote);
		case NO_PROMOTIONS:
			return pieceToUnpromote;
		default:
			return null;
		}
	}

	public void setGame(Game game)
	{
		m_game = game;
	}

	private Piece classicPromotion(Piece pieceToPromote, boolean pieceCanBePromoted, String pieceTypeToPromoteFrom)
	{
		if (!pieceCanBePromoted && (pieceToPromote.getPromotesTo() == null || pieceToPromote.getPromotesTo().size() == 0))
			return pieceToPromote;
		if (!pieceCanBePromoted)
		{
			m_lastPromotedFromPieceName = pieceToPromote.getName();
			m_promotedToClass = pieceToPromote.getName();
		}
		if (pieceCanBePromoted && pieceTypeToPromoteFrom != null && !pieceTypeToPromoteFrom.equals(pieceToPromote.getName()))
		{
			try
			{
				Piece promoted = PieceBuilder.makePiece(pieceTypeToPromoteFrom, pieceToPromote.isBlack(), pieceToPromote.getSquare(), pieceToPromote.getBoard());
				if (promoted.isBlack())
					m_game.getBlackTeam().set(m_game.getBlackTeam().indexOf(pieceToPromote), promoted);
				else
					m_game.getWhiteTeam().set(m_game.getWhiteTeam().indexOf(pieceToPromote), promoted);
				promoted.getLegalDests().clear();
				promoted.setMoveCount(pieceToPromote.getMoveCount());
				return promoted;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (!pieceCanBePromoted && (pieceToPromote.getPromotesTo() == null || pieceToPromote.getPromotesTo().size() == 0))
			return pieceToPromote;
		if (!pieceCanBePromoted && pieceTypeToPromoteFrom == null && m_game.isBlackMove() == pieceToPromote.isBlack())
		{
			m_promotedToClass = "";
			if (pieceToPromote.getPromotesTo().size() == 1)
				m_promotedToClass = pieceToPromote.getPromotesTo().get(0);
			while (m_promotedToClass.equals(""))
			{
				String result = (String) JOptionPane.showInputDialog(null, "Select the Promotion type:", "Promo choice",
						JOptionPane.PLAIN_MESSAGE, null, pieceToPromote.getPromotesTo().toArray(), null);

				if (result == null)
					continue;

				m_promotedToClass = result;
				pieceTypeToPromoteFrom = result;
			}
		}
		else if (pieceTypeToPromoteFrom != null && pieceToPromote.getPromotesTo() != null && pieceToPromote.getPromotesTo().contains(pieceTypeToPromoteFrom))
		{
			m_promotedToClass = pieceTypeToPromoteFrom;
		}

		try
		{
			Piece promoted = PieceBuilder.makePiece(m_promotedToClass, pieceToPromote.isBlack(), pieceToPromote.getSquare(), pieceToPromote.getBoard());
			if (promoted.isBlack())
				m_game.getBlackTeam().set(m_game.getBlackTeam().indexOf(pieceToPromote), promoted);
			else
				m_game.getWhiteTeam().set(m_game.getWhiteTeam().indexOf(pieceToPromote), promoted);
			promoted.getLegalDests().clear();
			promoted.setMoveCount(pieceToPromote.getMoveCount());
			return promoted;
		}
		catch (Exception e)
		{
			return pieceToPromote;
		}
	}

	private Piece classicUndo(Piece pieceToUnpromote)
	{
		return classicPromotion(pieceToUnpromote, true, m_lastPromotedFromPieceName);
	}

	private static String m_lastPromotedFromPieceName;

	private Game m_game;
	private String m_resetLastPromoted;
	private String m_promotedToClass;
}
