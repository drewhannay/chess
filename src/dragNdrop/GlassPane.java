package dragNdrop;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.JPanel;

public class GlassPane extends JPanel implements Serializable
{
	public GlassPane()
	{
		setOpaque(false);
		mAlphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
		mLocation = new Point(0, 0);
	}

	public void setImage(BufferedImage draggedImage)
	{
		mDraggedImage = draggedImage;
	}

	public void setPoint(Point location)
	{
		mLocation = location;
	}

	public void paintComponent(Graphics graphics)
	{
		if (mDraggedImage == null)
			return;

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setComposite(mAlphaComposite);
		graphics2D.drawImage(mDraggedImage, (int) (mLocation.getX() - (mDraggedImage.getWidth(this) / 2)),
				(int) (mLocation.getY() - (mDraggedImage.getHeight(this) / 2)), null);
	}

	private static final long serialVersionUID = 214303662751768477L;

	private final AlphaComposite mAlphaComposite;

	private BufferedImage mDraggedImage;
	private Point mLocation;
}
