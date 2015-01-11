package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.controllers.GameController;
import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.models.*;
import com.drewhannay.chesscrafter.timer.ChessTimer;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PlayGamePanel extends ChessPanel {
    public PlayGamePanel() {
        if (GameController.getGame() != null)
            setGame(GameController.getGame());
        else
            System.out.println("Didn't set Game in GameController. Things may go wonky...");

        mDropManager = new DropManager();
        mGlobalGlassPane = new GlassPane();
        mGlobalGlassPane.setOpaque(false);
        Driver.getInstance().setGlassPane(mGlobalGlassPane);

        // PlayGamePanel.mWhiteTimer = game.getWhiteTimer();
        // PlayGamePanel.mBlackTimer = game.getBlackTimer();
        // mWhiteTimer.restart();
        // mBlackTimer.restart();
        // turn(game.isBlackMove());
        try {
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//	protected void resetTurnLabels()
//	{
//		mTeamLabels[currentTeamIndex].setVisible(true);
//		mTeamLabels[currentTeamIndex].setBackground(Color.red);
//
//		for (JLabel label : mTeamLabels)
//			remove(label);
//		GridBagConstraints constraints = new GridBagConstraints();
//
//		if (isBlackTeam)
//		{
//			constraints.gridy = 0;
//			resetLabel.setForeground(mTeamIndex == 1 ? Color.white : Color.black);
//			resetLabel.setBackground(mTeamIndex == 1 ? SquareJLabel.HIGHLIGHT_COLOR :
//					null);
//			if (inCheck)
//				resetLabel.setText(Messages.getString("PlayGamePanel.blackTeam") + " " + Messages.getString("PlayGamePanel.inCheck")); //$NON-NLS-2$
//			else
//				resetLabel.setText(Messages.getString("PlayGamePanel.blackTeam")); //$NON-NLS-1$
//		}
//		else
//		{
//			// change spacing if there is a timer
//			// TODO: This assumes a two-player game
//			constraints.gridy = ChessTimer.isNoTimer(mTimers[0]) ? 9 : 10;
//			if (inCheck)
//				resetLabel.setText(Messages.getString("PlayGamePanel.whiteTeam") + " " + Messages.getString("PlayGamePanel.inCheck")); //$NON-NLS-2$
//			else
//				resetLabel.setText(Messages.getString("PlayGamePanel.whiteTeam")); //$NON-NLS-1$
//			resetLabel.setForeground(mTeamIndex == 0 ? Color.white : Color.black);
//			resetLabel.setBackground(mTeamIndex == 0 ? SquareJLabel.HIGHLIGHT_COLOR :
//					null);
//		}
//		if (inCheck)
//		{
//			resetLabel.setForeground(Color.white);
//			resetLabel.setBackground(Color.red);
//			constraints.ipadx = 50;
//		}
//		else
//		{
//			constraints.ipadx = 100;
//		}
//		constraints.gridwidth = 3;
//		constraints.insets = new Insets(10, 0, 10, 0);
//		constraints.gridx = 11 + twoBoardsGridBagOffset;
//		add(resetLabel, constraints);
//	}

    public static void boardRefresh() {
        for (BoardPanel panel : mGameBoards)
            panel.refreshSquares();

        // refreshGameSquares();

        // Team[] teams = getGame().getTeams();
        //
        // int currentTeamIndex = getGame().getTurnKeeper().getCurrentTeamIndex();
        //
        // // TODO: This may need to adapt based on the number of players or
        // // whatnot.
        // int jailDimension = 5;
        //
        // for (int teamIndex = 0; teamIndex < teams.length; teamIndex++)
        // {
        // Team currentTeam = teams[teamIndex];
        // List<Piece> capturedPieces = currentTeam.getCapturedPieces();
        // if (capturedPieces == null)
        // capturedPieces = Lists.newArrayList();
        //
        // int capturedPieceIndex = 0;
        //
        // mJails[teamIndex] = new BoardPanel(jailDimension, jailDimension, true);
        // mJails[teamIndex].createJailGrid(jailDimension, jailDimension);
        //
        // for (int rowIndex = 1; rowIndex <= jailDimension && capturedPieceIndex < capturedPieces.size(); rowIndex++)
        // {
        // for (int columnIndex = 1; columnIndex <= jailDimension && capturedPieceIndex < capturedPieces.size(); columnIndex++)
        // {
        // mJails[teamIndex].updateSquareLabel(new ChessCoordinates(rowIndex, columnIndex, teamIndex),
        // capturedPieces.get(capturedPieceIndex));
        // capturedPieceIndex++;
        // }
        // }
        //
        // }
        //
        // List<Piece> currentObjectivePieces = teams[currentTeamIndex].getNonCapturedObjectivePieces();
        //
        // if (currentObjectivePieces != null && teams[currentTeamIndex].objectivePieceIsInCheck(currentTeamIndex))
        // {
        // // resetTurnLabels();
        //
        // // for (Piece piece : getGame().getThreats(currentObjectivePieces))
        // // getGame().getBoards()[piece.getCoordinates().boardIndex].getSquare(piece.getCoordinates().row,
        // // piece.getCoordinates().column).setIsThreatSquare(true);
        // }
        // else
        // {
        // // resetTurnLabels();
        // }
    }

	/*
     * public void endOfGame(Result result) { if (mGame.getHistory().size() !=
	 * 0) { } else if (result != Result.DRAW) {
	 * JOptionPane.showMessageDialog(null,
	 * Messages.getString("PlayGamePanel.noMovesMade"), //$NON-NLS-1$
	 * Messages.getString("PlayGamePanel.timeRanOut"),
	 * JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
	 * PreferenceUtility.clearTooltipListeners();
	 * Driver.getInstance().revertToMainPanel();
	 * Driver.getInstance().setFileMenuVisibility(true); return; }
	 * 
	 * Object[] options = new String[] {
	 * Messages.getString("PlayGamePanel.saveRecord"),
	 * Messages.getString("PlayGamePanel.newGame"),
	 * Messages.getString("PlayGamePanel.quit") }; //$NON-NLS-1$ //$NON-NLS-2$
	 * //$NON-NLS-3$ mOptionsMenu.setVisible(false); switch
	 * (JOptionPane.showOptionDialog(Driver.getInstance(), result.getGuiText(),
	 * result.winText(), JOptionPane.YES_NO_CANCEL_OPTION,
	 * JOptionPane.PLAIN_MESSAGE, null, options, options[0])) { case
	 * JOptionPane.YES_OPTION:
	 * 
	 * mPreference = PreferenceUtility.getPreference();
	 * 
	 * if (!mPreference.isPathSet()) { JOptionPane .showMessageDialog(
	 * Driver.getInstance(), Messages.getString("PlayGamePanel.sinceFirstTime")
	 * + AppConstants.APP_NAME //$NON-NLS-1$ +
	 * Messages.getString("PlayGamePanel.pleaseChooseDefault") //$NON-NLS-1$ +
	 * Messages.getString("PlayGamePanel.pressingCancel"),
	 * Messages.getString("PlayGamePanel.saveLocation"),
	 * JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$ JFileChooser
	 * fileChooser = new JFileChooser();
	 * fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); int
	 * returnVal = fileChooser.showOpenDialog(Driver.getInstance()); if
	 * (returnVal == JFileChooser.APPROVE_OPTION)
	 * mPreference.setSaveLocation(fileChooser
	 * .getSelectedFile().getAbsolutePath()); else
	 * mPreference.setSaveLocation(FileUtility.getDefaultCompletedLocation()); }
	 * 
	 * String saveFileName = JOptionPane.showInputDialog(Driver.getInstance(),
	 * Messages.getString("PlayGamePanel.enterAName"),
	 * Messages.getString("PlayGamePanel.saving"), //$NON-NLS-1$ //$NON-NLS-2$
	 * JOptionPane.PLAIN_MESSAGE); getGame().saveGame(saveFileName,
	 * getGame().isClassicChess(), false); mGame.setBlackMove(false);
	 * Driver.getInstance().setFileMenuVisibility(true);
	 * PreferenceUtility.clearTooltipListeners();
	 * Driver.getInstance().revertToMainPanel(); break; case
	 * JOptionPane.NO_OPTION: mGame.setBlackMove(false);
	 * Driver.getInstance().setUpNewGame(); break; case
	 * JOptionPane.CANCEL_OPTION: mGame.setBlackMove(false); System.exit(0);
	 * break; } }
	 */

    public void saveGame() {
        String fileName = JOptionPane.showInputDialog(Driver.getInstance(),
                Messages.getString("PlayGamePanel.enterAName"), Messages.getString("PlayGamePanel.saving"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
        if (fileName == null)
            return;
        // getGame().saveGame(fileName, false, true);
    }

//	public void turn(boolean isBlackTurn)
//	{
//		if (mWhiteTimer != null && mBlackTimer != null)
//		{
//			(!isBlackTurn ? mWhiteTimer : mBlackTimer).startTimer();
//			(isBlackTurn ? mWhiteTimer : mBlackTimer).stopTimer();
//		}
//	}

    public JMenu createMenuBar() {
        mOptionsMenu = new JMenu(Messages.getString("PlayGamePanel.menu")); //$NON-NLS-1$

        JMenuItem drawMenuItem = new JMenuItem(Messages.getString("PlayGamePanel.declareDraw"), KeyEvent.VK_D); //$NON-NLS-1$
        JMenuItem saveMenuItem = new JMenuItem(Messages.getString("PlayGamePanel.saveAndQuit"), KeyEvent.VK_S); //$NON-NLS-1$

		/*
         * drawMenuItem.addActionListener(new ActionListener() {
		 * @Override public void actionPerformed(ActionEvent event) { if
		 * (getGame().getLastMove() == null) return;
		 * mOptionsMenu.setVisible(false); Result result = Result.DRAW;
		 * result.setGuiText(Messages.getString("PlayGamePanel.drawWhatNow"));
		 * //$NON-NLS-1$ getGame().getLastMove().setResult(result);
		 * endOfGame(result); } });
		 */

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
//				mWhiteTimer.stopTimer();
//				mBlackTimer.stopTimer();
                saveGame();

                mOptionsMenu.setVisible(false);
                PreferenceUtility.clearTooltipListeners();
                Driver.getInstance().revertToMainPanel();
            }
        });

        mOptionsMenu.add(drawMenuItem);
        mOptionsMenu.add(saveMenuItem);

        return mOptionsMenu;
    }

    private void initComponents() throws Exception {
        /*
         * JButton undoButton = new
		 * JButton(Messages.getString("PlayGamePanel.undo")); //$NON-NLS-1$
		 * undoButton.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent event) { if
		 * (getGame().getHistory().size() == 0) return;
		 * 
		 * try { getGame().getHistory().get(getGame().getHistory().size() -
		 * 1).undo(); } catch (Exception e) { e.printStackTrace(); }
		 * 
		 * getGame().getHistory().remove(getGame().getHistory().size() - 1);
		 * (getGame().isBlackMove() ? getGame().getBlackRules() :
		 * getGame().getWhiteRules()).undoEndOfGame();
		 * boardRefresh(getGame().getBoards()); } });
		 */

        twoBoardsGridBagOffset = 0;
        if (mOptionsMenu == null || !mOptionsMenu.isVisible())
            Driver.getInstance().setMenu(createMenuBar());

        Driver.getInstance().setOptionsMenuVisibility(true);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        Team[] teams = GameController.getGame().getTeams();
        mTeamLabels = new JLabel[teams.length];
        mJails = new BoardPanel[teams.length];

        Board[] boards = GameController.getGame().getBoards();
        mGameBoards = new BoardPanel[boards.length];
        // setBorder(BorderFactory.createLoweredBevelBorder());

        int gridx = 0;

        for (int boardIndex = 0; boardIndex < boards.length; boardIndex++) {
            constraints.gridheight = boards[boardIndex].getRowCount() + 2; // Added 2 for row labels? I dunno.
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = new Insets(10, 0, 0, 0);
            constraints.gridx = gridx;
            constraints.gridwidth = boards[boardIndex].getColumnCount() + 2; // Added 2 for row labels?
            gridx += constraints.gridwidth;

            mGameBoards[boardIndex] =
                    new BoardPanel(boards[boardIndex].getRowCount(), boards[boardIndex].getColumnCount(), boardIndex, mGlobalGlassPane, mDropManager, false);
            add(mGameBoards[boardIndex], constraints);
        }

        Iterator<String> colorIterator = PieceIconUtility.pieceColorMap.keySet().iterator();
        for (int teamIndex = 0; teamIndex < mGame.getTeams().length; teamIndex++) {
            String colorString = colorIterator.next();
            mTeamLabels[teamIndex] = GuiUtility.createJLabel(colorString);
            mTeamLabels[teamIndex].setHorizontalAlignment(SwingConstants.CENTER);
            mTeamLabels[teamIndex].setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$
            mTeamLabels[teamIndex].setOpaque(true);
            mTeamLabels[teamIndex].setVisible(true);
        }

        int jailBoardSize = getJailDimension();

        // mWhiteCapturesJail = new BoardController(jailBoardSize,
        // jailBoardSize, false);
        // mWhiteCapturePanel = createGrid(mWhiteCapturesJail, true).first;

        for (int teamIndex = 0; teamIndex < mGame.getTeams().length; teamIndex++) {
            mJails[teamIndex] = new BoardPanel(jailBoardSize, jailBoardSize, teamIndex, mGlobalGlassPane, mDropManager, true);
            mJails[teamIndex].setBorder(GuiUtility.createBorder(Messages.getString("PlayGamePanel.capturedPieces"))); //$NON-NLS-1$
            mJails[teamIndex].setLayout(new GridLayout(jailBoardSize, jailBoardSize));

            mJails[teamIndex].setPreferredSize(new Dimension((jailBoardSize + 1) * 25, (jailBoardSize + 1) * 25));
        }

        // add the Black timer
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.ipadx = 100;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 25, 10, 25);
        // TODO: This currently assumes a two-person game.
//		if (!ChessTimer.isNoTimer(mTimers[1]))
//			add(new ChessTimerLabel(mTimers[1]), constraints);

        // add the Black Jail
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 3;
        constraints.ipadx = 0;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 2;
        add(mJails[1], constraints);

        // adds the undo button
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.ipadx = 100;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 5;
        // add(undoButton, constraints);

        // adds the White Jail
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 3;
        constraints.ipadx = 0;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 6;
        add(mJails[0], constraints);

        // adds the White timer
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.ipadx = 100;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 9;
        constraints.insets = new Insets(0, 0, 0, 0);
        // TODO: This assumes a two-player game
//		add(new ChessTimerLabel(mTimers[0]), constraints);
//		resetTimers();
        Driver.getInstance().pack();
    }

    private int getJailDimension() {
        double size = 0;
        Team[] teams = mGame.getTeams();

        for (int i = 0; i < teams.length; i++) {
            if (teams[i].getTotalTeamSize() > size)
                size = teams[i].getTotalTeamSize();
        }

        return Math.max((int) Math.ceil(Math.sqrt(size)), 4);
    }

    public void setNextMoveMustPlacePiece(boolean nextMoveMustPlacePiece) {
        mNextMoveMustPlacePiece = nextMoveMustPlacePiece;
    }

    public boolean getNextMoveMustPlacePiece() {
        return mNextMoveMustPlacePiece;
    }

	/*
     * public void setPieceToPlace(PieceController piece) { mPieceToPlace =
	 * piece; }
	 */

    public static void setGame(Game game) {
        mGame = game;
    }

    public static Game getGame() {
        return mGame;
    }

    public static void resetTimers() {
        for (ChessTimer timer : mTimers)
            timer.reset();
    }

    public static List<SquareJLabel> highlightLegalDestinations(ChessCoordinates coordinates) {
        Piece movingPiece = GameController.getGame().getPieceOnSquare(coordinates);
        if (movingPiece != null && PreferenceUtility.getPreference().isHighlightMoves()) {
            Set<ChessCoordinates> legalDestinations = GameController.getLegalDestinations(movingPiece);
            List<SquareJLabel> labels = mGameBoards[coordinates.boardIndex].highlightSquares(legalDestinations);
            mGameBoards[coordinates.boardIndex].repaint();
            return labels;
        } else
            return null;
    }

    private static final long serialVersionUID = -2507232401817253688L;

    protected static boolean mNextMoveMustPlacePiece;
    protected int twoBoardsGridBagOffset;
    protected static Game mGame;
    protected static ChessTimer[] mTimers;
    protected static JLabel[] mTeamLabels;
    protected static BoardPanel[] mGameBoards;
    protected static BoardPanel[] mJails;
    protected static JMenu mOptionsMenu;
    private final DropManager mDropManager;
    protected GlassPane mGlobalGlassPane;
}
