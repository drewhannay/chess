package com.drewhannay.chesscrafter.label;

import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class SquareJLabel extends JLabel {
    private static final Color HIGHLIGHT_COLOR = new Color(20, 129, 191);
    private static final Color THREAT_COLOR = new Color(120, 20, 20);

    private final BoardCoordinate mBoardCoordinate;
    private final Color mBackgroundColor;
    private final Color mForegroundColor;

    public SquareJLabel(BoardCoordinate coordinate) {
        this(coordinate, null, null);
    }

    public SquareJLabel(BoardCoordinate coordinate, @Nullable Color backgroundColor, @Nullable Color foregroundColor) {
        mBoardCoordinate = coordinate;
        mBackgroundColor = backgroundColor != null ? backgroundColor : getDefaultBackgroundColor();
        mForegroundColor = foregroundColor != null ? foregroundColor : getDefaultForegroundColor();

        setBackground(mBackgroundColor);
        setForeground(mForegroundColor);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        setOpaque(true);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(24, 24);
    }

    @Override
    public Dimension getPreferredSize() {
        Container c = getParent();
        if (c == null) {
            return getMinimumSize();
        }

        Dimension d = c.getSize();
        int w = (int) d.getWidth();
        int h = (int) d.getHeight();
        int s = (w < h ? w : h);
        return new Dimension(s, s);
    }

    public BoardCoordinate getCoordinates() {
        return mBoardCoordinate;
    }

    public void hideIcon() {
        setIcon(null);
    }

    public void setPiece(@Nullable Piece piece) {
        if (piece != null) {
            Icon icon = PieceIconUtility.getPieceIcon(piece.getName(), piece.getTeamId());
            if (icon != null) {
                setIcon(icon);
            } else {
                setText(piece.getName());
            }
        } else {
            setIcon(null);
            setText(null);
            setToolTipText(null);
        }
        clearHighlight();
    }

    public void highlightThreat() {
        setBackground(THREAT_COLOR);
    }

    public void highlight() {
        setBackground(HIGHLIGHT_COLOR);
    }

    public void clearHighlight() {
        setBackground(mBackgroundColor);
        setForeground(mForegroundColor);
    }

    private Color getDefaultBackgroundColor() {
        return mBoardCoordinate.x % 2 == mBoardCoordinate.y % 2
                ? Color.LIGHT_GRAY : Color.getHSBColor(30, 70, 70);
    }

    private Color getDefaultForegroundColor() {
        return mBoardCoordinate.x % 2 == mBoardCoordinate.y % 2
                ? Color.getHSBColor(30, 70, 70) : Color.LIGHT_GRAY;
    }

    @Override
    public String toString() {
        return getCoordinates().toString();
    }
}
