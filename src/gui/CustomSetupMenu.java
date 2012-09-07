package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import logic.Board;
import logic.Builder;
import logic.Piece;
import logic.PieceBuilder;
import logic.Square;
import rules.EndOfGame;
import rules.ObjectivePiece;
import rules.Rules;
import utility.GUIUtility;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dragNdrop.AbstractDropManager;
import dragNdrop.DropAdapter;
import dragNdrop.DropEvent;
import dragNdrop.GlassPane;

public class CustomSetupMenu extends JPanel
{
	public CustomSetupMenu()
	{
		m_dropManager = new DropManager();
		m_globalGlassPane = new GlassPane();
		m_globalGlassPane.setOpaque(false);
		Driver.getInstance().setGlassPane(m_globalGlassPane);

		m_builder = new Builder("New Variant");
		m_whiteTeam = Lists.newArrayList();
		m_blackTeam = Lists.newArrayList();

		m_boardPanels = new JPanel[] { new JPanel(), new JPanel() };

		Board board = new Board(2, 1, false);
		m_pieceDisplaySquares[WHITE_INDEX] = board.getSquare(1, 1);
		m_pieceDisplaySquares[BLACK_INDEX] = board.getSquare(2, 1);

		m_optionsFrame = new JFrame();
		m_optionsFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		m_scrollPane.setPreferredSize(new Dimension(200, 200));
		initGUIComponents();
	}

	private void initGUIComponents()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		setBorder(BorderFactory.createLoweredBevelBorder());

		final JPanel showPiecePanel = new JPanel();
		showPiecePanel.setLayout(new GridLayout(2, 1));
		showPiecePanel.setPreferredSize(new Dimension(50, 100));

		for (Square square : m_pieceDisplaySquares)
			showPiecePanel.add(square);

		constraints.gridx = 5;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets(0, 10, 100, 0);
		add(showPiecePanel, constraints);

		for (Square square : m_pieceDisplaySquares)
		{
			square.addMouseListener(new PieceDisplayBoardListener(square));
			square.addMouseMotionListener(new MotionAdapter(m_globalGlassPane));
		}

		m_pieceDisplaySquares[WHITE_INDEX].setBackgroundColor(Color.LIGHT_GRAY);
		m_pieceDisplaySquares[BLACK_INDEX].setBackgroundColor(Color.getHSBColor(30, 70, 70));

