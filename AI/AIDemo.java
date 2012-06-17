import java.util.List;

import ai.AIAdapter.AIBoard;
import ai.AIAdapter.AISquare;
import ai.AIPlugin;
import ai.FakeMove;

public class AIDemo implements AIPlugin
{

	@Override
	public FakeMove getMove(AIBoard[] boards)
	{
		for (int i = 1; i <= boards[0].maxRow(); i++)
		{
			for (int j = 1; j <= boards[0].maxCol(); j++)
			{
				if (boards[0].getSquare(i, j).getPiece() != null
						&& boards[0].getSquare(i, j).getPiece().isBlack())
				{
					List<AISquare> legalDests = boards[0].getSquare(i, j)
							.getPiece().getLegalDests();
					for (AISquare s : legalDests)
					{
						try
						{
							return new FakeMove(0, i, j, s.getRow(),
									s.getCol(), "Queen");
						} catch (Exception e)
						{
							continue;
						}

					}
				}

			}
		}

		return null;
	}

}
