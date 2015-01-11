
package dragNdrop;

import gui.PlayGamePanel;
import gui.SquareJLabel;
import java.util.List;
import javax.swing.JComponent;
import models.ChessCoordinates;
import models.Move;
import com.google.common.collect.ImmutableList;
import controllers.GameController;

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
		{
			PlayGamePanel.boardRefresh();
			return;
		}
			
		ChessCoordinates originCoordinates = originSquareLabel.getCoordinates();
		ChessCoordinates destinationCoordinates = destinationSquareLabel.getCoordinates();
		
		try
		{
			GameController.playMove(new Move(originCoordinates, destinationCoordinates));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		PlayGamePanel.boardRefresh();
	}
}