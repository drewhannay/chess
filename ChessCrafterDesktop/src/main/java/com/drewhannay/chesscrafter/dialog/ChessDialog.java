package com.drewhannay.chesscrafter.dialog;

import com.drewhannay.chesscrafter.utility.FileUtility;

import javax.swing.JDialog;
import java.awt.Dialog;
import java.awt.Frame;

public class ChessDialog extends JDialog{

    public ChessDialog(boolean modal){
        this(modal, null);
    }

    public ChessDialog(boolean modal, Frame owner) {
        super(owner);
        if(modal) {
            setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        }
        setIconImage(FileUtility.getFrontPageImage());
    }
}
