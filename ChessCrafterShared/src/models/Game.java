package models;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logic.AlgebraicConverter;
import logic.Messages;
import logic.Move;

import timer.ChessTimer;
import timer.TimerTypes;
import utility.FileUtility;
import ai.FakeMove;

import com.google.common.collect.Lists;

public class Game implements Serializable
{
	public Game(String gameType, Board[] boards, List<Piece> whiteTeam, List<Piece> blackTeam, Rules whiteRules, Rules blackRules,
			Map<String, List<String>> whitePromotionMap, Map<String, List<String>> blackPromotionMap)
	{
		mGameType = gameType;
		mBoards = boards;

		mWhiteTimer = ChessTimer.createTimer(TimerTypes.NO_TIMER, null, 0, 0, false);
		mBlackTimer = ChessTimer.createTimer(TimerTypes.NO_TIMER, null, 0, 0, true);

		mWhiteTeam = whiteTeam;
		mBlackTeam = blackTeam;

		mWhiteRules = whiteRules;
		mBlackRules = blackRules;

		mWhitePromotionMap = whitePromotionMap;
		mBlackPromotionMap = blackPromotionMap;

		whiteRules.setGame(this);
		blackRules.setGame(this);

		for (Board board : mBoards)
			board.setGame(this);

		mStaleLegalDests = true;

		mHistory = Lists.newArrayList();
	}

	/**
	 * Call the afterMove method in the correct rules object
	 * 
	 * @param move The Move that has just been performed
	 */
	public void afterMove(Move move)
	{
		if (isBlackMove())
			getBlackRules().afterMove(move);
		else
			getWhiteRules().afterMove(move);
	}

	/**
	 * Generate the legal destinations for every piece on the board.
	 */
	public void genLegalDests()
	{
		Piece[] threats = null;
		Piece movingObjectivePiece = null;
		Piece otherObjectivePiece = null;
		List<Piece> movingTeam = null;
		List<Piece> otherTeam = null;

		setStaleLegalDests(false);

		for (int i = 0; i < mBoards.length; i++)
		{
			for (int r = 0, c = 0; r < mBoards[i].getMaxRow(); r++)
			{
				for (c = 0; c < mBoards[i].getMaxCol(); c++)
				{
					if (mBoards[i].mSquares[r][c].getPiece() != null)
					{
						mBoards[i].mSquares[r][c].getPiece().genLegalDests(
								(isBlackMove() ? getBlackRules() : getWhiteRules()).getBoard(mBoards[i]));
					}
				}
			}
		}

		movingObjectivePiece = (isBlackMove()) ? mBlackRules.objectivePiece(true) : mWhiteRules.objectivePiece(false);
		movingTeam = (isBlackMove()) ? getBlackTeam() : getWhiteTeam();
		otherObjectivePiece = (isBlackMove()) ? mBlackRules.objectivePiece(true) : mWhiteRules.objectivePiece(false);
		otherTeam = (isBlackMove()) ? getWhiteTeam() : getBlackTeam();

		// Make sure the objective piece doesn't put himself in check
		if (movingObjectivePiece != null)
		{
			(movingObjectivePiece.isBlack() ? getBlackRules() : getWhiteRules()).cropLegalDests(movingObjectivePiece,
					movingObjectivePiece, movingTeam);
		}
		if (otherObjectivePiece != null)
		{
			(otherObjectivePiece.isBlack() ? getBlackRules() : getWhiteRules()).cropLegalDests(otherObjectivePiece,
					otherObjectivePiece, otherTeam);
		}

		if (movingObjectivePiece != null)
		{
			// Now see if any of the moves puts the objective piece in check and
			// are therefore illegal
			for (int i = 0; i < otherTeam.size(); i++)
			{
				if (otherTeam.equals(getWhiteTeam()))
					getWhiteRules().cropLegalDests(movingObjectivePiece, otherTeam.get(i), movingTeam);
				else
					getBlackRules().cropLegalDests(movingObjectivePiece, otherTeam.get(i), movingTeam);
			}
		}

		(isBlackMove() ? getBlackRules() : getWhiteRules()).adjustTeamLegalDestinations(movingTeam);

		// if the objective piece is in check, the legal moves list must be
		// modified accordingly
		if (movingObjectivePiece != null && movingObjectivePiece.isInCheck())
		{
			if (getLastMove() != null)
				getLastMove().setCheck(true);

			threats = getThreats(movingObjectivePiece);

			switch (threats.length)
			{
			case 1:
				// there is only one threat, so another Piece could block, or
				// the King could move
				for (int i = 0; i < movingTeam.size(); i++)
					movingTeam.get(i).genLegalDestsSaveKing(movingObjectivePiece, threats[0]);

				break;
			case 2:
				// since there is two threats, the objective piece is the only
				// one who can get himself out of check.
				for (int i = 0; i < movingTeam.size(); i++)
				{
					Piece p = (movingTeam.get(i));
					if (p != movingObjectivePiece)
					{
						p.getLegalDests().clear();
						p.getGuardSquares().clear();
					}
				}

				if (getLastMove() != null)
					getLastMove().setDoubleCheck(true);

				break;
			}
		}
	}

