package gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class ChessPanel extends JPanel
{
	public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g.create();

        Paint p = new GradientPaint(
                0, 0, new Color(21, 0, 255, 100),
                0, getHeight(), new Color(0x272B39)
            );
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setPaint(p);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.dispose();
    }
	
	private static final long serialVersionUID = 2904391800353371870L;
}
