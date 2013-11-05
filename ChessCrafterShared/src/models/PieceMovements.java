
package models;

import java.util.Map;
import java.util.Set;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public final class PieceMovements
{
	public static final int UNLIMITED = -1;

	public static enum MovementDirection
	{
		NORTH('n'),
		SOUTH('s'),
		EAST('e'),
		WEST('w'),
		NORTHWEST('f'),
		NORTHEAST('g'),
		SOUTHWEST('a'),
		SOUTHEAST('d');

		private MovementDirection(char direction)
		{
			mDirection = direction;
		}

		@Override
		public String toString()
		{
			return String.valueOf(mDirection);
		}

		private final char mDirection;
	}

	public PieceMovements(Map<MovementDirection, Integer> movements, Set<BidirectionalMovement> bidirectionalMovements)
	{
		mMovements = ImmutableMap.copyOf(movements);
		mBidirectionalMovements = ImmutableSet.copyOf(bidirectionalMovements);
	}

	public int getDistance(MovementDirection direction)
	{
		return mMovements.containsKey(direction) ? mMovements.get(direction) : 0;
	}

	public ImmutableSet<BidirectionalMovement> getBidirectionalMovements()
	{
		return mBidirectionalMovements;
	}

	private final ImmutableMap<MovementDirection, Integer> mMovements;
	private final ImmutableSet<BidirectionalMovement> mBidirectionalMovements;
}
