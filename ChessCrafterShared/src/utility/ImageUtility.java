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
		ImageIO.write(image, "PNG", new File(FileUtility.getImagePath(LIGHT_PREFIX + pieceName + PNG, false))); //$NON-NLS-1$
	}

	public static void writeDarkImage(String pieceName, BufferedImage image) throws Exception
	{
		if (image == null)
			throw new Exception();
		ImageIO.write(image, "PNG", new File(FileUtility.getImagePath(DARK_PREFIX + pieceName + PNG, false))); //$NON-NLS-1$
	}

	public static ImageIcon getLightImage(String pieceName) throws IOException
	{
		boolean isBuiltInFile = PIECE_NAMES.contains(pieceName);
		return GuiUtility.createImageIcon(48, 48, (isBuiltInFile ? "/" : "") + LIGHT_PREFIX + pieceName + PNG, isBuiltInFile); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static ImageIcon getDarkImage(String pieceName)
	{
		boolean isBuiltInFile = PIECE_NAMES.contains(pieceName);
		try
		{
			return GuiUtility.createImageIcon(48, 48, (isBuiltInFile ? "/" : "") + DARK_PREFIX + pieceName + PNG, isBuiltInFile); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static final String LIGHT_PREFIX = "l_"; //$NON-NLS-1$
	private static final String DARK_PREFIX = "d_"; //$NON-NLS-1$
	private static final String PNG = ".png"; //$NON-NLS-1$
	private static final List<String> PIECE_NAMES = Lists
			.newArrayList(
					Messages.getString("pawn"), Messages.getString("rook"), Messages.getString("bishop"), Messages.getString("knight"), Messages.getString("queen"), Messages.getString("king")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
}
