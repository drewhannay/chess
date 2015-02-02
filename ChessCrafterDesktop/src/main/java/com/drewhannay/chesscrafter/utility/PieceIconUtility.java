package com.drewhannay.chesscrafter.utility;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PieceIconUtility {

    private static final String TAG = "PieceIconUtility";
    private static final Map<String, Icon> ICON_CACHE = new HashMap<>();

    @Nullable
    public static Icon getPieceIcon(String pieceName, Color teamColor) {
        String key = getKey(pieceName, teamColor);
        if (ICON_CACHE.containsKey(key)) {
            return ICON_CACHE.get(key);
        }
        try {
            BufferedImage pieceImage = ImageUtility.getPieceImage(pieceName);
            long start = System.currentTimeMillis();
            BufferedImage mask = generateMask(pieceImage, teamColor, 0.5f);
            Log.d(TAG, "GenerateMask took:" + String.valueOf(System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
            BufferedImage tintedImage = tint(pieceImage, mask);
            Log.d(TAG, "Tint took:" + String.valueOf(System.currentTimeMillis() - start));
            Icon icon = new StretchIcon(tintedImage, true);
            ICON_CACHE.put(key, icon);
            return icon;
        } catch (IOException e) {
            Log.e(TAG, "Could not load image:" + pieceName);
            e.printStackTrace();
        }
        return null;
    }

    private static String getKey(String pieceName, Color teamColor) {
        return pieceName + String.valueOf(teamColor.getRGB());
    }

    private static BufferedImage generateMask(BufferedImage imgSource, Color color, float alpha) {
        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
        Graphics2D g2 = imgMask.createGraphics();
        applyQualityRenderingHints(g2);

        g2.drawImage(imgSource, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
        g2.setColor(color);

        g2.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
        g2.dispose();

        return imgMask;
    }

    private static BufferedImage tint(BufferedImage master, BufferedImage tint) {
        BufferedImage tinted = createCompatibleImage(master.getWidth(), master.getHeight(), Transparency.TRANSLUCENT);
        Graphics2D g2 = tinted.createGraphics();
        applyQualityRenderingHints(g2);
        g2.drawImage(master, 0, 0, null);
        g2.drawImage(tint, 0, 0, null);
        g2.dispose();

        return tinted;
    }

    private static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    private static BufferedImage createCompatibleImage(int width, int height, int transparency) {
        BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
        image.coerceData(true);
        return image;
    }

    private static GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    public static BufferedImage greyscaleImage(BufferedImage image) {
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
