
package logic;

import java.util.List;
import java.util.Map;
import models.Board;
import models.ChessCoordinates;
import models.Game;
import models.Piece;
import models.PieceType;
import models.Team;
import models.turnkeeper.ClassicTurnKeeper;
import rules.Rules;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class GameBuilder
{
	public static final int BLACK = 1;
	public static final int WHITE = 2;
	public static final int BOTH = 3;

	// TODO: remove this eventually
	public static void main(String[] args)
	{
		buildClassic();
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of this Game type
	 */
	public GameBuilder(String name)
	{
		mName = name;
		mWhiteTeam = Lists.newArrayList();
		mBlackTeam = Lists.newArrayList();

		mWhitePromotionMap = Maps.newHashMap();
		mBlackPromotionMap = Maps.newHashMap();
	}

	public GameBuilder(String name, Board[] boards, List<Piece> whiteTeam, List<Piece> blackTeam, Rules whiteRules, Rules blackRules)
	{
		mName = name;
		mBoards = boards;

		mWhiteTeam = whiteTeam;
		mBlackTeam = blackTeam;
		mWhitePromotionMap = Maps.newHashMap();
		mBlackPromotionMap = Maps.newHashMap();

		mWhiteRules = whiteRules;
		mBlackRules = blackRules;
	}

	public static Game buildClassic()
	{
		long pieceId = 0;

		List<Piece> whitePieces = Lists.newArrayList();
		List<Piece> blackPieces = Lists.newArrayList();
		for (int i = 1; i < 9; i++)
		{
			whitePieces.add(new Piece(pieceId++, PieceBuilder.getPawnPieceType(), new ChessCoordinates(2, i, 0)));
			blackPieces.add(new Piece(pieceId++, PieceBuilder.getPawnPieceType(), new ChessCoordinates(7, i, 0)));
		}

		whitePieces.add(new Piece(pieceId++, PieceBuilder.getRookPieceType(), new ChessCoordinates(1, 1, 0)));
		whitePieces.add(new Piece(pieceId++, PieceBuilder.getKnightPieceType(), new ChessCoordinates(1, 2, 0)));
		whitePieces.add(new Piece(pieceId++, PieceBuilder.getBishopPieceType(), new ChessCoordinates(1, 3, 0)));
		whitePieces.add(new Piece(pieceId++, PieceBuilder.getQueenPieceType(), new ChessCoordinates(1, 4, 0)));
		whitePieces.add(new Piece(pieceId++, PieceBuilder.getKingPieceType(), new ChessCoordinates(1, 5, 0)));
		whitePieces.add(new Piece(pieceId++, PieceBuilder.getBishopPieceType(), new ChessCoordinates(1, 6, 0)));
		whitePieces.add(new Piece(pieceId++, PieceBuilder.getKnightPieceType(), new ChessCoordinates(1, 7, 0)));
		whitePieces.add(new Piece(pieceId++, PieceBuilder.getRookPieceType(), new ChessCoordinates(1, 8, 0)));

		blackPieces.add(new Piece(pieceId++, PieceBuilder.getRookPieceType(), new ChessCoordinates(8, 1, 0)));
		blackPieces.add(new Piece(pieceId++, PieceBuilder.getKnightPieceType(), new ChessCoordinates(8, 2, 0)));
		blackPieces.add(new Piece(pieceId++, PieceBuilder.getBishopPieceType(), new ChessCoordinates(8, 3, 0)));
		blackPieces.add(new Piece(pieceId++, PieceBuilder.getQueenPieceType(), new ChessCoordinates(8, 4, 0)));
		blackPieces.add(new Piece(pieceId++, PieceBuilder.getKingPieceType(), new ChessCoordinates(8, 5, 0)));
		blackPieces.add(new Piece(pieceId++, PieceBuilder.getBishopPieceType(), new ChessCoordinates(8, 6, 0)));
		blackPieces.add(new Piece(pieceId++, PieceBuilder.getKnightPieceType(), new ChessCoordinates(8, 7, 0)));
		blackPieces.add(new Piece(pieceId++, PieceBuilder.getRookPieceType(), new ChessCoordinates(8, 8, 0)));

		Rules whiteRules = new Rules();
		Rules blackRules = new Rules();

		Team[] teams = new Team[2];
		teams[0] = new Team(whiteRules, whitePieces);
		teams[1] = new Team(blackRules, blackPieces);

		Board[] boards = new Board[] { new Board(8, 8, false) };

		return new Game("Classic", boards, teams, new ClassicTurnKeeper());
	}

	public Board[] getBoards()
	{
		return mBoards;
	}

	public void setBoards(Board[] boards)
	{
		mBoards = boards;
	}

	public void setName(String name)
	{
		mName = name;
	}

	public void addToPromotionMap(PieceType key, List<PieceType> value, int colorCode)
	{
		if (colorCode == WHITE || colorCode == BOTH)
			mWhitePromotionMap.put(key, value);
		if (colorCode == BLACK || colorCode == BOTH)
			mBlackPromotionMap.put(key, value);
	}

	public void setWhitePromotionMap(Map<PieceType, List<PieceType>> promotionMap)
	{
		mWhitePromotionMap.clear();
		mWhitePromotionMap.putAll(promotionMap);
	}

	public void setBlackPromotionMap(Map<PieceType, List<PieceType>> promotionMap)
	{
		mBlackPromotionMap.clear();
		mBlackPromotionMap.putAll(promotionMap);
	}

	public Map<PieceType, List<PieceType>> getWhitePromotionMap()
	{
		return mWhitePromotionMap;
	}

	public Map<PieceType, List<PieceType>> getBlackPromotionMap()
	{
		return mBlackPromotionMap;
	}

	public Rules getWhiteRules()
	{
		return mWhiteRules;
	}

	public void setWhiteRules(Rules whiteRules)
	{
		mWhiteRules = whiteRules;
	}

	public Rules getBlackRules()
	{
		return mBlackRules;
	}

	public void setBlackRules(Rules blackRules)
	{
		mBlackRules = blackRules;
	}

	public String getName()
	{
		return mName;
	}

	public List<Piece> getWhiteTeam()
	{
		return mWhiteTeam;
	}

	public void setWhiteTeam(List<Piece> whiteTeam)
	{
		mWhiteTeam.clear();
		mWhiteTeam.addAll(whiteTeam);
	}

	public List<Piece> getBlackTeam()
	{
		return mBlackTeam;
	}

	public void setBlackTeam(List<Piece> blackTeam)
	{
		mBlackTeam.clear();
		mBlackTeam.addAll(blackTeam);
	}

	private final List<Piece> mWhiteTeam;
	private final List<Piece> mBlackTeam;
	private final Map<PieceType, List<PieceType>> mWhitePromotionMap;
	private final Map<PieceType, List<PieceType>> mBlackPromotionMap;

	private String mName;
	private Board[] mBoards;
	private Rules mWhiteRules;
	private Rules mBlackRules;
}
