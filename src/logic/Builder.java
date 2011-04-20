package logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import rules.Rules;

/**
 * Builder.java Builder class to create Game Types
 * @author Drew Hannay, Daniel Opdyke & Alisa Maas CSCI 335, Wheaton College,
 *         Spring 2011 Phase 2 April 7, 2011
 */
public class Builder implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 2099226533521671457L;

	/**
	 * The name of this Game type
	 */
	private String name;

	/**
	 * The Board[] for this Game type
	 */
	private Board[] boards;

	/**
	 * List of white Pieces
	 */
	public List<Piece> whiteTeam;

	/**
	 * List of black Pieces
	 */
	public List<Piece> blackTeam;

	/**
	 * Rules object for the white player
	 */
	private Rules whiteRules;

	/**
	 * Rules object for the black player
	 */
	private Rules blackRules;

	/**
	 * Constructor
	 * @param name The name of this Game type
	 */
	public Builder(String name) {
		this.name = name;
		whiteTeam = new ArrayList<Piece>();
		blackTeam = new ArrayList<Piece>();
	}

	/**
	 * Constructor
	 * @param name The name of this Game type
	 * @param boards The Board[] for this Game type
	 * @param whiteTeam The List of white pieces
	 * @param blackTeam The List of black pieces
	 * @param blackRules The rules for the black team
	 * @param whiteRules The rules for the white team
	 */
	public Builder(String name, Board[] boards, List<Piece> whiteTeam,
			List<Piece> blackTeam, Rules whiteRules, Rules blackRules) {
		this.name = name;
		this.boards = boards;
		this.whiteTeam = whiteTeam;
		this.blackTeam = blackTeam;
		this.whiteRules = whiteRules;
		this.blackRules = blackRules;
	}

	/**
	 * Build the classic game of chess for the user.
	 */
	private static void buildClassic() {
		Builder classic = new Builder("Classic");// Name is Classic chess
		classic.setBoards(new Board[] { new Board(8, 8, false) });
		Board b = classic.boards[0];
		for (int i = 1; i < 9; i++) {
			//classic.whiteTeam.add(new Piece("Pawn", new ImageIcon("./images/pawn_dark.png"), new ImageIcon("./images/pawn_light.png"), false, b.getSquare(2,i), b, null));
			//classic.blackTeam.add(new Piece("Pawn", new ImageIcon("./images/pawn_dark.png"), new ImageIcon("./images/pawn_light.png"), true, b.getSquare(7,i), b, null));
			classic.whiteTeam.add(createPawn(false,b.getSquare(2,i),b));
			classic.blackTeam.add(createPawn(true,b.getSquare(7,i),b));
			//classic.whiteTeam.add(new Pawn(false, b.getSquare(2, i), b));
			//classic.blackTeam.add(new Pawn(true, b.getSquare(7, i), b));
		}
		
//		HashMap<Character, Integer> rookMovement = new HashMap<Character, Integer>();
//		rookMovement.put('N', -1);
//		rookMovement.put('S', -1);
//		rookMovement.put('W', -1);
//		rookMovement.put('E', -1);
		
//		HashMap<Character, Integer> queenMovement = new HashMap<Character, Integer>();
//		queenMovement.put('N', -1);
//		queenMovement.put('S', -1);
//		queenMovement.put('W', -1);
//		queenMovement.put('E', -1);
//		queenMovement.put('R', -1);
//		queenMovement.put('r', -1);
//		queenMovement.put('L', -1);
//		queenMovement.put('l', -1);
		
//		HashMap<Character, Integer> knightMovement = new HashMap<Character, Integer>();
//		knightMovement.put('x', 1);
//		knightMovement.put('y', 2);
		
//		HashMap<Character, Integer> bishopMovement = new HashMap<Character, Integer>();
//		bishopMovement.put('R', -1);
//		bishopMovement.put('r', -1);
//		bishopMovement.put('L', -1);
//		bishopMovement.put('l', -1);
		
//		HashMap<Character, Integer> kingMovement = new HashMap<Character, Integer>();
//		kingMovement.put('N', 1);
//		kingMovement.put('S', 1);
//		kingMovement.put('E', 1);
//		kingMovement.put('W', 1);
//		kingMovement.put('R', 1);
//		kingMovement.put('r', 1);
//		kingMovement.put('L', 1);
//		kingMovement.put('l', 1);
		
		//classic.whiteTeam.add(new Rook(false, b.getSquare(1, 1), b));
		//classic.whiteTeam.add(new Knight(false, b.getSquare(1, 2), b));
		//classic.whiteTeam.add(new Bishop(false, b.getSquare(1, 3), b));
		//classic.whiteTeam.add(new Queen(false, b.getSquare(1, 4), b));
		//classic.whiteTeam.add(new Bishop(false, b.getSquare(1, 6), b));
		//classic.whiteTeam.add(new Knight(false, b.getSquare(1, 7), b));
		//classic.whiteTeam.add(new Rook(false, b.getSquare(1, 8), b));
		//classic.whiteTeam.add(new King(false, b.getSquare(1, 5), b));
		classic.whiteTeam.add(createRook(false,b.getSquare(1,1),b));
		classic.whiteTeam.add(createKnight(false,b.getSquare(1,2),b));
		classic.whiteTeam.add(createBishop(false,b.getSquare(1, 3),b));
		classic.whiteTeam.add(createQueen(false,b.getSquare(1, 4),b));
		classic.whiteTeam.add(createKing(false, b.getSquare(1,5), b));
		classic.whiteTeam.add(createBishop(false,b.getSquare(1,6),b));
		classic.whiteTeam.add(createKnight(false,b.getSquare(1,7),b));
		classic.whiteTeam.add(createRook(false,b.getSquare(1,8),b));
		//classic.whiteTeam.add(new Piece("Rook", new ImageIcon("./images/rook_dark.png"), new ImageIcon("./images/rook_light.png"), false, b.getSquare(1,1), b, rookMovement));
		//classic.whiteTeam.add(new Piece("Knight", new ImageIcon("./images/knight_dark.png"), new ImageIcon("./images/knight_light.png"), false, b.getSquare(1,2), b, knightMovement));
		//classic.whiteTeam.add(new Piece("Bishop", new ImageIcon("./images/bishop_dark.png"), new ImageIcon("./images/bishop_light.png"), false, b.getSquare(1,3), b, bishopMovement));
		//classic.whiteTeam.add(new Piece("Queen", new ImageIcon("./images/queen_dark.png"), new ImageIcon("./images/queen_light.png"), false, b.getSquare(1,4), b, queenMovement));
		//classic.whiteTeam.add(new Piece("King", new ImageIcon("./images/king_dark.png"), new ImageIcon("./images/king_light.png"), false, b.getSquare(1,5), b, kingMovement));
		//classic.whiteTeam.add(new Piece("Bishop", new ImageIcon("./images/bishop_dark.png"), new ImageIcon("./images/bishop_light.png"), false, b.getSquare(1,6), b, bishopMovement));
		//classic.whiteTeam.add(new Piece("Knight", new ImageIcon("./images/knight_dark.png"), new ImageIcon("./images/knight_light.png"), false, b.getSquare(1,7), b, knightMovement));
		//classic.whiteTeam.add(new Piece("Rook", new ImageIcon("./images/rook_dark.png"), new ImageIcon("./images/rook_light.png"), false, b.getSquare(1,8), b, rookMovement));

		//classic.blackTeam.add(new Rook(true, b.getSquare(8, 1), b));
		//classic.blackTeam.add(new Knight(true, b.getSquare(8, 2), b));
		//classic.blackTeam.add(new Bishop(true, b.getSquare(8, 3), b));
		//classic.blackTeam.add(new Queen(true, b.getSquare(8, 4), b));
		//classic.blackTeam.add(new Bishop(true, b.getSquare(8, 6), b));
		//classic.blackTeam.add(new Knight(true, b.getSquare(8, 7), b));
		//classic.blackTeam.add(new Rook(true, b.getSquare(8, 8), b));
		//classic.blackTeam.add(new King(true, b.getSquare(8, 5), b));
		classic.blackTeam.add(createRook(true,b.getSquare(8,1),b));
		classic.blackTeam.add(createKnight(true,b.getSquare(8,2),b));
		classic.blackTeam.add(createBishop(true,b.getSquare(8,3),b));
		classic.blackTeam.add(createQueen(true,b.getSquare(8,4),b));
		classic.blackTeam.add(createKing(true, b.getSquare(8,5), b));
		classic.blackTeam.add(createBishop(true,b.getSquare(8,6),b));
		classic.blackTeam.add(createKnight(true,b.getSquare(8, 7),b));
		classic.blackTeam.add(createRook(true,b.getSquare(8,8),b));
		//classic.blackTeam.add(new Piece("Rook", new ImageIcon("./images/rook_dark.png"), new ImageIcon("./images/rook_light.png"), true, b.getSquare(8,1), b, rookMovement));
		//classic.blackTeam.add(new Piece("Knight", new ImageIcon("./images/knight_dark.png"), new ImageIcon("./images/knight_light.png"), true, b.getSquare(8,2), b, knightMovement));
		//classic.blackTeam.add(new Piece("Bishop", new ImageIcon("./images/bishop_dark.png"), new ImageIcon("./images/bishop_light.png"), true, b.getSquare(8,3), b, bishopMovement));
		//classic.blackTeam.add(new Piece("Queen", new ImageIcon("./images/queen_dark.png"), new ImageIcon("./images/queen_light.png"), true, b.getSquare(8,4), b, queenMovement));
		//classic.blackTeam.add(new Piece("King", new ImageIcon("./images/king_dark.png"), new ImageIcon("./images/king_light.png"), true, b.getSquare(8,5), b, kingMovement));
		//classic.blackTeam.add(new Piece("Bishop", new ImageIcon("./images/bishop_dark.png"), new ImageIcon("./images/bishop_light.png"), true, b.getSquare(8,6), b, bishopMovement));
		//classic.blackTeam.add(new Piece("Knight", new ImageIcon("./images/knight_dark.png"), new ImageIcon("./images/knight_light.png"), true, b.getSquare(8,7), b, knightMovement));
		//classic.blackTeam.add(new Piece("Rook", new ImageIcon("./images/rook_dark.png"), new ImageIcon("./images/rook_light.png"), true, b.getSquare(8,8), b, rookMovement));

		classic.writeFile(new Rules(true), new Rules(true));
	}
	/**
	 * Create a piece that represents a bishop, without
	 * requiring a concrete bishop class.
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed bishop.
	 */
	public static Piece createBishop(boolean isBlack,Square square, Board board){
		HashMap<Character, Integer> bishopMovement = new HashMap<Character, Integer>();
		bishopMovement.put('R', -1);
		bishopMovement.put('r', -1);
		bishopMovement.put('L', -1);
		bishopMovement.put('l', -1);
		return new Piece("Bishop", new ImageIcon("./images/bishop_dark.png"), new ImageIcon("./images/bishop_light.png"), isBlack, square, board, bishopMovement);
	}
	/**
	 * Create a piece that represents a king, without
	 * requiring a concrete king class.
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed king.
	 */
	public static Piece createKing(boolean isBlack,Square square,Board board){
		HashMap<Character, Integer> kingMovement = new HashMap<Character, Integer>();
		kingMovement.put('N', 1);
		kingMovement.put('S', 1);
		kingMovement.put('E', 1);
		kingMovement.put('W', 1);
		kingMovement.put('R', 1);
		kingMovement.put('r', 1);
		kingMovement.put('L', 1);
		kingMovement.put('l', 1);
		return new Piece("King", new ImageIcon("./images/king_dark.png"), new ImageIcon("./images/king_light.png"), isBlack, square, board, kingMovement);
	}
	/**
	 * Create a piece that represents a knight, without
	 * requiring a concrete knight class.
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed knight.
	 */
	public static Piece createKnight(boolean isBlack,Square square, Board board){
		HashMap<Character, Integer> knightMovement = new HashMap<Character, Integer>();
		knightMovement.put('x', 1);
		knightMovement.put('y', 2);
		return new Piece("Knight", new ImageIcon("./images/knight_dark.png"), new ImageIcon("./images/knight_light.png"), isBlack, square, board, knightMovement);
	}
	/**
	 * Create a piece that represents a pawn, without
	 * requiring a concrete pawn class.
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed pawn.
	 */
	public static Piece createPawn(boolean isBlack,Square square,Board board){
		return new Piece("Pawn", new ImageIcon("./images/pawn_dark.png"), new ImageIcon("./images/pawn_light.png"), isBlack, square, board, null);
	}
	/**
	 * Create a piece that represents a queen, without
	 * requiring a concrete queen class.
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed queen.
	 */
	public static Piece createQueen(boolean isBlack,Square square,Board board){
		HashMap<Character, Integer> queenMovement = new HashMap<Character, Integer>();
		queenMovement.put('N', -1);
		queenMovement.put('S', -1);
		queenMovement.put('W', -1);
		queenMovement.put('E', -1);
		queenMovement.put('R', -1);
		queenMovement.put('r', -1);
		queenMovement.put('L', -1);
		queenMovement.put('l', -1);
		return new Piece("Queen", new ImageIcon("./images/queen_dark.png"), new ImageIcon("./images/queen_light.png"), isBlack, square, board, queenMovement);
	}
	/**
	 * Create a piece that represents a rook, without
	 * requiring a concrete rook class.
	 * @param isBlack Is this piece black?
	 * @param square What square does this piece start on?
	 * @param board The board the piece is on
	 * @return The constructed rook.
	 */
	public static Piece createRook(boolean isBlack,Square square,Board board){
		HashMap<Character, Integer> rookMovement = new HashMap<Character, Integer>();
		rookMovement.put('N', -1);
		rookMovement.put('S', -1);
		rookMovement.put('W', -1);
		rookMovement.put('E', -1);
		return new Piece("Rook", new ImageIcon("./images/rook_dark.png"), new ImageIcon("./images/rook_light.png"), isBlack, square, board, rookMovement);
	}
	/**
	 * Get the Set of names of Game types
	 * @return A Set containing the names of Game types
	 */
	public static String[] getArray() {
		File f = new File("variants");
		f.mkdir();
		String[] vars = f.list();
		for (String s : vars) {
			if (s.equals("Classic"))
				return vars;
		}
		buildClassic();
		return getArray();
	}

	/**
	 * Make a new instance of a Game type
	 * @param name The name of the Game type to return
	 * @return The created Game object
	 */
	public static Game newGame(String name) {
		String[] vars = new File("variants").list();
		for (String s : vars) {
			if (s.equals(name)) {
				try {
					ObjectInputStream in = new ObjectInputStream(
							new FileInputStream("variants/" + name));
					Builder b = (Builder) in.readObject();
					Game toReturn = new Game(name, b.boards, b.whiteRules,
							b.blackRules);
					toReturn.setWhiteTeam(b.whiteTeam);
					toReturn.setBlackTeam(b.blackTeam);

					return toReturn;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Getter method for the Board[]
	 * @return The Board[] of this type
	 */
	public Board[] getBoards() {
		return boards;
	}

	/**
	 * Setter method for the Board[]
	 * @param boards The Board[] to set
	 */
	public void setBoards(Board[] boards) {
		this.boards = boards;
	}

	/**
	 * Write the gameTypes array to disk
	 * @param blackRules Rules for the black team
	 * @param whiteRules Rules for the white team
	 */
	public void writeFile(Rules whiteRules, Rules blackRules) {
		try {
			new File("variants").mkdir();
			FileOutputStream f_out = new FileOutputStream(new File("variants/"
					+ name));
			ObjectOutputStream out = new ObjectOutputStream(f_out);
			out.writeObject(new Builder(name, boards, whiteTeam, blackTeam,
					whiteRules, blackRules));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
