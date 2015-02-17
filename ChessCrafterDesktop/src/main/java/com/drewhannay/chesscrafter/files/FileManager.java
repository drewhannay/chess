package com.drewhannay.chesscrafter.files;

import com.drewhannay.chesscrafter.logic.PieceTypeManager;
import com.drewhannay.chesscrafter.models.History;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.utility.GsonUtility;
import com.drewhannay.chesscrafter.utility.JavaFxFileDialog;
import com.drewhannay.chesscrafter.utility.Log;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public enum FileManager {
    INSTANCE;

    public static final FileChooser.ExtensionFilter IMAGE_EXTENSION_FILTER = new FileChooser.ExtensionFilter("PNG", "*.png");
    public static final FileChooser.ExtensionFilter HISTORY_EXTENSION_FILTER = new FileChooser.ExtensionFilter("Saved Chess Game", "*.chesscrafter");
    public static final FileChooser.ExtensionFilter CONFIG_EXTENSION_FILTER = new FileChooser.ExtensionFilter("Crafted Game", "*.craftconfig");
    public static final FileChooser.ExtensionFilter PIECE_EXTENSION_FILTER = new FileChooser.ExtensionFilter("Piece", "*.piece");

    private static final String TAG = "FileUtility";

    private static final String SAVED_GAME_EXTENSION = ".chesscrafter";
    private static final String GAME_CRAFTER_EXTENSION = ".craftconfig";
    private static final String PIECE_EXTENSION = ".piece";

    private final List<ChessFileListener> mListeners;

    private boolean mInitialized;

    private File sImageDir;
    private File sGameConfigDir;
    private File sSavedGameDir;
    private File sPieceDir;

    private FileManager() {
        mListeners = new ArrayList<>();
    }

    public void init() throws IOException {
        String hiddenDir;

        if (System.getProperty("os.name").startsWith("Windows")) {
            hiddenDir = System.getProperty("user.home") + "\\chess";

            Runtime rt = Runtime.getRuntime();
            // try to make our folder hidden on Windows
            rt.exec("attrib +H " + System.getProperty("user.home") + "\\chess");
        } else {
            // if we're not on Windows, just add a period
            hiddenDir = System.getProperty("user.home") + "/.chess";
        }

        sSavedGameDir = new File(hiddenDir + File.separator + "SavedGames" + File.separator);
        sGameConfigDir = new File(hiddenDir + File.separator + "GameConfigs" + File.separator);
        sPieceDir = new File(hiddenDir + File.separator + "Pieces" + File.separator);
        sImageDir = new File(hiddenDir + File.separator + "Images" + File.separator);

        boolean allExist = Stream.of(sSavedGameDir, sGameConfigDir, sPieceDir, sImageDir)
                .allMatch(dir -> {
                    //noinspection ResultOfMethodCallIgnored
                    dir.mkdirs();
                    return dir.exists();
                });

        if (!allExist) {
            throw new IOException("Failed to create directory");
        }

        File[] pieceFiles = sPieceDir.listFiles();
        if (pieceFiles == null) {
            throw new IOException("Failed to list files in piece directory");
        }
        Stream.of(pieceFiles).forEach(file -> {
            PieceType pieceType = readPiece(file);
            if (pieceType != null) {
                PieceTypeManager.INSTANCE.registerPieceType(pieceType);
            } else {
                Log.e(TAG, "Couldn't register null PieceType for file:" + file.getPath());
            }
        });

        mInitialized = true;
    }

    private void verifyInitialized() {
        Preconditions.checkState(mInitialized, "Must call FileUtility.init()");
    }

    public void addChessFileListener(@NotNull ChessFileListener listener) {
        mListeners.add(listener);
    }

    public void removeChessFileListener(@NotNull ChessFileListener listener) {
        mListeners.remove(listener);
    }

    public boolean writeHistory(History history, String fileName) {
        verifyInitialized();

        boolean result = writeToFile(history, new File(sSavedGameDir, fileName + SAVED_GAME_EXTENSION));
        if (result) {
            mListeners.forEach(ChessFileListener::onSavedGameFileChanged);
        }
        return result;
    }

    public boolean writePiece(PieceType pieceType) {
        verifyInitialized();

        boolean result = writeToFile(pieceType, new File(sPieceDir, pieceType.getInternalId() + PIECE_EXTENSION));
        if (result) {
            if (PieceTypeManager.INSTANCE.hasPieceTypeWithId(pieceType.getInternalId())) {
                PieceTypeManager.INSTANCE.unregisterPieceType(pieceType.getInternalId());
                PieceIconUtility.invalidateCache(pieceType.getInternalId());
            }
            PieceTypeManager.INSTANCE.registerPieceType(pieceType);
            mListeners.forEach(listener -> listener.onPieceFileChanged(pieceType.getInternalId()));
        }
        return result;
    }

    public boolean deletePiece(PieceType pieceType) {
        verifyInitialized();

        File imageFile = new File(sImageDir, pieceType.getInternalId());
        if (imageFile.exists() && !imageFile.delete()) {
            Log.e(TAG, "Could not delete image for PieceType:" + pieceType.getInternalId());
        }

        boolean result = new File(sPieceDir, pieceType.getInternalId() + PIECE_EXTENSION).delete();
        if (result) {
            PieceTypeManager.INSTANCE.unregisterPieceType(pieceType.getInternalId());
            PieceIconUtility.invalidateCache(pieceType.getInternalId());
            mListeners.forEach(listener -> listener.onPieceFileChanged(pieceType.getInternalId()));
        } else {
            Log.e(TAG, "Could not delete PieceType:" + pieceType.getInternalId());
        }
        return result;
    }

    private boolean writeToFile(Object object, File file) {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            String json = GsonUtility.toJson(object);
            fileOut.write(json.getBytes());
            fileOut.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error writing file", e);
            return false;
        }
        return true;
    }

    @Nullable
    private PieceType readPiece(@NotNull File pieceFile) {
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(pieceFile));
            return GsonUtility.fromJson(jsonElement, PieceType.class);
        } catch (IOException e) {
            Log.e(TAG, "Could not read piece file:" + pieceFile.getPath());
            return null;
        }
    }

    public boolean writePieceImage(@NotNull String internalId, @NotNull BufferedImage image) {
        try {
            ImageIO.write(image, "png", new File(sImageDir, internalId));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public File readPieceImage(String imageName) {
        return new File(sImageDir, imageName);
    }

    @Nullable
    public File chooseFile(FileChooser.ExtensionFilter filter) {
        return JavaFxFileDialog.chooseFile(filter, sSavedGameDir);
    }

    @Nullable
    public File chooseDirectory() {
        return JavaFxFileDialog.chooseDirectory(sSavedGameDir);
    }
}
