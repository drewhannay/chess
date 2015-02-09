package com.drewhannay.chesscrafter.dialog;

import com.drewhannay.chesscrafter.utility.FileUtility;

import javax.swing.JDialog;
import java.awt.Frame;

public class ChessDialog extends JDialog{

    public ChessDialog(){
        setIconImage(FileUtility.getFrontPageImage());
    }

    public ChessDialog(Frame owner, boolean modal) {
        super(owner, "", modal);
        setIconImage(FileUtility.getFrontPageImage());
    }
}
