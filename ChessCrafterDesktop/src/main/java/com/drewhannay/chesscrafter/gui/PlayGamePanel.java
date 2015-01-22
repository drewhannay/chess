package com.drewhannay.chesscrafter.gui;

import com.drewhannay.chesscrafter.controllers.GameController;
import com.drewhannay.chesscrafter.dragNdrop.DropManager;
import com.drewhannay.chesscrafter.dragNdrop.GlassPane;
import com.drewhannay.chesscrafter.logic.Result;
import com.drewhannay.chesscrafter.models.Board;
import com.drewhannay.chesscrafter.models.BoardCoordinate;
import com.drewhannay.chesscrafter.models.BoardSize;
import com.drewhannay.chesscrafter.models.Game;
import com.drewhannay.chesscrafter.models.MoveBuilder;
import com.drewhannay.chesscrafter.models.Piece;
import com.drewhannay.chesscrafter.models.PieceType;
import com.drewhannay.chesscrafter.models.Team;
import com.drewhannay.chesscrafter.timer.ChessTimer;
import com.drewhannay.chesscrafter.utility.AppConstants;
import com.drewhannay.chesscrafter.utility.GuiUtility;
import com.drewhannay.chesscrafter.utility.PieceIconUtility;
import com.drewhannay.chesscrafter.utility.PreferenceUtility;
import com.google.common.collect.Maps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
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
        initComponents();
    }

    private static void playPromotionMove(PieceType promotionPieceType, MoveBuilder moveBuilder) {
        moveBuilder.setPromotionType(promotionPieceType);
        changeTurns(GameController.playMove(moveBuilder.build()));
        boardRefresh();
    }

    public static void createPromotionPopup(Set<PieceType> promotionOptions, MoveBuilder moveBuilder) {
        JFrame promotionFrame = new JFrame();
        ChessPanel promotionPanel = new ChessPanel();
        JLabel promotionText = new JLabel("Choose a piece to promote to:");
        promotionText.setForeground(Color.white);
        promotionPanel.add(promotionText);
        for (PieceType pieceType : promotionOptions) {
            JButton label = new JButton(PieceIconUtility.getPieceIcon(pieceType.getName(), 48, mGame.getTurnKeeper().getActiveTeamId()));
            label.addActionListener(e -> {
                playPromotionMove(pieceType, moveBuilder);
                promotionFrame.dispose();
            });
            promotionPanel.add(label);
        }

        promotionFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        promotionFrame.setSize(200, 200);
        promotionFrame.add(promotionPanel);
        promotionFrame.setLocationRelativeTo(null);
        promotionFrame.setVisible(true);
    }

    public static void changeTurns(Result result) {
        if(result == Result.CHECKMATE) {
            endOfGame(result);
        }
        int teamID = mGame.getTurnKeeper().getActiveTeamId();
        if (result == Result.CHECK) {
            mTeamLabels.get(teamID).setBackground(Color.RED);
            mTeamLabels.get(teamID).setForeground(Color.WHITE);
            mTeamLabels.get(teamID).setText(mTeamNames.get(teamID) + " " + Messages.getString("PlayGamePanel.inCheck"));
        } else {
            mTeamLabels.get(teamID).setBackground(Color.CYAN);
            mTeamLabels.get(teamID).setForeground(Color.BLACK);
        }
        for (Map.Entry<Integer, JLabel> otherTeamLabel : mTeamLabels.entrySet()) {
            if (otherTeamLabel.getKey() != teamID) {
                otherTeamLabel.getValue().setBackground(Color.WHITE);
                otherTeamLabel.getValue().setForeground(Color.BLACK);
                otherTeamLabel.getValue().setText(mTeamNames.get(otherTeamLabel.getKey()));
            }
        }
    }

    public static void boardRefresh() {
        for (BoardPanel panel : mGameBoards) {
            panel.updatePieceLocations(mGame.getBoards());
            panel.refreshSquares();
        }

        for (BoardPanel panel : mJails) {
            panel.updateJailPopulation(mGame.getTeams());
            panel.refreshSquares();
        }
    }

     public static void endOfGame(Result result) {
         if (mGame.getHistory().size() == 0) { return;}
         else if (result == Result.STALEMATE) {
             JOptionPane.showMessageDialog(null, Messages.getString("PlayGamePanel.noMovesMade"), //$NON-NLS-1$
                     Messages.getString("PlayGamePanel.timeRanOut"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
             PreferenceUtility.clearTooltipListeners();
             Driver.getInstance().revertToMainPanel();
             Driver.getInstance().setFileMenuVisibility(true);
             return;
         }

         Object[] options = new String[] {
             Messages.getString("PlayGamePanel.saveRecord"),//$NON-NLS-1$
             Messages.getString("PlayGamePanel.newGame"), //$NON-NLS-2$
             Messages.getString("PlayGamePanel.mainMenu")  //$NON-NLS-3$
         };
         mOptionsMenu.setVisible(false);
         //TODO make this work with more than classic teams
         String winningTeamName = mTeamNames.get(1);
         if(mGame.getTurnKeeper().getActiveTeamId() == 1) {
             winningTeamName = mTeamNames.get(2);
         }
         String panelTitle = winningTeamName + " Wins!";
         String panelMessage = Messages.getString("PlayGamePanel.gameOver");
         if(result == Result.DRAW) {
             panelTitle = Messages.getString("PlayGamePanel.declareDraw");
             panelMessage = Messages.getString("PlayGamePanel.drawWhatNow");
         }

         switch(JOptionPane.showOptionDialog(Driver.getInstance(), panelMessage, panelTitle,
                 JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0])){
             case JOptionPane.YES_OPTION:
                 if (PreferenceUtility.getSaveLocationPreference().equals("default")) {
                     JOptionPane .showMessageDialog(Driver.getInstance(), Messages.getString("PlayGamePanel.sinceFirstTime") + AppConstants.APP_NAME //$NON-NLS-1$
                     + Messages.getString("PlayGamePanel.pleaseChooseDefault")/*$NON-NLS-1$*/ + Messages.getString("PlayGamePanel.pressingCancel"),
                     Messages.getString("PlayGamePanel.saveLocation"),
                     JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$ JFileChooser
                     JFileChooser fileChooser = new JFileChooser();
                     fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); int
                     returnVal = fileChooser.showOpenDialog(Driver.getInstance());
                     if(returnVal == JFileChooser.APPROVE_OPTION)
                         PreferenceUtility.setSaveLocationPreference(fileChooser.getSelectedFile().getAbsolutePath());
                 }
                 String saveFileName = JOptionPane.showInputDialog(Driver.getInstance(), Messages.getString("PlayGamePanel.enterAName"),
                         Messages.getString("PlayGamePanel.saving"), //$NON-NLS-1$ //$NON-NLS-2$
                         JOptionPane.PLAIN_MESSAGE);
                 //getGame().saveGame(saveFileName, getGame().getGameType().equals("Classic"), false);
                 //mGame.setBlackMove(false);
                 Driver.getInstance().setFileMenuVisibility(true);
                 PreferenceUtility.clearTooltipListeners();
                 Driver.getInstance().revertToMainPanel();
                 break;
             case JOptionPane.NO_OPTION:
                 //mGame.setBlackMove(false);
                 Driver.getInstance().setUpNewGame();
                 break;
             case JOptionPane.CANCEL_OPTION:
                 PreferenceUtility.clearTooltipListeners();
                 Driver.getInstance().revertToMainPanel();
                 Driver.getInstance().setFileMenuVisibility(true);
                 break;
         }
     }


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


         drawMenuItem.addActionListener(e -> {
             if(getGame().getHistory().size() == 0)
                 return;
             mOptionsMenu.setVisible(false);
             //getGame().getLastMove().setResult(result);
             endOfGame(Result.DRAW);
         });


        saveMenuItem.addActionListener(event -> {
//				mWhiteTimer.stopTimer();
//				mBlackTimer.stopTimer();
            saveGame();

            mOptionsMenu.setVisible(false);
            PreferenceUtility.clearTooltipListeners();
            Driver.getInstance().revertToMainPanel();
        });

        mOptionsMenu.add(drawMenuItem);
        mOptionsMenu.add(saveMenuItem);

        return mOptionsMenu;
    }

    private void initComponents() {
        JButton undoButton = new JButton(Messages.getString("PlayGamePanel.undo")); //$NON-NLS-1$
        undoButton.addActionListener(event -> {
            changeTurns(getGame().undoMove());
            boardRefresh();
        });

        twoBoardsGridBagOffset = 0;
        if (mOptionsMenu == null || !mOptionsMenu.isVisible())
            Driver.getInstance().setMenu(createMenuBar());

        Driver.getInstance().setOptionsMenuVisibility(true);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        Team[] teams = GameController.getGame().getTeams();
        mTeamLabels = Maps.newHashMapWithExpectedSize(teams.length);
        mTeamNames = Maps.newHashMapWithExpectedSize(teams.length);
        mJails = new BoardPanel[teams.length];

        Board[] boards = GameController.getGame().getBoards();
        mGameBoards = new BoardPanel[boards.length];
        // setBorder(BorderFactory.createLoweredBevelBorder());

        int gridx = 0;

        for (int boardIndex = 0; boardIndex < boards.length; boardIndex++) {
            constraints.gridheight = boards[boardIndex].getBoardSize().height + 2; // Added 2 for row labels? I dunno.
            constraints.gridy = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = new Insets(10, 0, 0, 0);
            constraints.gridx = gridx;
            constraints.gridwidth = boards[boardIndex].getBoardSize().width + 2; // Added 2 for row labels?
            gridx += constraints.gridwidth;

            mGameBoards[boardIndex] =
                    new BoardPanel(boards[boardIndex].getBoardSize(), boardIndex, 0, mGlobalGlassPane, mDropManager, false);
            add(mGameBoards[boardIndex], constraints);
        }

        for (Team team : mGame.getTeams()) {
            JLabel teamLabel = GuiUtility.createJLabel("");
            teamLabel.setHorizontalAlignment(SwingConstants.CENTER);
            teamLabel.setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$
            teamLabel.setOpaque(true);
            mTeamLabels.put(team.getTeamId(), teamLabel);
            if (GameController.getGame().getGameType().equals("Classic")) {
                if (team.getTeamId() == 1)
                    mTeamNames.put(team.getTeamId(), Messages.getString("PlayGamePanel.whiteTeam"));
                else
                    mTeamNames.put(team.getTeamId(), Messages.getString("PlayGamePanel.blackTeam"));
            } else
                mTeamNames.put(team.getTeamId(), "Team " + team.getTeamId());
        }

        int jailBoardSize = getJailDimension();

        for (int teamIndex = 0; teamIndex < mGame.getTeams().length; teamIndex++) {
            mJails[teamIndex] = new BoardPanel(BoardSize.withDimensions(jailBoardSize, jailBoardSize), 0, teamIndex, mGlobalGlassPane, mDropManager, true);
            mJails[teamIndex].setBorder(GuiUtility.createBorder(Messages.getString("PlayGamePanel.capturedPieces"))); //$NON-NLS-1$
            mJails[teamIndex].setLayout(new GridLayout(jailBoardSize, jailBoardSize));
            mJails[teamIndex].setPreferredSize(new Dimension((jailBoardSize + 1) * 25, (jailBoardSize + 1) * 25));
        }

        //Add the Black Team Label
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 0, 10, 0);
        mTeamLabels.get(2).setText("Black Team");
        add(mTeamLabels.get(2), constraints);

        // add the Black timer
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 2;
        constraints.insets = new Insets(0, 25, 10, 25);
        // TODO: This currently assumes a two-person game.
