package com.drewhannay.chesscrafter.utility;

import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public final class FileUtility {
    public static final FileChooser.ExtensionFilter IMAGE_EXTENSION_FILTER = new FileChooser.ExtensionFilter("PNG", "*.png");
    public static final FileChooser.ExtensionFilter HISTORY_EXTENSION_FILTER = new FileChooser.ExtensionFilter("Saved Chess Game", "*.chesscrafter");
    public static final FileChooser.ExtensionFilter CONFIG_EXTENSION_FILTER = new FileChooser.ExtensionFilter("Crafted Game", "*.craftconfig");
    public static final FileChooser.ExtensionFilter PIECE_EXTENSION_FILTER = new FileChooser.ExtensionFilter("Piece", "*.piece");

    private static final String TAG = "FileUtility";

    private static final String HIDDEN_DIR;
    private static final String IMAGES = "Images";
    private static final String VARIANTS = "Crafted_Games";
    private static final String PIECES = "Pieces";
    private static final String SAVED_GAMES = "Saved_Games";
    private static final String SAVED_GAME_EXTENSION = ".chesscrafter";
    private static final String GAME_CRAFTER_EXTENSION = ".craftconfig";
    private static final String PIECE_EXTENSION = ".piece";

    private static final String SLASH;

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

    public static File getGameFile(String gameFileName) {
        String path = PreferenceUtility.getSaveLocationPreference();
        new File(path).mkdirs();
        return new File(path + SLASH + gameFileName + SAVED_GAME_EXTENSION);
    }

    public static String getDefaultCompletedLocation() {
        String path = HIDDEN_DIR + SLASH + SAVED_GAMES;
        new File(path).mkdirs();
        return path;
    }

    @Nullable
    public static File chooseFile(FileChooser.ExtensionFilter filter) {
        return JavaFxFileDialog.chooseFile(filter, new File(PreferenceUtility.getSaveLocationPreference()));
    }

    @Nullable
    public static File chooseDirectory() {
        return JavaFxFileDialog.chooseDirectory(new File(PreferenceUtility.getSaveLocationPreference()));
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
            Log.e(TAG, "Can't find path:" + path, e);
        }
        return frontPage;
    }

    public static void deletePiece(String pieceName) {
        File pieceFile = getPieceFile(pieceName);
        pieceFile.delete();
        new File((getImagePath("l_" + pieceName + ".png"))).delete();
        new File((getImagePath("d_" + pieceName + ".png"))).delete();
    }
}
