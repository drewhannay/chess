package utility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;

import com.google.common.collect.Lists;

public interface ImageUtility
{
	public void writeLightImage(String pieceName, BufferedImage image) throws Exception;

	public void writeDarkImage(String pieceName, BufferedImage image) throws Exception;

	public ImageIcon getLightImage(String pieceName) throws IOException;

	public ImageIcon getDarkImage(String pieceName) throws IOException;

	final String LIGHT_PREFIX = "l_"; //$NON-NLS-1$
	final String DARK_PREFIX = "d_"; //$NON-NLS-1$
	final String PNG = ".png"; //$NON-NLS-1$
	final List<String> PIECE_NAMES = Lists
			.newArrayList(
					Messages.getString("pawn"), Messages.getString("rook"), Messages.getString("bishop"), Messages.getString("knight"), Messages.getString("queen"), Messages.getString("king")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
}
