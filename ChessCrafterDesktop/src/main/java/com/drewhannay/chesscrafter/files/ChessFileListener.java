package com.drewhannay.chesscrafter.files;

import org.jetbrains.annotations.NotNull;

public interface ChessFileListener {
    public void onPieceFileChanged(@NotNull String internalId);

    public void onPieceImageChanged(@NotNull String internalId);

    public void onGameConfigFileChanged();

    public void onSavedGameFileChanged();
}
