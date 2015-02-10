package com.drewhannay.chesscrafter.files;

public interface ChessFileListener {
    public void onPieceFileChanged();

    public void onGameConfigFileChanged();

    public void onSavedGameFileChanged();
}
