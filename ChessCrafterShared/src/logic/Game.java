package logic;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rules.Rules;
import timer.ChessTimer;
import timer.TimerTypes;
import utility.FileUtility;
import ai.FakeMove;

import com.google.common.collect.Lists;

/**
 * Game.java Object to save Game state.
 * 
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas CSCI 335, Wheaton College,
 * Spring 2011 Phase 2 April 7, 2011
 */
public class Game implements Serializable
{
	private Map<String, List<String>> mPromotionMap;
	/**
	 * Constructor. Initializes and saves state of Game information.
	 * 
	 * @param gameType Game type identifier.
	 * @param boards Reference to boards Array.
	 * @param whiteRules The rules for the white team
	 * @param blackRules The rules for the black team
	 * @param promotionMap 
	 */
	public Game(String gameType, Board[] boards, Rules whiteRules, Rules blackRules, Map<String, List<String>> promotionMap)
	{
		mGameType = gameType;
		mBoards = boards;
		mWhiteTimer = ChessTimer.createTimer(TimerTypes.NO_TIMER, null, 0, 0, false);
		mBlackTimer = ChessTimer.createTimer(TimerTypes.NO_TIMER, null, 0, 0, true);
		setWhiteRules(whiteRules);
		whiteRules.setGame(this);
		setBlackRules(blackRules);
		blackRules.setGame(this);
		setBlackMove(false);

		setPromotionMap(promotionMap);
		
		for (Board b : mBoards)
			b.setGame(this);

		mHistory = Lists.newArrayList();
	}

	/**
	 * Call the afterMove method in the correct rules object
	 * 
	 * @param m The Move that has just been performed
	 */
	public void afterMove(Move m)
	{
		if (isBlackMove())
			getBlackRules().afterMove(m);
		else
			getWhiteRules().afterMove(m);
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

		movingObjectivePiece = (isBlackMove()) ? mBlackObjectivePiece : mWhiteObjectivePiece;
		movingTeam = (isBlackMove()) ? getBlackTeam() : getWhiteTeam();
		otherObjectivePiece = (isBlackMove()) ? mBlackObjectivePiece : mWhiteObjectivePiece;
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

		// If the King is in check, the legal moves list must be modified
		// accordingly.
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
		return getCaptures(isBlack, true);
	}

	/**
	 * Get the captured or not captured pieces from the specified team
	 * 
	 * @param isBlack The team to get pieces from
	 * @param isCaptured Whether to collect captured or free pieces
	 * @return The Piece[] of matching pieces
	 */
	protected Piece[] getCaptures(boolean isBlack, boolean isCaptured)
	{
		Piece[] pows = null;
		List<Piece> team = (isBlack) ? getBlackTeam() : getWhiteTeam();
		int count = 0;
		Piece piece = null;

		for (int i = 0; i < team.size(); i++)
		{
			if (team.get(i).isCaptured()){
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
				if (piece.isCaptured() == isCaptured)
					pows[count++] = piece;
			}
		}
		return pows;
	}

