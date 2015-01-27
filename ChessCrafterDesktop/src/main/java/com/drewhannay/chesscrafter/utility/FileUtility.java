package com.drewhannay.chesscrafter.utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public final class FileUtility {
    public static String getImagePath(String imageName) {
        File file = new File(HIDDEN_DIR + SLASH + IMAGES);
        file.mkdirs();
        return HIDDEN_DIR + SLASH + IMAGES + SLASH + imageName;
    }

    public static String[] getVariantsFileArray() {
        File file = new File(HIDDEN_DIR + SLASH + VARIANTS);
        file.mkdirs();
        return file.list();
    }

    public static String[] getCustomPieceArray() {
        File file = new File(HIDDEN_DIR + SLASH + PIECES);
        file.mkdirs();
        return file.list();
    }

    public static File getVariantsFile(String variantName) {
        return new File(HIDDEN_DIR + SLASH + VARIANTS + SLASH + variantName);
    }

    public static File getPieceFile(String pieceName) {
        return new File(HIDDEN_DIR + SLASH + PIECES + SLASH + pieceName);
    }

    public static String[] getGamesInProgressFileArray() {
        File file = new File(HIDDEN_DIR + SLASH + GAMES_IN_PROGRESS);
        file.mkdirs();
        return file.list();
    }

    public static File getGamesInProgressFile(String gameFileName) {
        String path = HIDDEN_DIR + SLASH + GAMES_IN_PROGRESS;
        new File(path).mkdirs();
        return new File(path + SLASH + gameFileName);
    }

    public static String[] getCompletedGamesFileArray() {
        File file = new File(HIDDEN_DIR + SLASH + COMPLETED_GAMES);
        file.mkdirs();
        return file.list();
    }

    public static File getCompletedGamesFile(String completedGameFileName) {
        String path = HIDDEN_DIR + SLASH + COMPLETED_GAMES;
        new File(path).mkdirs();
        return new File(path + SLASH + completedGameFileName);
    }

    public static String getDefaultCompletedLocation() {
        String path = HIDDEN_DIR + SLASH + COMPLETED_GAMES;
        new File(path).mkdirs();
        return path;
    }

    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            HIDDEN_DIR = System.getProperty("user.home") + "\\chess";
            SLASH = "\\";

            try {
                Runtime rt = Runtime.getRuntime();
                // try to make our folder hidden on Windows
                rt.exec("attrib +H " + System.getProperty("user.home") + "\\chess");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            // if we're not on Windows, just add a period
            HIDDEN_DIR = System.getProperty("user.home") + "/.chess";
            SLASH = "/";
        }
    }

    public static BufferedImage getFrontPageImage() {
        BufferedImage frontPage = null;
        String path = "/chess_logo.png";
        try {
            URL resource = FileUtility.class.getResource(path);
            frontPage = ImageIO.read(resource);
        } catch (IOException e) {
            System.out.println("Can't find path:" + path);
            e.printStackTrace();
        }
        return frontPage;
    }

    public static void deletePiece(String pieceName) {
        File pieceFile = getPieceFile(pieceName);
        pieceFile.delete();
        new File((getImagePath("l_" + pieceName + ".png"))).delete();
        new File((getImagePath("d_" + pieceName + ".png"))).delete();
    }

    private static final String HIDDEN_DIR;
    private static final String IMAGES = "images";
    private static final String VARIANTS = "variants";
    private static final String PIECES = "pieces";
    private static final String GAMES_IN_PROGRESS = "gamesInProgress";
    private static final String COMPLETED_GAMES = "completedGames";

    private static final String SLASH;
}
