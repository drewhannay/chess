package com.drewhannay.chesscrafter.files;

import org.jetbrains.annotations.NotNull;

public interface ChessFileListener {
    void onPieceFileChanged(@NotNull String internalId);

    void onPieceImageChanged(@NotNull String internalId);

    void onGameConfigFileChanged();

    void onSavedGameFileChanged();
}
