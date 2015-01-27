package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.utility.AppConstants;

import javax.swing.*;
import java.awt.*;

public class ChessFrame extends JFrame {
    public ChessFrame() {
        setTitle(AppConstants.APP_NAME);
        setLayout(new BorderLayout());
        setResizable(true);

        // put the window in the center of the screen, regardless of resolution
        setLocationRelativeTo(null);

        setJMenuBar(ChessActions.createJMenuBar());
        setSize(685, 450);

        initComponents();

        setVisible(true);
    }

    void initComponents() {
    }
}
