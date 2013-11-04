package logic;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class PieceMovements implements Serializable
{
	public static final int UNLIMITED = -1;

	public static enum MovementDirection
	{
		NORTH('n'), SOUTH('s'), EAST('e'), WEST('w'), NORTHWEST('f'), NORTHEAST('g'), SOUTHWEST('a'), SOUTHEAST('d');

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

	public PieceMovements()
	{
		mMovements = Maps.newHashMap();
		mBidirectionalMovements = Sets.newHashSet();
	}

	public void addMovement(MovementDirection direction, int distance)
	{
		Preconditions.checkState(distance >= 0 || distance == UNLIMITED);

		mMovements.put(direction, Integer.valueOf(distance));
	}

	public void addBidirectionalMovement(BidirectionalMovement movement)
	{
		mBidirectionalMovements.add(movement);
	}

	// TODO: this shouldn't need to exist and definitely shouldn't be a public
	// method on the PieceMovements class
	public void clearBidirectionalMovements()
	{
		mBidirectionalMovements.clear();
	}

	public int getDistance(MovementDirection direction)
	{
		return mMovements.containsKey(direction) ? mMovements.get(direction) : 0;
	}

	public Set<BidirectionalMovement> getBidirectionalMovements()
	{
		return mBidirectionalMovements;
	}

	private static final long serialVersionUID = -7877544580471563255L;

	private final Map<MovementDirection, Integer> mMovements;
	private final Set<BidirectionalMovement> mBidirectionalMovements;
}
