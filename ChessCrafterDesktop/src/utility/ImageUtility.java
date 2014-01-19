
package utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import com.google.common.collect.Lists;

public final class ImageUtility
{
	public static void writeLightImage(String pieceName, BufferedImage image) throws Exception
	{
		if (image == null)
			throw new Exception();

		ImageIO.write(image, "PNG", new File(FileUtility.getImagePath(LIGHT_PREFIX + pieceName + PNG))); //$NON-NLS-1$
	}

	public static void writeDarkImage(String pieceName, BufferedImage image) throws Exception
	{
		if (image == null)
			throw new Exception();
		ImageIO.write(image, "PNG", new File(FileUtility.getImagePath(DARK_PREFIX + pieceName + PNG))); //$NON-NLS-1$
	}

	public static ImageIcon getLightImage(String pieceName, int imageScale) throws IOException
	{
		boolean isBuiltInFile = PIECE_NAMES.contains(pieceName);
		return GuiUtility.createImageIcon(imageScale, imageScale, (isBuiltInFile ? "/" : "") + LIGHT_PREFIX + pieceName + PNG, isBuiltInFile); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static ImageIcon getDarkImage(String pieceName, int imageScale) throws IOException
	{
		boolean isBuiltInFile = PIECE_NAMES.contains(pieceName);
		return GuiUtility.createImageIcon(imageScale, imageScale, (isBuiltInFile ? "/" : "") + DARK_PREFIX + pieceName + PNG, isBuiltInFile); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static final String LIGHT_PREFIX = "l_"; //$NON-NLS-1$
	private static final String DARK_PREFIX = "d_"; //$NON-NLS-1$
	private static final String PNG = ".png"; //$NON-NLS-1$
	private static final List<String> PIECE_NAMES =
			Lists
					.newArrayList(
							Messages.getString("Utility.pawn"), Messages.getString("Utility.rook"), Messages.getString("Utility.bishop"), Messages.getString("Utility.knight"), Messages.getString("Utility.queen"), Messages.getString("Utility.king")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
}
