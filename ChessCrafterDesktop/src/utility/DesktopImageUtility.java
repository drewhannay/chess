
package utility;

import gui.Driver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public final class DesktopImageUtility implements ImageUtility
{
	public void writeLightImage(String pieceName, BufferedImage image) throws Exception
	{
		if (image == null)
			throw new Exception();

		ImageIO.write(image, "PNG", new File(Driver.getFileUtility().getImagePath(LIGHT_PREFIX + pieceName + PNG))); //$NON-NLS-1$
	}

	public void writeDarkImage(String pieceName, BufferedImage image) throws Exception
	{
		if (image == null)
			throw new Exception();
		ImageIO.write(image, "PNG", new File(Driver.getFileUtility().getImagePath(DARK_PREFIX + pieceName + PNG))); //$NON-NLS-1$
	}

	public ImageIcon getLightImage(String pieceName) throws IOException
	{
		boolean isBuiltInFile = PIECE_NAMES.contains(pieceName);
		return GuiUtility.createImageIcon(48, 48, (isBuiltInFile ? "/" : "") + LIGHT_PREFIX + pieceName + PNG, isBuiltInFile); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public ImageIcon getDarkImage(String pieceName) throws IOException
	{
		boolean isBuiltInFile = PIECE_NAMES.contains(pieceName);
		return GuiUtility.createImageIcon(48, 48, (isBuiltInFile ? "/" : "") + DARK_PREFIX + pieceName + PNG, isBuiltInFile); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
