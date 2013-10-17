package rules;

import java.util.List;

import logic.Piece;
import logic.Square;

import com.google.common.collect.Lists;

public enum AdjustTeamLegalDestinations
{
	CLASSIC, MUST_CAPTURE;

	public void adjustDestinations(List<Piece> team)
	{
		switch (this)
		{
		case MUST_CAPTURE:
			mustCapture(team);
			break;
		case CLASSIC:
		default:
			break;
		}
	}

	/**
	 * Adjust the available destinations if the player has chosen to play a
	 * variant where captures are mandatory.
	 * 
	 * @param team The team to adjust.
	 */
	private void mustCapture(List<Piece> team)
	{
		boolean foundCapture = false;
		for (int i = 0; i < team.size(); i++)
		{
			Piece current = team.get(i);
			for (Square s : current.getLegalDests())
			{
				if (s.isOccupied())
				{
					foundCapture = true;
					break;
				}
			}

			if (foundCapture)
				break;
		}
		if (foundCapture)
		{
			for (int i = 0; i < team.size(); i++)
			{
				Piece current = team.get(i);
				List<Square> adjusted = Lists.newArrayList();
				for (Square s : current.getLegalDests())
				{
					if (s.isOccupied())
						adjusted.add(s);
				}
				current.setLegalDests(adjusted);
			}
		}
	}
}
