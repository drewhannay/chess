package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.models.Piece;
import com.google.common.collect.Maps;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

public final class PieceIconUtility {
    public static final Map<String, Color> pieceColorMap;

    static {
        pieceColorMap = Maps.newHashMap();
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


    public static ImageIcon getPieceIcon(String pieceName, int imageScale, int teamId) {
        String pieceKey = pieceName + imageScale;
        Map<Integer, ImageIcon> list = IMAGE_MAP.get(pieceKey);
        if (list == null) {
            try {
                list = Maps.newHashMap();
//				for (Iterator<Color> colorIterator = pieceColorMap.values().iterator(); colorIterator.hasNext(); )
//				{
//					//TODO: basic black/white setup. Soon enough we should be able to take a grayscale image and filter it for each team
//					list.add(ImageUtility.getImage(pieceName, colorIterator.next(), imageScale));
//				}
                list.put(Piece.TEAM_ONE, ImageUtility.getLightImage(pieceName, imageScale));
                list.put(Piece.TEAM_TWO, ImageUtility.getDarkImage(pieceName, imageScale));
            } catch (IOException e) {
                System.out.println(e);
            }
            IMAGE_MAP.put(pieceKey, list);
        }
        return list.get(teamId);
    }

    private static final Map<String, Map<Integer, ImageIcon>> IMAGE_MAP = Maps.newHashMap();
}