	public Rules getBlackRules()
	{
		return mBlackRules;
	}

	public List<Piece> getBlackTeam()
	{
		return mBlackTeam;
	}

	public ChessTimer getBlackTimer()
	{
		return mBlackTimer;
	}

	public Board[] getBoards()
	{
		return mBoards;
	}

	/**
	 * Get an array of the captured Pieces for the specified team
	 * 
	 * @param isBlack The team of which to find captured pieces
	 * @return The Piece[] of captured Pieces
	 */
	public Piece[] getCapturedPieces(boolean isBlack)
	{
		Piece[] pows = null;
		List<Piece> team = (isBlack) ? getBlackTeam() : getWhiteTeam();
		int count = 0;
		Piece piece = null;

		for (int i = 0; i < team.size(); i++)
		{
			if (team.get(i).isCaptured())
			{
				count++;
			}
		}

		if (count > 0)
		{
			pows = new Piece[count];
			count = 0;
			for (int i = 0; i < team.size(); i++)
			{
				piece = team.get(i);
				if (piece.isCaptured())
					pows[count++] = piece;
			}
		}
		return pows;
	}

	/**
	 * Get the Pieces of the specified team guarding the specified Square
	 * 
	 * @param square The Square being guarded
	 * @param isBlack The team guarding the Square
	 * @return The Pieces guarding the Square
	 */
	public Piece[] getGuards(Square square, boolean isBlack)
	{
		Iterator<Piece> team = (isBlack) ? getBlackTeam().iterator() : getWhiteTeam().iterator();
		List<Piece> attackers = Lists.newArrayList();

		if (isStaleLegalDests())
			genLegalDests();

		Piece piece = null;
		while (team.hasNext())
		{
			piece = team.next();
			if (piece.isGuarding(square))
				attackers.add(piece);
		}

		Piece[] guards = null;
		if (attackers.size() > 0)
		{
			guards = new Piece[attackers.size()];
			guards = attackers.toArray(guards);
		}
		return guards;
	}

	public List<Move> getHistory()
	{
		return mHistory;
	}

	public Move getLastMove()
	{
		return mLastMove;
	}

	/**
	 * Get a count of all legal moves for this turn.
	 * 
	 * @return The number of legal moves this turn.
	 */
	public int getLegalMoveCount()
	{
		int count = 0;
		List<Piece> movingTeam = (isBlackMove()) ? getBlackTeam() : getWhiteTeam();

		if (isStaleLegalDests())
			genLegalDests();

		for (int i = 0; i < movingTeam.size(); i++)
			count += (movingTeam.get(i)).getLegalDests().size();

		return count;
	}

	/**
	 * Get the specified objective piece
	 * 
	 * @param isBlack The team from which to get the objective piece
	 * @return The objective piece, if found
	 */
	public Piece getOtherObjectivePiece(boolean isBlack)
	{
		return isBlack ? mWhiteRules.objectivePiece(false) : mBlackRules.objectivePiece(true);
	}

	/**
	 * Get the threats on the given piece
	 * 
	 * @param piece The piece on which to assess threats
	 * @return The pieces threatening the specified piece
	 */
	public Piece[] getThreats(Piece piece)
	{
		return getThreats(piece.getSquare(), !piece.isBlack());
	}

