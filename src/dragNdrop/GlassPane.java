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
		m_alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
		m_location = new Point(0, 0);
	}

	public void setImage(BufferedImage draggedImage)
	{
		m_draggedImage = draggedImage;
	}

	public void setPoint(Point location)
	{
		m_location = location;
	}

	public void paintComponent(Graphics graphics)
	{
		if (m_draggedImage == null)
			return;

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setComposite(m_alphaComposite);
		graphics2D.drawImage(m_draggedImage, (int) (m_location.getX() - (m_draggedImage.getWidth(this) / 2)),
				(int) (m_location.getY() - (m_draggedImage.getHeight(this) / 2)), null);
	}

	private static final long serialVersionUID = 214303662751768477L;

	private final AlphaComposite m_alphaComposite;

	private BufferedImage m_draggedImage = null;
	private Point m_location;
}
