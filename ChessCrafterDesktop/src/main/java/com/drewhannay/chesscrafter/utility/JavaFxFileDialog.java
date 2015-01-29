package com.drewhannay.chesscrafter.utility;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.function.Supplier;

public final class JavaFxFileDialog {
    static {
        // Prevent automatic JavaFX launcher thread shutdown after last JavaFX window is closed
        // Probably not necessary for the FileChooser situation, but better safe than sorry
        Platform.setImplicitExit(false);
    }

    private static final Object LOCK = new Object();

    /**
     * Does not only check if JavaFX is still ready for use but also initializes the JavaFX Toolkit, if that hasn't
     * happened yet.
     *
     * @return True if JavaFX stuff can be used right after this method call. False if the JavaFX launcher thread has
     * already shut down.
     */
    private static boolean isJavaFXStillUsable() {
        try {
            // initialize the Toolkit required by JavaFX, as stated in the docs of Platform.runLater()
            new JFXPanel();
        } catch (IllegalStateException ise) {
            return false;
        }
        return true;
    }

    public static File chooseDirectory() {
        return choose(JavaFxFileDialog::doChooseDirectory);
    }

    public static File chooseFile(FileChooser.ExtensionFilter extensionFilter) {
        return choose(() -> doChooseFile(extensionFilter));
    }

    private static File doChooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(null);
    }

    private static File doChooseFile(FileChooser.ExtensionFilter extensionFilter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showOpenDialog(null);
    }

    private static File choose(Supplier<File> fileSupplier) {
        // necessary, or the LOCK.wait() will never end
        if (!isJavaFXStillUsable()) {
            System.err.println("Problem in chooseFile(): JavaFX launcher thread has already shut down, can't use JavaFX");
            return null;
        }

        synchronized (LOCK) {
            // dirty hack to evade usage of a field
            File[] chosenFile = new File[1];
            boolean[] keepWaiting = new boolean[1];
            keepWaiting[0] = true;

            Platform.runLater(() -> {
                synchronized (LOCK) {
                    chosenFile[0] = fileSupplier.get();
                    keepWaiting[0] = false;
                    LOCK.notifyAll();
                }
            });

            // wait for runLater to complete
            do {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (keepWaiting[0]);

            return chosenFile[0];
        }
    }
}
