package com.drewhannay.chesscrafter.utility;

import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class ImageUtility {
    public static void writeLightImage(String pieceName, BufferedImage image) throws Exception {
        if (image == null)
            throw new Exception();

        ImageIO.write(image, "PNG", new File(FileUtility.getImagePath(LIGHT_PREFIX + pieceName + PNG)));
    }

    public static void writeDarkImage(String pieceName, BufferedImage image) throws Exception {
        if (image == null)
            throw new Exception();
        ImageIO.write(image, "PNG", new File(FileUtility.getImagePath(DARK_PREFIX + pieceName + PNG)));
    }

    public static ImageIcon getLightImage(String pieceName) throws IOException {
        boolean isSystemFile = PIECE_NAMES.contains(pieceName);
        return GuiUtility.createImageIcon((isSystemFile ? "/" : "") + LIGHT_PREFIX + pieceName + PNG, isSystemFile);
    }

    public static ImageIcon getDarkImage(String pieceName) throws IOException {
        boolean isSystemFile = PIECE_NAMES.contains(pieceName);
        return GuiUtility.createImageIcon((isSystemFile ? "/" : "") + DARK_PREFIX + pieceName + PNG, isSystemFile);
    }

    private static final String LIGHT_PREFIX = "l_";
    private static final String DARK_PREFIX = "d_";
    private static final String PNG = ".png";
    private static final List<String> PIECE_NAMES = Lists.newArrayList(
            Messages.getString("Utility.northFacingPawn"),
            Messages.getString("Utility.southFacingPawn"),
            Messages.getString("Utility.rook"),
            Messages.getString("Utility.bishop"),
            Messages.getString("Utility.knight"),
            Messages.getString("Utility.queen"),
            Messages.getString("Utility.king"));
}
