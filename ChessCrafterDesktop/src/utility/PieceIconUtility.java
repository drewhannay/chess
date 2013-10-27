package utility;

import java.io.IOException;
import java.util.Map;

import javax.swing.ImageIcon;

import com.google.common.collect.Maps;

public final class PieceIconUtility
{
	public static ImageIcon getPieceIcon(String pieceName, boolean isDarkPiece)
	{
		Pair<ImageIcon, ImageIcon> pair = IMAGE_MAP.get(pieceName);
		if (pair == null)
		{
			try
			{
				pair = Pair.create(ImageUtility.getLightImage(pieceName), ImageUtility.getDarkImage(pieceName));
			}
			catch (IOException e)
			{
				System.out.println(e);
			}

			if (pair != null)
				IMAGE_MAP.put(pieceName, pair);
			else
				return null;
		}

		return isDarkPiece ? pair.second : pair.first;
	}

	private static final Map<String, Pair<ImageIcon, ImageIcon>> IMAGE_MAP = Maps.newHashMap();
}
