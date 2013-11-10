package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.Board;
import models.Team;
import timer.ChessTimer;
import utility.GuiUtility;

import controllers.GameController;
import dragNdrop.GlassPane;
import dragNdrop.DropManager;

public class PlayGamePanel extends ChessPanel
{
	public PlayGamePanel(GameController game)
	{
		if (game != null)
			setGame(game);
		else
			game = getGame();

		mTeamIndex = 0;
		mDropManager = new DropManager();
		mGlobalGlassPane = new GlassPane();
		mGlobalGlassPane.setOpaque(false);
		Driver.getInstance().setGlassPane(mGlobalGlassPane);

		// PlayGamePanel.mWhiteTimer = game.getWhiteTimer();
		// PlayGamePanel.mBlackTimer = game.getBlackTimer();
		// mWhiteTimer.restart();
		// mBlackTimer.restart();
		// turn(game.isBlackMove());
		try
		{
			initComponents();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		sInstance = this;
		// boardRefresh(game.getBoards());
	}

	protected void resetTurnLabels(JLabel resetLabel, boolean isBlackTeam, boolean inCheck)
	{
		remove(resetLabel);
		GridBagConstraints constraints = new GridBagConstraints();

		if (isBlackTeam)
		{
			constraints.gridy = 0;
			resetLabel.setForeground(mTeamIndex == 1 ? Color.white : Color.black);
			resetLabel.setBackground(mTeamIndex == 1 ? SquareJLabel.HIGHLIGHT_COLOR : null);
			if (inCheck)
				resetLabel.setText(Messages.getString("PlayGamePanel.blackTeam") + " " + Messages.getString("PlayGamePanel.inCheck")); //$NON-NLS-2$
			else
				resetLabel.setText(Messages.getString("PlayGamePanel.blackTeam")); //$NON-NLS-1$
		}
		else
		{
			// change spacing if there is a timer
			constraints.gridy = ChessTimer.isNoTimer(mWhiteTimer) ? 9 : 10;
			if (inCheck)
				resetLabel.setText(Messages.getString("PlayGamePanel.whiteTeam") + " " + Messages.getString("PlayGamePanel.inCheck")); //$NON-NLS-2$
			else
				resetLabel.setText(Messages.getString("PlayGamePanel.whiteTeam")); //$NON-NLS-1$
			resetLabel.setForeground(mTeamIndex == 0 ? Color.white : Color.black);
			resetLabel.setBackground(mTeamIndex == 0 ? SquareJLabel.HIGHLIGHT_COLOR : null);
		}
		if (inCheck)
		{
			resetLabel.setForeground(Color.white);
			resetLabel.setBackground(Color.red);
			constraints.ipadx = 50;
		}
		else
		{
			constraints.ipadx = 100;
		}
		constraints.gridwidth = 3;
		constraints.insets = new Insets(10, 0, 10, 0);
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		add(resetLabel, constraints);
	}

	/*
	 * public void boardRefresh() { //refreshSquares(boards);
	 * 
	 * PieceController objectivePiece = getGame().isBlackMove() ?
	 * getGame().getBlackRules().objectivePiece(true) :
	 * getGame().getWhiteRules() .objectivePiece(false);
	 * 
	 * int index = 0; PieceController[] blackCapturedPieces =
	 * getGame().getCapturedPieces(true); for (int i =
	 * mWhiteCapturesJail.getMaxRow(); i >= 1; i--) { for (int j = 1; j <=
	 * mWhiteCapturesJail.getMaxCol(); j++) { if (blackCapturedPieces != null &&
	 * index < blackCapturedPieces.length) { mWhiteCapturesJail.getSquare(i,
	 * j).setPiece(blackCapturedPieces[index]); index++; }
	 * mWhiteCapturesJail.getSquare(i, j).setJailStateChanged(); } }
	 * 
	 * index = 0; PieceController[] whiteCapturedPieces =
	 * getGame().getCapturedPieces(false); for (int i =
	 * mBlackCapturesJail.getMaxRow(); i >= 1; i--) { for (int j = 1; j <=
	 * mBlackCapturesJail.getMaxCol(); j++) { if (whiteCapturedPieces != null &&
	 * index < whiteCapturedPieces.length) { mBlackCapturesJail.getSquare(i,
	 * j).setPiece(whiteCapturedPieces[index]); index++; }
	 * mBlackCapturesJail.getSquare(i, j).setJailStateChanged(); } }
	 * 
	 * if (objectivePiece != null && objectivePiece.isInCheck()) { //
	 * mInCheckLabel.setVisible(true); if
	 * (getGame().getBlackRules().objectivePiece(true).isInCheck()) {
	 * mBlackLabel.setBackground(Color.red); resetTurnLabels(mBlackLabel, true,
	 * true); resetTurnLabels(mWhiteLabel, false, false); } else {
	 * mWhiteLabel.setBackground(Color.red); resetTurnLabels(mWhiteLabel, false,
	 * true); resetTurnLabels(mBlackLabel, true, false); }
	 * 
	 * for (PieceController piece : getGame().getThreats(objectivePiece))
	 * piece.getSquare().setIsThreatSquare(true); } else {
	 * resetTurnLabels(mWhiteLabel, false, false); resetTurnLabels(mBlackLabel,
	 * true, false); } }
	 */

	/*
	 * private static void refreshSquares(BoardController[] boards) { for (int k
	 * = 0; k < boards.length; k++) { for (int i = 1; i <=
	 * boards[k].getMaxRow(); i++) { for (int j = 1; j <= boards[k].getMaxCol();
	 * j++) boards[k].getSquare(i, j).setStateChanged(); } } }
	 */

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

	public void saveGame()
	{
		String fileName = JOptionPane.showInputDialog(Driver.getInstance(),
				Messages.getString("PlayGamePanel.enterAName"), Messages.getString("PlayGamePanel.saving"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		if (fileName == null)
			return;
		// getGame().saveGame(fileName, false, true);
	}

	public void turn(boolean isBlackTurn)
	{
		if (mWhiteTimer != null && mBlackTimer != null)
		{
			(!isBlackTurn ? mWhiteTimer : mBlackTimer).startTimer();
			(isBlackTurn ? mWhiteTimer : mBlackTimer).stopTimer();
		}
	}

	/*
	 * private Pair<JPanel, List<SquareJLabel>> createGrid(GameController
	 * gameToPlay, int rows, int columns, int boardIndex) { JPanel gridPanel =
	 * new JPanel(); List<SquareJLabel> squareLabels = Lists.newArrayList();
	 * 
	 * gridPanel.setOpaque(false); gridPanel.setLayout(new GridLayout(rows + 1,
	 * columns)); gridPanel.setPreferredSize(new Dimension((columns + 1) * 48,
	 * (rows + 1) * 48));
	 * 
	 * for (int i = rows; i > 0; i--) { JLabel label =
	 * GuiUtility.createJLabel("" + i); //$NON-NLS-1$
	 * label.setHorizontalAlignment(SwingConstants.CENTER);
	 * gridPanel.add(label);
	 * 
	 * for (int j = 1; j <= columns; j++) { SquareJLabel squareLabel = new
	 * SquareJLabel(new ChessCoordinates(i, j, boardIndex));
	 * squareLabels.add(squareLabel); //squareLabel.addMouseListener(new
	 * SquareListener(squareLabel, board));
	 * squareLabel.addMouseMotionListener(new MotionAdapter(mGlobalGlassPane));
	 * 
	 * gridPanel.add(squareLabel); }
	 * 
	 * } for (int k = 0; k <= columns; k++) { if (k != 0) { JLabel label =
	 * GuiUtility.createJLabel("" + (char) (k - 1 + 'A')); //$NON-NLS-1$
	 * label.setHorizontalAlignment(SwingConstants.CENTER);
	 * gridPanel.add(label); } else {
	 * gridPanel.add(GuiUtility.createJLabel("")); //$NON-NLS-1$ } } return
	 * Pair.create(gridPanel, squareLabels); }
	 */

	public JMenu createMenuBar()
	{
		mOptionsMenu = new JMenu(Messages.getString("PlayGamePanel.menu")); //$NON-NLS-1$

		JMenuItem drawMenuItem = new JMenuItem(Messages.getString("PlayGamePanel.declareDraw"), KeyEvent.VK_D); //$NON-NLS-1$
		JMenuItem saveMenuItem = new JMenuItem(Messages.getString("PlayGamePanel.saveAndQuit"), KeyEvent.VK_S); //$NON-NLS-1$

		/*
		 * drawMenuItem.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent event) { if
		 * (getGame().getLastMove() == null) return;
		 * 
		 * mOptionsMenu.setVisible(false); Result result = Result.DRAW;
		 * result.setGuiText(Messages.getString("PlayGamePanel.drawWhatNow"));
		 * //$NON-NLS-1$ getGame().getLastMove().setResult(result);
		 * endOfGame(result); } });
		 */

		saveMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				mWhiteTimer.stopTimer();
				mBlackTimer.stopTimer();
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

	private void initComponents() throws Exception
	{
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

		Board[] boards = GameController.getGame().getBoards();
		// setBorder(BorderFactory.createLoweredBevelBorder());

		if (boards.length == 1)
		{
			constraints.gridheight = 12;
			constraints.gridy = 0;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 0;

			// Pair<JPanel, List<SquareJLabel>> pair = createGrid(boards[0],
			// false);

			// add(pair.first, constraints);
			add(new BoardPanel(boards[0].getRowCount(), boards[0].getColumnCount(), 0, mGlobalGlassPane, mDropManager));
			// mSquareLabels.addAll(pair.second);
		}
		else
		{
			constraints.gridheight = 12;
			constraints.gridy = 0;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 0;

			// Pair<JPanel, List<SquareJLabel>> pair = createGrid(boards[0],
			// false);
			// add(pair.first, constraints);
			// mSquareLabels.addAll(pair.second);

			add(new BoardPanel(boards[0].getRowCount(), boards[0].getColumnCount(), 0, mGlobalGlassPane, mDropManager));

			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.gridwidth = 10;
			constraints.insets = new Insets(10, 0, 0, 0);
			constraints.gridx = 11;
			// pair = createGrid(boards[1], false);
			// add(pair.first, constraints);
			// mSquareLabels.addAll(pair.second);
			add(new BoardPanel(boards[1].getRowCount(), boards[1].getColumnCount(), 1, mGlobalGlassPane, mDropManager));

			twoBoardsGridBagOffset += 10;
		}

		mWhiteLabel = GuiUtility.createJLabel(Messages.getString("PlayGamePanel.whiteCaps")); //$NON-NLS-1$
		mWhiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mWhiteLabel.setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$

		mBlackLabel = GuiUtility.createJLabel(Messages.getString("PlayGamePanel.blackCaps")); //$NON-NLS-1$
		mBlackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mBlackLabel.setBorder(BorderFactory.createTitledBorder("")); //$NON-NLS-1$

		mWhiteLabel.setOpaque(true);
		mBlackLabel.setOpaque(true);
		mWhiteLabel.setVisible(true);
		mBlackLabel.setVisible(true);

		int jailBoardSize;
		Team[] teams = GameController.getGame().getTeams();

		double size = teams[0].getPieces().size() > teams[1].getPieces().size() ? Math.sqrt(teams[0].getPieces().size()) : Math
				.sqrt(teams[1].getPieces().size());
		// the jail should always be at least 4x4
		jailBoardSize = Math.max((int) Math.ceil(size), 4);

		// mWhiteCapturesJail = new BoardController(jailBoardSize,
		// jailBoardSize, false);
		// mWhiteCapturePanel = createGrid(mWhiteCapturesJail, true).first;
		mWhiteCapturePanel = new BoardPanel(jailBoardSize, jailBoardSize);
		mWhiteCapturePanel.setBorder(GuiUtility.createBorder(Messages.getString("PlayGamePanel.capturedPieces"))); //$NON-NLS-1$
		mWhiteCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));

		mWhiteCapturePanel.setPreferredSize(new Dimension((jailBoardSize + 1) * 25, (jailBoardSize + 1) * 25));

		// mBlackCapturesJail = new BoardController(jailBoardSize,
		// jailBoardSize, false);
		// mBlackCapturePanel = createGrid(mBlackCapturesJail, true).first;
		mBlackCapturePanel = new BoardPanel(jailBoardSize, jailBoardSize);
		mBlackCapturePanel.setBorder(GuiUtility.createBorder(Messages.getString("PlayGamePanel.capturedPieces"))); //$NON-NLS-1$
		mBlackCapturePanel.setLayout(new GridLayout(jailBoardSize, jailBoardSize));

		mBlackCapturePanel.setPreferredSize(new Dimension((jailBoardSize + 1) * 25, (jailBoardSize + 1) * 25));

		// add the Black timer
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.ipadx = 100;
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 25, 10, 25);
		add(new ChessTimerLabel(mBlackTimer), constraints);

		// add the Black Jail
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 3;
		constraints.ipadx = 0;
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 2;
		add(mBlackCapturePanel, constraints);

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
		add(mWhiteCapturePanel, constraints);

		// adds the White timer
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.BASELINE;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.ipadx = 100;
		constraints.gridx = 11 + twoBoardsGridBagOffset;
		constraints.gridy = 9;
		constraints.insets = new Insets(0, 0, 0, 0);
		add(new ChessTimerLabel(mWhiteTimer), constraints);
		resetTimers();
	}

	public void setNextMoveMustPlacePiece(boolean nextMoveMustPlacePiece)
	{
		mNextMoveMustPlacePiece = nextMoveMustPlacePiece;
	}

	public boolean getNextMoveMustPlacePiece()
	{
		return mNextMoveMustPlacePiece;
	}

	/*
	 * public void setPieceToPlace(PieceController piece) { mPieceToPlace =
	 * piece; }
	 */

	public static void setGame(GameController game)
	{
		mGame = game;
	}

	public static GameController getGame()
	{
		return mGame;
	}

	public void resetTimers()
	{
		mWhiteTimer.reset();
		mBlackTimer.reset();
	}

	private static final long serialVersionUID = -2507232401817253688L;

	protected static boolean mNextMoveMustPlacePiece;
	private int mTeamIndex;
	protected int twoBoardsGridBagOffset;
	protected static GameController mGame;
	protected static ChessTimer mWhiteTimer;
	protected static ChessTimer mBlackTimer;
	protected static JLabel mWhiteLabel;
	protected static JLabel mBlackLabel;
	protected static JPanel mWhiteCapturePanel;
	protected static JPanel mBlackCapturePanel;
	protected static BoardPanel mWhiteCapturesJail;
	protected static BoardPanel mBlackCapturesJail;
	protected static JMenu mOptionsMenu;
	private final DropManager mDropManager;

	protected GlassPane mGlobalGlassPane;

	private static PlayGamePanel sInstance;
}