	/**
	 * Get the Pieces of the specified team guarding the specified Square
	 * 
	 * @param sq The Square being guarded
	 * @param isBlack The team guarding the Square
	 * @return The Pieces guarding the Square
	 */
	public Piece[] getGuards(Square sq, boolean isBlack)
	{
		Iterator<Piece> team = (isBlack) ? getBlackTeam().iterator() : getWhiteTeam().iterator();
		List<Piece> attackers = Lists.newArrayList();

		if (isStaleLegalDests())
			genLegalDests();

		Piece piece = null;
		while (team.hasNext())
		{
			piece = team.next();
			if (piece.isGuarding(sq))
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
		return isBlack ? mWhiteObjectivePiece : mBlackObjectivePiece;
	}

	/**
	 * Get the rules for the specified team
	 * 
	 * @param isBlack The team to get the rules for
	 * @return The rules for the specified team
	 */
	public Rules getRules(boolean isBlack)
	{
		return isBlack ? getBlackRules() : getWhiteRules();
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
	public Piece[] getThreats(Square threatened, boolean attackerIsBlack)
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
	public boolean isCheck()
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
	 * @param sq The square to assess
	 * @param isBlack The team to assess
	 * @return If the square is guarded by the given team
	 */
	public boolean isGuarded(Square sq, boolean isBlack)
	{
		return (getGuards(sq, isBlack) != null);
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
		// We only need to track turns once, so always use the whiteRules
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
		Move m = move;
		m.execute();

		if (getHistory().contains(m))
			return;

		getHistory().add(m);
		getBlackRules().checkEndOfGame(mBlackObjectivePiece);
		getWhiteRules().checkEndOfGame(mWhiteObjectivePiece);
	}

	/**
	 * Revert to the previous turn
	 */
	public void prevTurn()
	{
		setBlackMove(getWhiteRules().prevTurn());
	}

	/**
	 * Saves game's state to file.
	 * 
	 * @param dirName The directory in which to save the game
	 * @param fileName The name for the game file
	 * @param ACN Whether the game should be saved in ACN
	 */
	public void saveGame(String fileName, boolean ACN)
	{
		try
		{
			if (ACN)
			{
				FileOutputStream f_out = new FileOutputStream(FileUtility.getCompletedGamesFile(fileName + ".acn")); //$NON-NLS-1$
				ObjectOutputStream out = new ObjectOutputStream(f_out);
				out.writeObject(this);
				out.close();
				f_out.close();
				AlgebraicConverter.convert(getHistory(), (fileName + ".acn")); //$NON-NLS-1$
			}
			else
			{
				FileOutputStream f_out = new FileOutputStream(FileUtility.getGamesInProgressFile(fileName + ".var")); //$NON-NLS-1$
				ObjectOutputStream out = new ObjectOutputStream(f_out);
				out.writeObject(this);
				out.close();
				f_out.close();
			}
		}
		catch (Exception e)
		{
		}
	}

	public void setBlackMove(boolean isBlackMove)
	{
		mIsBlackMove = isBlackMove;
	}

	public void setBlackRules(Rules blackRules)
	{
		mBlackRules = blackRules;
	}

	/**
	 * Sets the List of Pieces for the black team.
	 * 
	 * @param blackTeam List of black team pieces.
	 */
	public void setBlackTeam(List<Piece> blackTeam)
	{
		mBlackTeam = blackTeam;
		mBlackObjectivePiece = getBlackRules().objectivePiece(true);
	}

	public void setLastMove(Move lastMove)
	{
		mLastMove = lastMove;
	}

	/**
	 * Set the Result for the Game
	 * 
	 * @param result The Result to set.
	 */
	public void setResult(Result result)
	{
		Move m = getLastMove();
		m.setResult(result);
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

	public void setWhiteRules(Rules whiteRules)
	{
		mWhiteRules = whiteRules;
	}

	/**
	 * Sets the ArrayList of Pieces for the white team.
	 * 
	 * @param whiteTeam ArrayList of white team pieces.
	 */
	public void setWhiteTeam(List<Piece> whiteTeam)
	{
		mWhiteTeam = whiteTeam;
		mWhiteObjectivePiece = getWhiteRules().objectivePiece(false);
	}

	/**
	 * Undo the afterMove events
	 * 
	 * @param m The Move to undo
	 */
	public void undoAfterMove(Move m)
	{
		if (isBlackMove())
			getBlackRules().undoAfterMove(m);
		else
			getWhiteRules().undoAfterMove(m);
	}

	/**
	 * @param m A fake move
	 * @return An actual move
	 * @throws Exception If the move is not legal.
	 */
	public Move fakeToRealMove(FakeMove m) throws Exception
	{
		Board to = mBoards[m.mBoardIndex];
		Board from = ((mIsBlackMove) ? mBlackRules : mWhiteRules).getBoard(to);
		return new Move(from, from.getSquare(m.mOriginRow, m.mOriginColumn),
				to.getSquare(m.mDestinationRow, m.mDestinationColumn), m.mPromotionPieceName);
	}

	/**
	 * @param m An actual move
	 * @return A fake move.
	 */
	public FakeMove moveToFakeMove(Move m)
	{
		int boardNum = (m.board == mBoards[0]) ? 0 : 1;
		String promoName = null;

		if (mHistory.get(mHistory.size() - 1).getPromoPiece() != null)
			promoName = mHistory.get(mHistory.size() - 1).getPromoPiece().getName();

		return new FakeMove(boardNum, m.origin.getRow(), m.origin.getCol(), m.getDest().getRow(), m.getDest().getCol(), promoName);
	}

	public Map<String, List<String>> getPromotionMap() {
		return mPromotionMap;
	}

	public void setPromotionMap(Map<String, List<String>> mPromotionMap) {
		this.mPromotionMap = mPromotionMap;
	}

	private static final long serialVersionUID = 7291801823624891384L;

	private Board[] mBoards;
	private Rules mWhiteRules;
	private Rules mBlackRules;
	private String mGameType;
	private ChessTimer mWhiteTimer;
	private ChessTimer mBlackTimer;
	private boolean mIsBlackMove;
	private List<Piece> mWhiteTeam;
	private List<Piece> mBlackTeam;
	private Piece mWhiteObjectivePiece;
	private Piece mBlackObjectivePiece;
	private boolean mStaleLegalDests = true;
	private List<Move> mHistory;
	private Move mLastMove = null;
}
