
package rules.promotionmethods;

import java.util.Map;
import java.util.Set;
import models.Piece;
import models.PieceType;

public final class ClassicPromotionMethod extends PromotionMethod
{
	@Override
	public Piece promotePiece(Piece pieceToPromote, Map<PieceType, Set<PieceType>> promotionMap)
	{
		if (!promotionMap.containsKey(pieceToPromote))
			return pieceToPromote;

		// TODO: Thoughts about this: GameController will have a static accessor
		// and will have an
		// interface that the GUI implements. That interface will include a
		// method for getting
		// the promotion choice from the user
		return pieceToPromote;

		// if (pieceTypeToPromoteFrom != null &&
		// !pieceTypeToPromoteFrom.equals(pieceToPromote.getName()))
		// {
		// // we don't want to promote the objective pieces. That makes things
		// // weird...
		// if ((pieceToPromote.isBlack() &&
		// !mGame.getBlackRules().getObjectiveName().equals(pieceToPromote.getName()))
		// || (!pieceToPromote.isBlack() &&
		// !mGame.getWhiteRules().getObjectiveName().equals(pieceToPromote.getName())))
		// {
		// try
		// {
		// PieceController promoted =
		// PieceBuilder.makePiece(pieceTypeToPromoteFrom,
		// pieceToPromote.isBlack(),
		// pieceToPromote.getSquare(), pieceToPromote.getBoard());
		// if (promoted.isBlack())
		// mGame.getBlackTeam().set(mGame.getBlackTeam().indexOf(pieceToPromote),
		// promoted);
		// else
		// mGame.getWhiteTeam().set(mGame.getWhiteTeam().indexOf(pieceToPromote),
		// promoted);
		// promoted.getLegalDests().clear();
		// promoted.setMoveCount(pieceToPromote.getMoveCount());
		// return promoted;
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		// }
		// }
		// else if (pieceTypeToPromoteFrom == null && mGame.isBlackMove() ==
		// pieceToPromote.isBlack())
		// {
		//			mPromotedToClass = ""; //$NON-NLS-1$
		// if ((!pieceToPromote.isBlack() &&
		// pieceToPromote.getPromotesTo().size() == 1) ||
		// pieceToPromote.isBlack()
		// && pieceToPromote.getPromotesTo().size() == 1)
		// mPromotedToClass = pieceToPromote.getPromotesTo().get(0);
		//			while (mPromotedToClass.equals("")) //$NON-NLS-1$
		// {
		// List<String> promotion = pieceToPromote.isBlack() ?
		// pieceToPromote.getPromotesTo() : pieceToPromote.getPromotesTo();
		// String result = (String) JOptionPane.showInputDialog(null,
		//						Messages.getString("selectPromotionType"), Messages.getString("promoChoice"), //$NON-NLS-1$ //$NON-NLS-2$
		// JOptionPane.PLAIN_MESSAGE, null, promotion.toArray(), null);
		//
		// if (result == null)
		// continue;
		//
		// mPromotedToClass = result;
		// pieceTypeToPromoteFrom = result;
		// }
		// }
		// else if (pieceTypeToPromoteFrom != null && !pieceToPromote.isBlack()
		// && pieceToPromote.getPromotesTo() != null
		// && pieceToPromote.getPromotesTo().contains(pieceTypeToPromoteFrom))
		// {
		// mPromotedToClass = pieceTypeToPromoteFrom;
		// }
		// else if (pieceTypeToPromoteFrom != null && pieceToPromote.isBlack()
		// && pieceToPromote.getPromotesTo() != null
		// && pieceToPromote.getPromotesTo().contains(pieceTypeToPromoteFrom))
		// {
		// mPromotedToClass = pieceTypeToPromoteFrom;
		// }
		//
		// try
		// {
		// PieceController promoted = PieceBuilder.makePiece(mPromotedToClass,
		// pieceToPromote.isBlack(), pieceToPromote.getSquare(),
		// pieceToPromote.getBoard());
		// if (promoted.isBlack())
		// mGame.getBlackTeam().set(mGame.getBlackTeam().indexOf(pieceToPromote),
		// promoted);
		// else
		// mGame.getWhiteTeam().set(mGame.getWhiteTeam().indexOf(pieceToPromote),
		// promoted);
		// promoted.getLegalDests().clear();
		// promoted.setMoveCount(pieceToPromote.getMoveCount());
		// return promoted;
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// return pieceToPromote;
		// }
	}

	@Override
	public Piece undoPromotion(Piece pieceToDemote)
	{
		// TODO:
		// return classicPromotion(pieceToUnpromote,
		// mLastPromotedFromPieceName);
		return super.undoPromotion(pieceToDemote);
	}

	// private static String mLastPromotedFromPieceName;

	// private String mResetLastPromoted;
	// private String mPromotedToClass;
}
