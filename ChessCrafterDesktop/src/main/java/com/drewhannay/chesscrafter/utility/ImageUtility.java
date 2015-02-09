package com.drewhannay.chesscrafter.utility;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.PieceType;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.IOException;

public final class ImageUtility {

    private static final String TAG = "ImageUtility";

    @NotNull
    public static StretchIcon createStretchIcon(@NotNull BufferedImage image) {
        return new StretchIcon(image, true);
    }

    @NotNull
    public static BufferedImage readSystemImage(String name) {
        try {
            return readImage(name, true);
        } catch (IOException e) {
            Log.wtf(TAG, "Cannot find image:" + name, e);
            return null;
        }
    }

    public static boolean writePieceImage(@NotNull String internalId, @NotNull BufferedImage image) {
        return FileUtility.writePieceImage(internalId, greyscaleImage(image));
    }

    @NotNull
    static BufferedImage getPieceImage(@NotNull String internalId) {
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

    @NotNull
    private static BufferedImage readImage(@NotNull String path, boolean isSystemFile) throws IOException {
        if (isSystemFile)
            return ImageIO.read(GuiUtility.class.getResourceAsStream("/" + path + ".png"));
        else
            return ImageIO.read(FileUtility.readPieceImage(path));
    }

    @NotNull
    private static BufferedImage createDefaultPieceImage(@NotNull String internalId) {
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

    @NotNull
    private static BufferedImage greyscaleImage(@NotNull BufferedImage image) {
        ColorSpace gsColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ComponentColorModel ccm = new ComponentColorModel(gsColorSpace, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        WritableRaster raster = ccm.createCompatibleWritableRaster(image.getWidth(), image.getHeight());

        BufferedImage greyscaleImage = new BufferedImage(ccm, raster, ccm.isAlphaPremultiplied(), null);

        Graphics g = greyscaleImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return greyscaleImage;
    }
}
