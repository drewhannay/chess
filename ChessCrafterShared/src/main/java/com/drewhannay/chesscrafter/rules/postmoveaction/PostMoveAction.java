
package rules.postmoveaction;

import models.Move;

public abstract class PostMoveAction
{
	public abstract void perform(Move move);

	public abstract void undo(Move move);
}
