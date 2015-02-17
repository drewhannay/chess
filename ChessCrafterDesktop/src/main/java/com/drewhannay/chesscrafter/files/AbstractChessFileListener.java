package com.drewhannay.chesscrafter.files;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractChessFileListener implements ChessFileListener {

    @Override
    public void onPieceFileChanged(@NotNull String internalId) {
    }

    @Override
    public void onPieceImageChanged(@NotNull String internalId) {
    }

    @Override
    public void onGameConfigFileChanged() {
    }

    @Override
    public void onSavedGameFileChanged() {
    }
}