	/**
	 * Get the threats on the given piece
	 * 
	 * @param threatened The piece on which to assess threats
	 * @param attackerIsBlack The team of the attacker
	 * @return The pieces threatening the specified piece
	 */
	private Piece[] getThreats(Square threatened, boolean attackerIsBlack)
	{
		Iterator<Piece> team = (attackerIsBlack) ? getBlackTeam().iterator() : getWhiteTeam().iterator();
		List<Piece> attackers = Lists.newArrayList();

		if (isStaleLegalDests())
			genLegalDests();

		Piece piece = null;
		while (team.hasNext())
		{
			piece = team.next();
			if (piece.isLegalAttack(threatened))
				attackers.add(piece);
		}

		Piece[] threats = null;
		if (attackers.size() > 0)
		{
			threats = new Piece[attackers.size()];
			threats = attackers.toArray(threats);
			return threats;
		}

		return threats;
	}

	public Rules getWhiteRules()
	{
		return mWhiteRules;
	}

	public List<Piece> getWhiteTeam()
	{
		return mWhiteTeam;
	}

	public ChessTimer getWhiteTimer()
	{
		return mWhiteTimer;
	}

	public boolean isBlackMove()
	{
		return mIsBlackMove;
	}

	/**
	 * Determines if a unit threatens the King-type unit.
	 * 
	 * @return If king-type unit is threatened.
	 */
	private boolean isCheck()
	{
		boolean check = false;

		if (getLastMove() != null)
			return getLastMove().isCheck();

		if (isStaleLegalDests())
			genLegalDests();

		check = (isBlackMove()) ? getBlackRules().objectivePiece(true).isInCheck() : getWhiteRules().objectivePiece(false).isInCheck();

		return check;
	}

	/**
	 * Determines if the King-type unit cannot escape check.
	 * 
	 * @return If the King-type unit is in Check mate.
	 */
	public boolean isCheckmate()
	{
		if (getLastMove() != null)
			return getLastMove().isCheckmate();

		if (isStaleLegalDests())
			genLegalDests();

		if (getLegalMoveCount() == 0 && isCheck())
			return true;
		else
			return false;
	}

	/**
	 * Check if this game is classic chess
	 * 
	 * @return If this game is classic chess
	 */
	public boolean isClassicChess()
	{
		return mGameType.equals(Messages.getString("classic")); //$NON-NLS-1$
	}

	/**
	 * Check if the given square is guarded by the given team
	 * 
	 * @param square The square to assess
	 * @param isBlack The team to assess
	 * @return If the square is guarded by the given team
	 */
	public boolean isGuarded(Square square, boolean isBlack)
	{
		return (getGuards(square, isBlack) != null);
	}

	public boolean isStaleLegalDests()
	{
		return mStaleLegalDests;
	}

	/**
	 * Determines if a player is unable to move.
	 * 
	 * @return If game is over by Stale mate.
	 */
	public boolean isStalemate()
	{
		if (getLastMove() != null)
			return getLastMove().isStalemate();

		if (isStaleLegalDests())
			genLegalDests();

		if (getLegalMoveCount() == 0 && !isCheck())
			return true;
		else
			return false;
	}

	/**
	 * Check threats for the given piece
	 * 
	 * @param piece The piece to assess threats on
	 * @return If the piece is threatened
	 */
	public boolean isThreatened(Piece piece)
	{
		return isThreatened(piece.getSquare(), !piece.isBlack());
	}

	/**
	 * Check threats for the given square
	 * 
	 * @param sq The square to assess threats for
	 * @param isBlack The team to assess threats for
	 * @return If the square is threatened by the given team
	 */
	public boolean isThreatened(Square sq, boolean isBlack)
	{
		return (getThreats(sq, isBlack) != null);
	}

	/**
	 * Move to the next turn
	 */
	public void nextTurn()
	{
		// we only need to track turns once, so always use the whiteRules
		setBlackMove(getWhiteRules().nextTurn());
	}

	/**
	 * Play a move in the Game
	 * 
	 * @param move The Move to play
	 * @throws Exception If the Move was illegal
	 */
	public void playMove(Move move) throws Exception
	{
		move.execute();

		if (getHistory().contains(move))
			return;

		getHistory().add(move);
		getBlackRules().checkEndOfGame(mBlackRules.objectivePiece(true));
		getWhiteRules().checkEndOfGame(mWhiteRules.objectivePiece(false));
	}

	/**
	 * Revert to the previous turn
	 */
	public void prevTurn()
	{
		setBlackMove(getWhiteRules().prevTurn(mIsPlayback));
	}

