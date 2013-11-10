package dragNdrop;

import gui.SquareJLabel;

import java.util.List;

import javax.swing.JComponent;

import com.google.common.collect.ImmutableList;

public class DropManager extends AbstractDropManager
{
	/*
	 * public void setBoard(BoardController board) { m_board = board; }
	 */

	@Override
	public void dropped(DropEvent event, boolean fromDisplayBoard)
	{
		SquareJLabel originSquareLabel = (SquareJLabel) event.getOriginComponent();
		SquareJLabel destinationSquareLabel = (SquareJLabel) isInTarget(event.getDropLocation());

		final List<JComponent> dummyList = ImmutableList.of();
		setComponentList(dummyList);

		if (destinationSquareLabel == null)
			return;

		try
		{
			// getGame().playMove(new MoveController(m_board,
			// originSquareLabel.getSquare(),
			// destinationSquareLabel.getSquare()));
			// sInstance.boardRefresh(getGame().getBoards());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}