//		if (!ChessTimer.isNoTimer(mTimers[1]))
//			add(new ChessTimerLabel(mTimers[1]), constraints);

        // add the Black Jail
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 3;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 3;
        add(mJails[1], constraints);

        // adds the undo button
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 6;
        add(undoButton, constraints);

        // adds the White Jail
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 3;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 7;
        add(mJails[0], constraints);

        // adds the White timer
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 6;
        constraints.insets = new Insets(0, 0, 0, 0);

        //Add the White Team Label
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.BASELINE;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 11 + twoBoardsGridBagOffset;
        constraints.gridy = 10;
        constraints.insets = new Insets(10, 0, 10, 0);
        mTeamLabels.get(1).setText("White Team");
        add(mTeamLabels.get(1), constraints);

        // TODO: This assumes a two-player game
//		add(new ChessTimerLabel(mTimers[0]), constraints);
//		resetTimers();
        boardRefresh();
        changeTurns(Result.CONTINUE);
        Driver.getInstance().pack();
    }

    private int getJailDimension() {
        return 4;
    }

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

    public static List<SquareJLabel> highlightLegalDestinations(int boardIndex, BoardCoordinate coordinates) {
        Piece movingPiece = GameController.getGame().getPiece(boardIndex, coordinates);
        if (movingPiece != null && PreferenceUtility.getHighlightMovesPreference()) {
            Set<BoardCoordinate> legalDestinations = GameController.getLegalDestinations(boardIndex, coordinates);
            List<SquareJLabel> labels = mGameBoards[boardIndex].highlightSquares(legalDestinations);
            mGameBoards[boardIndex].repaint();
            return labels;
        } else
            return null;
    }

    private static final long serialVersionUID = -2507232401817253688L;

    protected int twoBoardsGridBagOffset;
    protected static Game mGame;
    protected static ChessTimer[] mTimers;
    protected static Map<Integer, JLabel> mTeamLabels;
    protected static Map<Integer, String> mTeamNames;
    protected static BoardPanel[] mGameBoards;
    protected static BoardPanel[] mJails;
    protected static JMenu mOptionsMenu;
    private final DropManager mDropManager;
    protected GlassPane mGlobalGlassPane;
}
