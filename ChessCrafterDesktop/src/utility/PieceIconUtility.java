
package utility;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class PieceIconUtility
{
	public static final Map<String, Color> pieceColorMap;
	
	static
	{
		pieceColorMap= Maps.newHashMap();
		pieceColorMap.put("White", Color.WHITE);
		pieceColorMap.put("Black", Color.BLACK);
		pieceColorMap.put("Red", Color.RED);
		pieceColorMap.put("Blue", Color.BLUE);
		pieceColorMap.put("Yellow", Color.YELLOW);
		pieceColorMap.put("Green", Color.GREEN);
		pieceColorMap.put("Orange", Color.ORANGE);
		pieceColorMap.put("Cyan", Color.CYAN);
		pieceColorMap.put("Magenta", Color.MAGENTA);
		pieceColorMap.put("Gray", Color.GRAY);
			
	}
	
	
	public static ImageIcon getPieceIcon(String pieceName, int imageScale, int teamId)
	{
		List<ImageIcon> list = IMAGE_MAP.get(pieceName);
		if (list == null)
		{
			try
			{
				list = Lists.newArrayList();
//				for (Iterator<Color> colorIterator = pieceColorMap.values().iterator(); colorIterator.hasNext(); )
//				{
//					//TODO: basic black/white setup. Soon enough we should be able to take a grayscale image and filter it for each team
//					list.add(ImageUtility.getImage(pieceName, colorIterator.next(), imageScale));
//				}
				list.add(ImageUtility.getLightImage(pieceName, imageScale));
				list.add(ImageUtility.getDarkImage(pieceName, imageScale));
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
			IMAGE_MAP.put(pieceName, list);
		}
		return list.get(teamId);
	}

	private static final Map<String, List<ImageIcon>> IMAGE_MAP = Maps.newHashMap();
}