	/**
	 * Saves game's state to file.
	 * 
	 * @param dirName The directory in which to save the game
	 * @param fileName The name for the game file
	 * @param ACN Whether the game should be saved in ACN
	 * @param inProgress
	 */
	public void saveGame(String fileName, boolean ACN, boolean inProgress)
	{
		try
		{
			if (!inProgress)
			{
				FileOutputStream f_out = new FileOutputStream(FileUtility.getCompletedGamesFile(fileName));
				ObjectOutputStream out = new ObjectOutputStream(f_out);
				out.writeObject(this);
				out.close();
				f_out.close();
				if (ACN)
					AlgebraicConverter.convert(getHistory(), fileName);
			}
			else
			{
				FileOutputStream f_out = new FileOutputStream(FileUtility.getGamesInProgressFile(fileName));
				ObjectOutputStream out = new ObjectOutputStream(f_out);
				out.writeObject(this);
				out.close();
				f_out.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setBlackMove(boolean isBlackMove)
	{
		mIsBlackMove = isBlackMove;
	}

	public void setLastMove(Move lastMove)
	{
		mLastMove = lastMove;
	}

	/**
	 * @param staleLegalDests the staleLegalDests to set
	 */
	public void setStaleLegalDests(boolean staleLegalDests)
	{
		mStaleLegalDests = staleLegalDests;
	}

	/**
	 * Set the timers for this game
	 * 
	 * @param whiteTimer The timer for the white team
	 * @param blackTimer The timer for the black team
	 */
	public void setTimers(ChessTimer whiteTimer, ChessTimer blackTimer)
	{
		mWhiteTimer = whiteTimer;
		mBlackTimer = blackTimer;
	}

	/**
	 * Undo the afterMove events
	 * 
	 * @param move The Move to undo
	 */
	public void undoAfterMove(Move move)
	{
		if (isBlackMove())
			getBlackRules().undoAfterMove(move);
		else
			getWhiteRules().undoAfterMove(move);
	}

	/**
	 * @param fakeMove A fake move
	 * @return An actual move
	 * @throws Exception If the move is not legal.
	 */
	public Move fakeToRealMove(FakeMove fakeMove) throws Exception
	{
		Board to = mBoards[fakeMove.mBoardIndex];
		Board from = ((mIsBlackMove) ? mBlackRules : mWhiteRules).getBoard(to);
		return new Move(from, from.getSquare(fakeMove.mOriginRow, fakeMove.mOriginColumn), to.getSquare(fakeMove.mDestinationRow,
				fakeMove.mDestinationColumn), fakeMove.mPromotionPieceName);
	}

	/**
	 * @param move An actual move
	 * @return A fake move.
	 */
	public FakeMove moveToFakeMove(Move move)
	{
		int boardNum = (move.board == mBoards[0]) ? 0 : 1;
		String promoName = null;

		if (mHistory.get(mHistory.size() - 1).getPromoPiece() != null)
			promoName = mHistory.get(mHistory.size() - 1).getPromoPiece().getName();

		return new FakeMove(boardNum, move.origin.getRow(), move.origin.getCol(), move.getDest().getRow(), move.getDest().getCol(),
				promoName);
	}

	public Map<String, List<String>> getWhitePromotionMap()
	{
		return mWhitePromotionMap;
	}

	public Map<String, List<String>> getBlackPromotionMap()
	{
		return mBlackPromotionMap;
	}

	public void setIsPlayback(boolean isPlayback)
	{
		mIsPlayback = isPlayback;
	}

	public boolean isPlayback()
	{
		return mIsPlayback;
	}

	private static final long serialVersionUID = 7291801823624891384L;

	private final Board[] mBoards;
	private final Rules mWhiteRules;
	private final Rules mBlackRules;
	private final String mGameType;
	private final List<Piece> mWhiteTeam;
	private final List<Piece> mBlackTeam;
	private final Map<String, List<String>> mWhitePromotionMap;
	private final Map<String, List<String>> mBlackPromotionMap;

	private ChessTimer mWhiteTimer;
	private ChessTimer mBlackTimer;

	private boolean mIsBlackMove;
	private boolean mIsPlayback;
	private boolean mStaleLegalDests;

	private List<Move> mHistory;
	private Move mLastMove;
}
