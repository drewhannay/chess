
package models;

public class PawnPieceType extends PieceType
{
	public PawnPieceType(PieceMovements pieceMovements, boolean isLeaper)
	{
		super("Pawn", pieceMovements, isLeaper);
		// TODO: eventually we should get rid of this class by making our system
		// generic enough to create a piece like a Pawn
	}
}
