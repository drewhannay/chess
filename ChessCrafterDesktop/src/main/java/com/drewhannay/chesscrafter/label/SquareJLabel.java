package com.drewhannay.chesscrafter.label;

import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;

import javax.swing.*;
import java.awt.*;

public class SquareJLabel extends JLabel {
    public static final Color HIGHLIGHT_COLOR = new Color(20, 129, 191);
    public static final Color THREAT_COLOR = new Color(120, 20, 20);

    private Color mBackgroundColor;

    private final BoardCoordinate mBoardCoordinate;
    private Piece mPiece;
    private int mImageScale;

    public SquareJLabel(BoardCoordinate coordinates, int imageScale) {
        mBoardCoordinate = coordinates;
        mImageScale = imageScale;
        setOpaque(true);
    }

    public BoardCoordinate getCoordinates() {
        return mBoardCoordinate;
    }

    public Color getColor() {
        return getBackground();
    }

    /**
     * Sets the background Color of the Square PERMANENTLY.
     * NOTE: Do not use this method for temporarily highlighting a square.
     * Use highlight() for that.
     *
     * @param c New Color
     */
    public void setBackgroundColor(Color c) {
        mBackgroundColor = c;
        setBackground(c);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    public void hideIcon() {
        setIcon(null);
    }

    public void setImageScale(int newImageScale) {
        mImageScale = newImageScale;
    }

    public int getImageScale() {
        return mImageScale;
    }

    public void setPiece(Piece piece) {
        mPiece = piece;
    }

    /**
     * Refresh the GUI's view of this Square with the current accurate
     * information.
     */
    public void refresh() {
        if (mPiece != null) {
            ImageIcon pieceIcon = PieceIconUtility.getPieceIcon(mPiece.getName(), mImageScale, mPiece.getTeamId());

            if (pieceIcon == null)
                setText(mPiece.getName());
            else {
                setIcon(pieceIcon);
            }

			/*
             * if (PreferenceUtility.getPreference().showPieceToolTips())
			 * setToolTipText(mPiece.getPieceType().getToolTipText()); else
			 * setToolTipText(null);
			 */
        } else {
            // if there's no Piece, clear the Icon, tooltip, and Text of the
            // Square
            setIcon(null);
            setText(""); //$NON-NLS-1$
            setToolTipText(null);
        }

        resetColor();
    }

    public Piece getPiece() {
        return mPiece;
    }

    /**
     * Reset temporary changes to the Color of the Square
     */
    public void resetColor() {
        if (mBackgroundColor != null) {
            // If a custom background color has been saved, use that.
            setBackground(mBackgroundColor);
            return;
        }
        setBorder(null);
        // Otherwise make our normal light/dark pattern.
        if ((mBoardCoordinate.y % 2 != 0 && mBoardCoordinate.x % 2 != 0)
                || (mBoardCoordinate.y % 2 == 0 && mBoardCoordinate.x % 2 == 0)) {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.getHSBColor(30, 70, 70));
        } else {
            setBackground(Color.getHSBColor(30, 70, 70));
            setForeground(Color.LIGHT_GRAY);
        }
    }

    public void highlightAsThreat() {
        setBackground(THREAT_COLOR);
    }

    public void highlight() {
        setBackground(HIGHLIGHT_COLOR);
    }

    @Override
    public String toString() {
        return getCoordinates().toString();
    }
}
