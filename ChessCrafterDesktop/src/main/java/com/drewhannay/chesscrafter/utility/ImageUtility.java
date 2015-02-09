package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.PieceType;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageUtility {

    private static final String TAG = "ImageUtility";

    @NotNull
    public static BufferedImage getPieceImage(@NotNull String internalId) {
        boolean isSystemFile = PieceTypeManager.INSTANCE.isSystemPiece(internalId);
        BufferedImage image;
        try {
            image = readImage(internalId, isSystemFile);
        } catch (IOException e) {
            Log.e(TAG, "No image for piece with id:" + internalId + ". Creating default image.");
            image = createDefaultPieceImage(internalId);
        }

        return image;
    }

    private static BufferedImage readImage(@NotNull String path, boolean isSystemFile) throws IOException {
        if (isSystemFile)
            return ImageIO.read(GuiUtility.class.getResourceAsStream("/" + path + ".png"));
        else
            return ImageIO.read(new File(FileUtility.getImagePath(path)));
    }

    @NotNull
    private static BufferedImage createDefaultPieceImage(String internalId) {
        PieceType pieceType = PieceTypeManager.INSTANCE.getPieceTypeById(internalId);

        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Font font = new Font(Font.MONOSPACED, Font.BOLD, 50);
        GlyphVector gv = font.createGlyphVector(graphics.getFontRenderContext(), new char[]{pieceType.getName().charAt(0)});
        Shape letterShape = gv.getGlyphOutline(0);

        graphics.translate(10, 43);
        graphics.setPaint(Color.WHITE);
        graphics.fill(letterShape);
        graphics.setPaint(Color.BLACK);
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(letterShape);

        graphics.dispose();

        return image;
    }
}
