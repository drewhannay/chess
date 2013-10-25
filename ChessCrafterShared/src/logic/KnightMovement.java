package logic;

import java.io.Serializable;

public class KnightMovement implements Serializable
{
	private static final long serialVersionUID = -7878954893313555613L;

	private int knightOne;
	private int knightTwo;

	public KnightMovement(int knightOne, int knightTwo)
	{
		this.knightOne = knightOne;
		this.knightTwo = knightTwo;
	}

	public int getKnightOne()
	{
		return knightOne;
	}

	public int getKnightTwo()
	{
		return knightTwo;
	}

	@Override
	public String toString()
	{
		return knightOne + " x " + knightTwo; //$NON-NLS-1$
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof KnightMovement)
		{
			KnightMovement otherK = (KnightMovement) other;
			return (otherK.getKnightOne() == knightOne && otherK.getKnightTwo() == knightTwo)
					|| (otherK.getKnightOne() == knightTwo && otherK.getKnightTwo() == knightOne);
		}
		return false;
	}
}
