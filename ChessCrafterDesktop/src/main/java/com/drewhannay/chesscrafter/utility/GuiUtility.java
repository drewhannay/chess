
package utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import models.BidirectionalMovement;
import models.Piece;
import models.PieceMovements;
import models.PieceMovements.MovementDirection;

public final class GuiUtility
{
	public static void requestFocus(final JComponent component)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				component.requestFocus();
			}
		});
	}

	public static JLabel createJLabel(String labelText)
	{
		JLabel newLabel = new JLabel(labelText);
		newLabel.setForeground(Color.white);
		return newLabel;
	}

	public static JLabel createJLabel(ImageIcon image)
	{
		JLabel newLabel = new JLabel(image);
		newLabel.setForeground(Color.white);
		return newLabel;
	}

	public static TitledBorder createBorder(String borderText)
	{
		TitledBorder newBorder = BorderFactory.createTitledBorder(borderText);
		newBorder.setTitleColor(Color.white);
		return newBorder;
	}

	public static void setupDoneButton(JButton doneButton, final JFrame popup)
	{
		doneButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				popup.dispose();
			}
		});
	}

	public static void setupVariantCancelButton(JButton cancelButton, final JPanel displayPanel, final JFrame popup)
	{
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				displayPanel.removeAll();
				popup.setVisible(false);
			}
		});
	}

	public static ImageIcon createImageIcon(int imageWidth, int imageHeight, String imageLocation) throws IOException
	{
		return createImageIcon(imageWidth, imageHeight, imageLocation, true);
	}

	public static BufferedImage createBufferedImage(int width, int height, String name) throws IOException
	{
		BufferedImage bufferedImage = ImageIO.read(new File(FileUtility.getImagePath(name)));
		return bufferedImage;
	}

	public static ImageIcon createImageIcon(int imageWidth, int imageHeight, String imageLocation, boolean isBuiltInFile)
			throws IOException
	{
		BufferedImage bufferedImage;
		if (isBuiltInFile)
			bufferedImage = ImageIO.read(GuiUtility.class.getResource(imageLocation));
		else
			bufferedImage = ImageIO.read(new File(FileUtility.getImagePath(imageLocation)));

		ImageIcon imageIcon = new ImageIcon(bufferedImage);
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
		return imageIcon;
	}

	public static boolean tryAIFileInstall(Component parent)
	{
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(parent);
		File file = fileChooser.getSelectedFile();

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if (!file.renameTo(FileUtility.getAIFile(file.getName())))
			{
				JOptionPane.showMessageDialog(parent, Messages.getString("fileNotInstalledSuccessfully"), Messages.getString("error"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.PLAIN_MESSAGE);
			}
			else
			{
				return true;
			}
		}

		return false;
	}

	public static String getPieceToolTipText(Piece piece)
	{
		String name = piece.getPieceType().getName();
		PieceMovements movements = piece.getPieceType().getPieceMovements();

		StringBuilder builder = new StringBuilder("<html><b>"); //$NON-NLS-1$
		builder.append(name);
		builder.append("</b><br/>"); //$NON-NLS-1$

		builder.append("<table><tr>"); //$NON-NLS-1$
		builder.append("<td>"); //$NON-NLS-1$
		builder.append("<table border=\"1\"> <tr> <td align=\"center\">"); //$NON-NLS-1$
		builder.append(directionToTooltip(movements.getDistance(MovementDirection.NORTHWEST)));
		builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
		builder.append(directionToTooltip(movements.getDistance(MovementDirection.NORTH)));
		builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
		builder.append(directionToTooltip(movements.getDistance(MovementDirection.NORTHEAST)));
		builder.append("</td></tr>"); //$NON-NLS-1$

		builder.append("<tr> <td align=\"center\">"); //$NON-NLS-1$
		builder.append(directionToTooltip(movements.getDistance(MovementDirection.WEST)));
		builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
		builder.append(name.equals(Messages.getString("Piece.knight")) ? Messages.getString("Piece.knightChar") : name.charAt(0)); //$NON-NLS-1$ //$NON-NLS-2$
		builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
		builder.append(directionToTooltip(movements.getDistance(MovementDirection.EAST)));
		builder.append("</td></tr>"); //$NON-NLS-1$

		builder.append("<tr> <td align=\"center\">"); //$NON-NLS-1$
		builder.append(directionToTooltip(movements.getDistance(MovementDirection.SOUTHWEST)));
		builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
		builder.append(directionToTooltip(movements.getDistance(MovementDirection.SOUTH)));
		builder.append("</td><td align=\"center\">"); //$NON-NLS-1$
		builder.append(directionToTooltip(movements.getDistance(MovementDirection.SOUTHEAST)));
		builder.append("</td></tr>"); //$NON-NLS-1$

		builder.append("</table>"); //$NON-NLS-1$

		builder.append("</td>"); //$NON-NLS-1$

		builder.append("<td>"); //$NON-NLS-1$
		if (piece.getPieceType().isLeaper())
			builder.append(Messages.getString("Piece.ableToLeapBr")); //$NON-NLS-1$
		else
			builder.append(Messages.getString("Piece.notAbleToLeapBr")); //$NON-NLS-1$

		for (BidirectionalMovement movement : movements.getBidirectionalMovements())
		{
			builder.append("- "); //$NON-NLS-1$
			builder.append(movement.getRowDistance());
			builder.append(" x "); //$NON-NLS-1$
			builder.append(movement.getColumnDistance());
			builder.append("<br/>"); //$NON-NLS-1$
		}

		builder.append("</td>"); //$NON-NLS-1$

		builder.append("</html>"); //$NON-NLS-1$
		return builder.toString();
	}

	private static String directionToTooltip(Integer direction)
	{
		if (direction == PieceMovements.UNLIMITED)
			return "&infin;"; //$NON-NLS-1$
		else
			return direction.toString();
	}
}
