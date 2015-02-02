package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageUtility {
    public static BufferedImage getPieceImage(@NotNull String pieceName) throws IOException {
        boolean isSystemFile = PieceTypeManager.INSTANCE.isSystemPiece(pieceName);
        return readImage(pieceName, isSystemFile);
    }

    private static BufferedImage readImage(@NotNull String path, boolean isSystemFile) throws IOException {
        if (isSystemFile)
            return ImageIO.read(GuiUtility.class.getResourceAsStream("/" + path + ".png"));
        else
            return ImageIO.read(new File(FileUtility.getImagePath(path)));
    }
}