		m_whiteRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "", false));
		m_blackRules.addEndOfGame(EndOfGame.CLASSIC.init(0, "", true));

		m_whiteRules.setObjectivePiece(ObjectivePiece.CLASSIC);
		m_blackRules.setObjectivePiece(ObjectivePiece.CLASSIC);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(10, 10, 10, 5);
		constraints.anchor = GridBagConstraints.CENTER;
		add(new JLabel("Variant Name"), constraints);

		final JTextField variantNameField = new JTextField(25);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		add(variantNameField, constraints);
		GUIUtility.requestFocus(variantNameField);

		Board[] temp = new Board[1];
		temp[0] = new Board(8, 8, false);

		setupPiecesList();
		drawBoards(temp, false);

		// Create button and add ActionListener
		JButton returnToMainButton = new JButton("Return to Main Menu");
		returnToMainButton.setToolTipText("Press me to go back to the Main Menu");
		returnToMainButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Driver.getInstance().revertToMainPanel();
			}
		});

		// Create button and add ActionListener
		JButton submitButton = new JButton("Save and Quit");
		submitButton.setToolTipText("Press me to save your finished variant");
		submitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (variantNameField.getText().trim().isEmpty())
				{
					JOptionPane.showMessageDialog(CustomSetupMenu.this, "Please enter a name for this game.", "Enter Name",
							JOptionPane.PLAIN_MESSAGE);
					return;
				}

				m_builder.setName(variantNameField.getText());

				for (Piece piece : m_whiteTeam)
					piece.setPromotesTo(m_promotionMap.get(piece.getName()));
				for (Piece piece : m_blackTeam)
					piece.setPromotesTo(m_promotionMap.get(piece.getName()));

				int numberOfObjectives = 0;
				if (!m_whiteRules.getObjectiveName().isEmpty())
				{
					for (Piece piece : m_whiteTeam)
					{
						if (piece.getName().equals(m_whiteRules.getObjectiveName()))
							numberOfObjectives++;
					}
					if (numberOfObjectives != 1)
					{
						JOptionPane.showMessageDialog(Driver.getInstance(), "Please place exactly one White Objective Piece", "Objective Missing",
								JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}

				numberOfObjectives = 0;
				if (!m_blackRules.getObjectiveName().isEmpty())
				{
					for (Piece piece : m_blackTeam)
					{
						if (piece.getName().equals(m_blackRules.getObjectiveName()))
							numberOfObjectives++;
					}
					if (numberOfObjectives != 1)
					{
						JOptionPane.showMessageDialog(Driver.getInstance(), "Please place exactly one Black Objective Piece", "Objective Missing",
								JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}

				m_builder.m_whiteTeam = m_whiteTeam;

				boolean objectivePieceIsSet = false;
				for (Piece piece : m_whiteTeam)
				{
					if (piece.getName().equals("King"))
					{
						m_whiteRules.setObjectivePiece(ObjectivePiece.CLASSIC);
						objectivePieceIsSet = true;
						break;
					}
					// TODO Fix custom Objective Piece
					// else if (p.isObjective()) {
					// whiteRules.setObjectivePiece(new
					// ObjectivePiece("custom objective", p.getName()));
					// System.out.println(p.getName());
					// set = true;
					// break;
					// }
				}
				if (!objectivePieceIsSet)
					m_whiteRules.setObjectivePiece(ObjectivePiece.NO_OBJECTIVE);

				m_builder.m_blackTeam = m_blackTeam;
				objectivePieceIsSet = false;
				for (Piece piece : m_blackTeam)
				{
					if (piece.getName().equals("King"))
					{
						m_blackRules.setObjectivePiece(ObjectivePiece.CLASSIC);
						objectivePieceIsSet = true;
						break;
					}
					// TODO Fix custom Objective Piece
					// else if (p.isObjective()) {
					// blackRules.setObjectivePiece(new
					// ObjectivePiece("custom objective", p.getName()));
					// set = true;
					// break;
					// }
				}
				if (!objectivePieceIsSet)
					m_blackRules.setObjectivePiece(ObjectivePiece.CLASSIC);

				m_builder.writeFile(m_whiteRules, m_blackRules);
				// Return to the main screen.
				Driver.getInstance().revertToMainPanel();
			}
		});

		m_changePromotionButton = new JButton("Promote This Piece");
		m_changePromotionButton.setToolTipText("Press me to set up promotion for the above selected piece");
		m_changePromotionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();
				
				new PiecePromotion((String) m_pieceTypeList.getSelectedValue(), CustomSetupMenu.this, m_optionsFrame);
			}
		});
		
		JButton boardSetupButton = new JButton("Customize Game Board");
		boardSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				new BoardCustomMenu(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		JButton objectiveSetupButton = new JButton("Set up Game Objectives");
		objectiveSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				new ObjectiveMaker(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		JButton ruleSetupButton = new JButton("Set up Game Rules");
		ruleSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				new RuleMaker(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		JButton playerSetupButton = new JButton("Set up Player Rules");
		playerSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				new PlayerCustomMenu(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		JButton pieceSetupButton = new JButton("Make New Pieces");
		pieceSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				new PieceMaker(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		GridBagConstraints mainPanelConstraints = new GridBagConstraints();

		m_pieceListPanel.setLayout(new GridBagLayout());
		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 0;
		mainPanelConstraints.gridwidth = 2;
		m_pieceListPanel.add(m_scrollPane, mainPanelConstraints);

		mainPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 1;
		mainPanelConstraints.gridwidth = 1;
		mainPanelConstraints.insets = new Insets(5, 3, 3, 3);
		m_pieceListPanel.add(m_changePromotionButton, mainPanelConstraints);

		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 1;
		m_pieceListPanel.add(pieceSetupButton, mainPanelConstraints);

		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 2;
		m_pieceListPanel.add(boardSetupButton, mainPanelConstraints);

		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 3;
		m_pieceListPanel.add(objectiveSetupButton, mainPanelConstraints);

		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 3;
		m_pieceListPanel.add(ruleSetupButton, mainPanelConstraints);

		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 2;
		m_pieceListPanel.add(playerSetupButton, mainPanelConstraints);

		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridBagLayout());

		mainPanelConstraints.fill = GridBagConstraints.CENTER;
		mainPanelConstraints.gridx = 0;
		mainPanelConstraints.gridy = 1;
		optionsPanel.add(submitButton, mainPanelConstraints);

		mainPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanelConstraints.gridx = 1;
		mainPanelConstraints.gridy = 1;
		optionsPanel.add(returnToMainButton, mainPanelConstraints);

		constraints.gridy = 2;
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(10, 0, 10, 5);
		add(optionsPanel, constraints);

		mainPanelConstraints.fill = GridBagConstraints.NONE;
		mainPanelConstraints.insets = new Insets(0, 0, 0, 0);
		mainPanelConstraints.gridwidth = 1;
		mainPanelConstraints.gridx = 6;
		mainPanelConstraints.gridy = 1;
		add(m_pieceListPanel, mainPanelConstraints);

	}

	public void drawBoards(Board[] boards, boolean hasMultipleBoards)
	{
		// Get the set the board into the builder.
		m_builder.setBoards(boards);

		for (JPanel panel : m_boardPanels)
		{
			panel.removeAll();
			remove(panel);
		}

		GridBagConstraints constraints = new GridBagConstraints();

		List<Square> squareList = Lists.newArrayListWithExpectedSize(boards[0].getMaxRow() * boards[0].getMaxCol() * 2);
		for (int boardIndex = 0, gridxConstraint = 1; boardIndex < boards.length; boardIndex++, gridxConstraint += 2)
		{
			// create a JPanel to hold the grid and set the layout to the number
			// of squares in the board
			JPanel gridHolder = new JPanel();
			gridHolder.setLayout(new GridLayout(boards[boardIndex].numRows(), boards[boardIndex].numCols()));

			int numberOfRows = boards[boardIndex].numRows();
			int numberOfColumns = boards[boardIndex].numCols();
			for (int row = numberOfRows; row > 0; row--)
			{
				for (int column = 1; column <= numberOfColumns; column++)
				{
					Square square = boards[boardIndex].getSquare(row, column);
					square.addMouseListener(new SquareSetupMouseListener(square, boards[boardIndex]));
					squareList.add(square);
					gridHolder.add(square);
					square.refresh();
				}
			}
			JPanel panel = gridHolder;

			panel.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
			panel.setLayout(new GridLayout(boards[boardIndex].numRows(), boards[boardIndex].numCols()));
			// set the size of the grid to the number of rows and columns, scaled by 48, the size of the images
			panel.setPreferredSize(new Dimension(boards[boardIndex].numCols() * 48, boards[boardIndex].numRows() * 48));

			constraints.insets = new Insets(3, 5, 3, 10);
			constraints.gridx = gridxConstraint;
			constraints.gridy = 1;
			constraints.gridwidth = 2;

			add(panel, constraints);
		}
		m_dropManager.setComponentList(squareList);

		for (JPanel panel : m_boardPanels)
		{
			panel.revalidate();
			panel.repaint();
		}

		Driver.getInstance().pack();
	}

	public void setupPiecesList()
	{
		// create a List with a vertical ScrollBar
		final DefaultListModel list = new DefaultListModel();

		Object[] allPieces = PieceBuilder.getSet().toArray();
		for (int i = 0; i < allPieces.length; i++)
			list.addElement(allPieces[i]);

		m_pieceTypeList = new JList(list);

		m_pieceTypeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		m_pieceTypeList.setLayoutOrientation(JList.VERTICAL);
		m_pieceTypeList.setVisibleRowCount(-1);
		m_pieceTypeList.setSelectedIndex(0);

		Piece whitePieceBeingDisplayed = PieceBuilder.makePiece((String) list.elementAt(0), false, m_pieceDisplaySquares[WHITE_INDEX],
				null);
		Piece blackPieceBeingDisplayed = PieceBuilder.makePiece((String) list.elementAt(0), true, m_pieceDisplaySquares[BLACK_INDEX],
				null);

		m_pieceDisplaySquares[WHITE_INDEX].setPiece(whitePieceBeingDisplayed);
		m_pieceDisplaySquares[BLACK_INDEX].setPiece(blackPieceBeingDisplayed);

		for (Square square : m_pieceDisplaySquares)
			square.refresh();

		ListSelectionModel selectList = m_pieceTypeList.getSelectionModel();

		m_scrollPane.getViewport().add(m_pieceTypeList, null);

		selectList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();

				int selection = listSelectionModel.getAnchorSelectionIndex();
				if (!listSelectionModel.getValueIsAdjusting())
				{
					for (Square square : m_pieceDisplaySquares)
						square.setVisible(true);

					m_changePromotionButton.setEnabled(true);

					Piece whitePieceBeingDisplayed = PieceBuilder.makePiece((String) list.elementAt(selection), false,
							m_pieceDisplaySquares[WHITE_INDEX], null);
					Piece blackPieceBeingDisplayed = PieceBuilder.makePiece((String) list.elementAt(selection), true,
							m_pieceDisplaySquares[BLACK_INDEX], null);
					m_pieceDisplaySquares[WHITE_INDEX].setPiece(whitePieceBeingDisplayed);
					m_pieceDisplaySquares[BLACK_INDEX].setPiece(blackPieceBeingDisplayed);

					for (Square square : m_pieceDisplaySquares)
					{
						square.resetColor();
						square.refresh();
					}
				}
			}
		});

		m_pieceListPanel.revalidate();
		m_pieceListPanel.repaint();

		Driver.getInstance().pack();
	}

	public Builder getBuilder()
	{
		return m_builder;
	}

	private final class SquareSetupMouseListener extends MouseAdapter
	{
		public SquareSetupMouseListener(Square square, Board board)
		{
			m_square = square;
			m_board = board;
		}

		@Override
		public void mouseClicked(MouseEvent event)
		{
			if (m_square.isOccupied())
			{
				Piece toRemove = m_square.setPiece(null);
				(toRemove.isBlack() ? m_blackTeam : m_whiteTeam).remove(toRemove);

				m_square.refresh();
			}
			else
			{
				showSquareOptions();
			}
		}

		public Board getBoard()
		{
			return m_board;
		}

		private void showSquareOptions()
		{
			final JFrame popupFrame = new JFrame("Square Options");
			popupFrame.setSize(370, 120);
			popupFrame.setLocationRelativeTo(Driver.getInstance());
			popupFrame.setLayout(new FlowLayout());

			final JButton colorChooserButton = new JButton("Set Square Color");
			colorChooserButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent event)
				{
					Color color = JColorChooser.showDialog(popupFrame, "Choose Color", m_square.getColor());
					if (color == null)
						return;
					// TODO: verify that this can be removed
					// can't let them pick exactly the highlight color, or they
					// could move to that space from anywhere
					if (color != Square.HIGHLIGHT_COLOR)
					{
						m_square.setBackgroundColor(color);
						colorChooserButton.setBackground(color);
					}
					else
					{
						// the chances of this happening is EXTREMELY small...
						JOptionPane.showMessageDialog(popupFrame, "That color cannot be selected.", "Invalid Color",
								JOptionPane.PLAIN_MESSAGE);
					}

				}
			});
			popupFrame.add(colorChooserButton);

			final JCheckBox uninhabitableButton = new JCheckBox("Uninhabitable", !m_square.isHabitable());
			popupFrame.add(uninhabitableButton);

			final JButton doneButton = new JButton("Done");
			doneButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					if (uninhabitableButton.isSelected())
					{
						m_square.setIsHabitable(false);
						m_square.setIcon(uninhabitableIcon);
					}
					else
					{
						m_square.setIsHabitable(true);
						m_square.setIcon(null);
					}
					popupFrame.dispose();
				}
			});
			popupFrame.add(doneButton);

			popupFrame.setVisible(true);
		}

		private ImageIcon uninhabitableIcon = GUIUtility.createImageIcon(48, 48, "/Uninhabitable.png");
		
		private Square m_square;
		private Board m_board;
	}

	public void putPromotionMap(String pieceName, List<String> promotesTo)
	{
		m_promotionMap.put(pieceName, promotesTo);
	}

	private final class PieceDisplayBoardListener extends DropAdapter implements MouseListener
	{
		public PieceDisplayBoardListener(Square square)
		{
			super(m_globalGlassPane);
			m_square = square;
			addDropListener(m_dropManager);
		}

		@Override
		public void mousePressed(MouseEvent event)
		{
			m_glassPane.setVisible(true);

			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, event.getComponent());
			SwingUtilities.convertPointFromScreen(point, m_glassPane);

			m_glassPane.setPoint(point);

			BufferedImage image = null;
			ImageIcon imageIcon = m_square.getPiece().getIcon();
			int width = imageIcon.getIconWidth();
			int height = imageIcon.getIconHeight();
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics2D = (Graphics2D) image.getGraphics();
			imageIcon.paintIcon(null, graphics2D, 0, 0);
			graphics2D.dispose();

			m_glassPane.setImage(image);
			m_glassPane.repaint();
		}

		@Override
		public void mouseReleased(MouseEvent event)
		{
			Point point = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(point, event.getComponent());

			m_glassPane.setImage(null);
			m_glassPane.setVisible(false);

			fireDropEvent(new DropEvent(point, m_square));
		}

		@Override
		public void mouseExited(MouseEvent event)
		{
		}

		@Override
		public void mouseEntered(MouseEvent event)
		{
		}

		@Override
		public void mouseClicked(MouseEvent event)
		{
		}

		private final Square m_square;
	};

	private final class DropManager extends AbstractDropManager
	{
		@Override
		public void dropped(DropEvent event)
		{
			Square originSquare = (Square) event.getOriginComponent();
			Square destinationSquare = (Square) isInTarget(event.getDropLocation());

			if (destinationSquare == null)
				return;

			if (destinationSquare.isHabitable())
			{
				Piece originPiece = originSquare.getPiece();

				Piece oldPiece = destinationSquare.getPiece();
				if (oldPiece != null)
					(oldPiece.isBlack() ? m_blackTeam : m_whiteTeam).remove(oldPiece);

				MouseListener[] listeners = destinationSquare.getMouseListeners();
				Preconditions.checkPositionIndex(0, listeners.length);
				Preconditions.checkState(listeners[0] instanceof SquareSetupMouseListener);

				Piece piece = PieceBuilder.makePiece(originPiece.getName(), originPiece.isBlack(), destinationSquare,
						((SquareSetupMouseListener) listeners[0]).getBoard());

				if (originPiece.isBlack())
					m_blackTeam.add(piece);
				else
					m_whiteTeam.add(piece);

				destinationSquare.setPiece(piece);
				destinationSquare.refresh();
			}
			else
			{
				JOptionPane.showMessageDialog(Driver.getInstance(), "This square is uninhabitable.", "Warning", JOptionPane.PLAIN_MESSAGE);
			}
		}
	};

	private static final long serialVersionUID = 7830479492072657640L;
	private static final int WHITE_INDEX = 0;
	private static final int BLACK_INDEX = 1;

	private final GlassPane m_globalGlassPane;
	private final DropManager m_dropManager;
	private final Square[] m_pieceDisplaySquares = new Square[2];

	public Rules m_whiteRules = new Rules(false);
	public Rules m_blackRules = new Rules(true);

	private JPanel[] m_boardPanels;
	private JPanel m_pieceListPanel = new JPanel();
	private Map<String, List<String>> m_promotionMap = Maps.newHashMap();
	private Builder m_builder;
	private List<Piece> m_whiteTeam;
	private List<Piece> m_blackTeam;
	private JButton m_changePromotionButton;
	private JList m_pieceTypeList;
	private JFrame m_optionsFrame;
	private JScrollPane m_scrollPane = new JScrollPane();
}
