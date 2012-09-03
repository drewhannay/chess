package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CustomSetupMenu extends JPanel
{
	public CustomSetupMenu()
	{
		m_builder = new Builder("New Variant");
		m_whiteTeam = Lists.newArrayList();
		m_blackTeam = Lists.newArrayList();
		m_pieceDisplayBoard = new Board(2, 1, false);
		m_draggedSquare = new Square(0, 0);
		m_optionsFrame = new JFrame();
		m_optionsFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		m_scrollPane.setPreferredSize(new Dimension(200, 200));
		initGUIComponents();
	}

	private void initGUIComponents()
	{
		// Set the layout of this JPanel.
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		setBorder(BorderFactory.createLoweredBevelBorder());

		final JPanel showPiecePanel = new JPanel();
		showPiecePanel.setLayout(new GridLayout(2, 1));
		showPiecePanel.setPreferredSize(new Dimension(50, 100));

		showPiecePanel.add(m_pieceDisplayBoard.getSquare(1, 1));
		showPiecePanel.add(m_pieceDisplayBoard.getSquare(2, 1));
		constraints.gridx = 5;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets(0, 10, 100, 0);
		add(showPiecePanel, constraints);

		m_pieceDisplayBoard.getSquare(1, 1).addMouseListener(new MouseListener()
		{
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				if (m_draggedSquare.getPiece() != m_pieceDisplayBoard.getSquare(1, 1).getPiece())
				{
					m_pieceDisplayBoard.getSquare(1, 1).setBackgroundColor(Square.HIGHLIGHT_COLOR);
					m_draggedSquare.setPiece(m_pieceDisplayBoard.getSquare(1, 1).getPiece());
					m_pieceDisplayBoard.getSquare(2, 1).resetColor();
				}
				else
				{
					m_draggedSquare.setPiece(null);
					m_pieceDisplayBoard.getSquare(1, 1).resetColor();
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0)
			{
			}

			@Override
			public void mouseExited(MouseEvent arg0)
			{
			}

			@Override
			public void mouseEntered(MouseEvent arg0)
			{
			}

			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});

		m_pieceDisplayBoard.getSquare(2, 1).addMouseListener(new MouseListener()
		{
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				if (m_draggedSquare.getPiece() != m_pieceDisplayBoard.getSquare(2, 1).getPiece())
				{
					m_pieceDisplayBoard.getSquare(2, 1).setBackgroundColor(Square.HIGHLIGHT_COLOR);
					m_draggedSquare.setPiece(m_pieceDisplayBoard.getSquare(2, 1).getPiece());
					m_pieceDisplayBoard.getSquare(1, 1).resetColor();
				}
				else
				{
					m_draggedSquare.setPiece(null);
					m_pieceDisplayBoard.getSquare(2, 1).resetColor();
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0)
			{
			}

			@Override
			public void mouseExited(MouseEvent arg0)
			{
			}

			@Override
			public void mouseEntered(MouseEvent arg0)
			{
			}

			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});

		m_pieceDisplayBoard.getSquare(1, 1).setBackgroundColor(Color.LIGHT_GRAY);
		m_pieceDisplayBoard.getSquare(2, 1).setBackgroundColor(Color.getHSBColor(30, 70, 70));

		m_whiteRules.addEndOfGame(new EndOfGame("classic", 0, "", false));
		m_blackRules.addEndOfGame(new EndOfGame("classic", 0, "", true));

		m_whiteRules.setObjectivePiece(new ObjectivePiece("classic", "King"));
		m_blackRules.setObjectivePiece(new ObjectivePiece("classic", "King"));

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
		m_returnToMainButton = new JButton("Return to Main Menu");
		m_returnToMainButton.setToolTipText("Press me to go back to the Turn setup window");
		GUIUtility.setupReturnToMainButton(m_returnToMainButton);

		// Create button and add ActionListener
		m_submitButton = new JButton("Save and Quit");
		m_submitButton.setToolTipText("Press me to save your finished variant");
		m_submitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (variantNameField.getText().equals("") || variantNameField.getText().equals(" "))
				{
					JOptionPane.showMessageDialog(CustomSetupMenu.this, "Please enter a name for this game.", "Enter Name",
							JOptionPane.PLAIN_MESSAGE);
				}
				else
				{
					m_builder.setName(variantNameField.getText());

					for (Piece piece : m_whiteTeam)
						piece.setPromotesTo(m_promotionMap.get(piece.getName()));
					for (Piece piece : m_blackTeam)
						piece.setPromotesTo(m_promotionMap.get(piece.getName()));

					int numberOfObjectives = 0;
					if (!m_whiteRules.getObjectiveName().equals(""))
					{
						for (Piece piece : m_whiteTeam)
						{
							if (piece.getName().equals(m_whiteRules.getObjectiveName()))
								numberOfObjectives++;
						}
						if (numberOfObjectives != 1)
						{
							JOptionPane.showMessageDialog(null, "Please place exactly one White Objective Piece", "Objective Missing",
									JOptionPane.PLAIN_MESSAGE);
							return;
						}
					}

					numberOfObjectives = 0;
					if (!m_blackRules.getObjectiveName().equals(""))
					{
						for (Piece piece : m_blackTeam)
						{
							if (piece.getName().equals(m_blackRules.getObjectiveName()))
								numberOfObjectives++;
						}
						if (numberOfObjectives != 1)
						{
							JOptionPane.showMessageDialog(null, "Please place exactly one Black Objective Piece", "Objective Missing",
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
							m_whiteRules.setObjectivePiece(new ObjectivePiece("classic", "King"));
							objectivePieceIsSet = true;
							break;
						}
						// TODO Fix this.
						// else if (p.isObjective()) {
						// whiteRules.setObjectivePiece(new
						// ObjectivePiece("custom objective", p.getName()));
						// System.out.println(p.getName());
						// set = true;
						// break;
						// }
					}
					if (!objectivePieceIsSet)
						m_whiteRules.setObjectivePiece(new ObjectivePiece("no objective", ""));

					m_builder.m_blackTeam = m_blackTeam;
					objectivePieceIsSet = false;
					for (Piece piece : m_blackTeam)
					{
						if (piece.getName().equals("King"))
						{
							m_blackRules.setObjectivePiece(new ObjectivePiece("classic", "King"));
							objectivePieceIsSet = true;
							break;
						}
						// TODO Fix this
						// else if (p.isObjective()) {
						// blackRules.setObjectivePiece(new
						// ObjectivePiece("custom objective", p.getName()));
						// set = true;
						// break;
						// }
					}
					if (!objectivePieceIsSet)
						m_blackRules.setObjectivePiece(new ObjectivePiece("no objective", ""));

					m_builder.writeFile(m_whiteRules, m_blackRules);
					// Return to the main screen.
					Driver.getInstance().revertToMainPanel();
				}
			}
		});

		m_changePromotionButton = new JButton("Promote This Piece");
		m_changePromotionButton.setToolTipText("Press me to set up promotion for the above selected piece");
		m_changePromotionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();
				
				@SuppressWarnings("unused")
				PiecePromotion piecePromote = new PiecePromotion((String) m_pieceTypeList.getSelectedValue(), CustomSetupMenu.this, m_optionsFrame);
			}
		});
		
		JButton boardSetupButton = new JButton("Customize Game Board");
		boardSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				@SuppressWarnings("unused")
				BoardCustomMenu makeObj = new BoardCustomMenu(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		JButton objectiveSetupButton = new JButton("Set up Game Objectives");
		objectiveSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				@SuppressWarnings("unused")
				ObjectiveMaker makeObj = new ObjectiveMaker(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		JButton ruleSetupButton = new JButton("Set up Game Rules");
		ruleSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				@SuppressWarnings("unused")
				RuleMaker makeRules = new RuleMaker(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		JButton playerSetupButton = new JButton("Set up Player Rules");
		playerSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				@SuppressWarnings("unused")
				PlayerCustomMenu makeObj = new PlayerCustomMenu(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		JButton pieceSetupButton = new JButton("Make New Pieces");
		pieceSetupButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_optionsFrame.dispose();
				m_optionsFrame = new JFrame();

				@SuppressWarnings("unused")
				PieceMaker makeObj = new PieceMaker(CustomSetupMenu.this, m_optionsFrame);
			}
		});

		// TODO: this variable should have a more descriptive name; so should
		// the other GridBagConstraints
		GridBagConstraints c = new GridBagConstraints();

		m_pieceListPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		m_pieceListPanel.add(m_scrollPane, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 3, 3, 3);
		m_pieceListPanel.add(m_changePromotionButton, c);

		c.gridx = 1;
		c.gridy = 1;
		m_pieceListPanel.add(pieceSetupButton, c);

		c.gridx = 0;
		c.gridy = 2;
		m_pieceListPanel.add(boardSetupButton, c);

		c.gridx = 0;
		c.gridy = 3;
		m_pieceListPanel.add(objectiveSetupButton, c);

		c.gridx = 1;
		c.gridy = 3;
		m_pieceListPanel.add(ruleSetupButton, c);

		c.gridx = 1;
		c.gridy = 2;
		m_pieceListPanel.add(playerSetupButton, c);

		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 1;
		optionsPanel.add(m_submitButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		optionsPanel.add(m_returnToMainButton, c);

		constraints.gridy = 2;
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(10, 0, 10, 5);
		add(optionsPanel, constraints);

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridwidth = 1;
		c.gridx = 6;
		c.gridy = 1;
		add(m_pieceListPanel, c);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() / 4));
		int y = (int) ((dimension.getHeight() / 4));
		Driver.getInstance().setLocation(x, y);
	}

	public void drawBoards(Board[] boards, boolean hasMultipleBoards)
	{
		// Get the set the board into the builder.
		m_builder.setBoards(boards);

		m_boardOnePanel.removeAll();
		remove(m_boardOnePanel);
		m_boardTwoPanel.removeAll();
		remove(m_boardTwoPanel);

		GridBagConstraints constraints = new GridBagConstraints();

		for (int n = 0; n < boards.length; n++)
		{
			// create a JPanel to hold the grid and set the layout to the number
			// of squares in the board
			JPanel gridHolder = new JPanel();
			gridHolder.setLayout(new GridLayout(boards[n].numRows(), boards[n].numCols()));

			// loop through the board, initializing each Square and adding
			// ActionListeners
			int numberOfRows = boards[n].numRows();
			int numberOfColumns = boards[n].numCols();
			for (int i = numberOfRows; i > 0; i--)
			{
				for (int j = 1; j <= numberOfColumns; j++)
				{
					boards[n].getSquare(i, j).addMouseListener(new DragMouseAdapter(boards[n].getSquare(i, j), boards[n]));
					gridHolder.add(boards[n].getSquare(i, j));
					// have the square refresh all it's properties
					boards[n].getSquare(i, j).refresh();
				}
			}
			if (n == 0)
			{
				m_boardOnePanel = gridHolder;

				m_boardOnePanel.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
				m_boardOnePanel.setLayout(new GridLayout(boards[n].numRows(), boards[n].numCols()));
				// set the size of the grid to the number of rows and columns,
				// scaled by 48, the size of the images
				m_boardOnePanel.setPreferredSize(new Dimension(boards[n].numCols() * 48, boards[n].numRows() * 48));

				constraints.insets = new Insets(3, 5, 3, 10);
				constraints.gridx = 1;
				constraints.gridy = 1;
				constraints.gridwidth = 2;
				// add the grid to the main JPanel
				add(m_boardOnePanel, constraints);
			}
			else
			{
				m_boardTwoPanel = gridHolder;

				m_boardTwoPanel.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
				m_boardTwoPanel.setLayout(new GridLayout(boards[n].numRows(), boards[n].numCols()));
				// set the size of the grid to the number of rows and columns,
				// scaled by 48, the size of the images
				m_boardTwoPanel.setPreferredSize(new Dimension(boards[n].numCols() * 48, boards[n].numRows() * 48));

				constraints.insets = new Insets(3, 5, 3, 10);
				constraints.gridx = 3;
				constraints.gridy = 1;
				constraints.gridwidth = 2;
				// add the grid to the main JPanel
				add(m_boardTwoPanel, constraints);
			}
		}

		m_boardOnePanel.revalidate();
		m_boardOnePanel.repaint();
		m_boardTwoPanel.revalidate();
		m_boardTwoPanel.repaint();

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

		// TODO: these need better names
		Piece toAdd = PieceBuilder.makePiece((String) list.elementAt(0), false, m_pieceDisplayBoard.getSquare(1, 1),
				m_pieceDisplayBoard);
		Piece toAdd1 = PieceBuilder.makePiece((String) list.elementAt(0), true, m_pieceDisplayBoard.getSquare(2, 1),
				m_pieceDisplayBoard);
		m_pieceDisplayBoard.getSquare(1, 1).setPiece(toAdd);
		m_pieceDisplayBoard.getSquare(2, 1).setPiece(toAdd1);

		m_pieceDisplayBoard.getSquare(1, 1).refresh();
		m_pieceDisplayBoard.getSquare(2, 1).refresh();

		ListSelectionModel selectList = m_pieceTypeList.getSelectionModel();
		final Color originalColor = m_pieceDisplayBoard.getSquare(1, 1).getColor();

		m_scrollPane.getViewport().add(m_pieceTypeList, null);

		selectList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();

				int selection = listSelectionModel.getAnchorSelectionIndex();
				if (!listSelectionModel.getValueIsAdjusting())
				{
					m_pieceDisplayBoard.getSquare(2, 1).setVisible(true);
					m_pieceDisplayBoard.getSquare(1, 1).setVisible(true);
					m_changePromotionButton.setEnabled(true);
					if (m_pieceDisplayBoard.getSquare(1, 1).getColor().equals(originalColor) == false)
						m_pieceDisplayBoard.getSquare(1, 1).setBackgroundColor(originalColor);

					// TODO: these need better names
					Piece toAdd = PieceBuilder.makePiece((String) list.elementAt(selection), false,
							m_pieceDisplayBoard.getSquare(1, 1), m_pieceDisplayBoard);
					Piece toAdd1 = PieceBuilder.makePiece((String) list.elementAt(selection), true,
							m_pieceDisplayBoard.getSquare(2, 1), m_pieceDisplayBoard);
					m_pieceDisplayBoard.getSquare(1, 1).setPiece(toAdd);
					m_pieceDisplayBoard.getSquare(2, 1).setPiece(toAdd1);

					m_pieceDisplayBoard.getSquare(1, 1).resetColor();
					m_pieceDisplayBoard.getSquare(2, 1).resetColor();
					m_pieceDisplayBoard.getSquare(1, 1).refresh();
					m_pieceDisplayBoard.getSquare(2, 1).refresh();
					m_draggedSquare.setPiece(null);
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

	class DragMouseAdapter extends MouseAdapter
	{
		public DragMouseAdapter(Square square, Board board)
		{
			m_square = square;
			m_board = board;
		}

		private void squareOptions()
		{
			final JFrame popupFrame = new JFrame("Square Options");
			popupFrame.setSize(370, 120);
			popupFrame.setLocationRelativeTo(null);
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
				public void actionPerformed(ActionEvent arg0)
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

		private void setPieceOnBoard(boolean isBlackPiece)
		{
			if (m_square.isHabitable())
			{
				Piece piece = PieceBuilder.makePiece(m_draggedSquare.getPiece().getName(), isBlackPiece, m_square, m_board);

				if (isBlackPiece)
					m_blackTeam.add(piece);
				else
					m_whiteTeam.add(piece);

				m_square.setPiece(piece);
				m_square.refresh();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "This square is uninhabitable.", "Warning", JOptionPane.PLAIN_MESSAGE);
			}
		}

		/**
		 * Pulls up square options if no piece has been chosen to add or no
		 * piece is present. Deletes any pieces currently on the square. Or it
		 * will just place the piece that has been selected.
		 */
		@Override
		public void mousePressed(MouseEvent e)
		{
			if (m_square.isOccupied())
			{
				Piece toRemove = m_square.getPiece();
				(toRemove.isBlack() ? m_blackTeam : m_whiteTeam).remove(toRemove);
				m_square.setPiece(null);
				if (m_draggedSquare.getPiece() != null)
				{
					setPieceOnBoard(m_draggedSquare.getPiece().isBlack());
				}
				m_square.refresh();
			}
			else if (m_draggedSquare.isOccupied())
			{
				setPieceOnBoard(m_draggedSquare.getPiece().isBlack());
			}
			else
				squareOptions();
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
		}

		private final ImageIcon uninhabitableIcon = new ImageIcon("./images/Uninhabitable.png");

		private Square m_square;
		private Board m_board;
	}

	public void putPromotionMap(String pieceName, List promotesTo)
	{
		m_promotionMap.put(pieceName, promotesTo);
	}

	private static final long serialVersionUID = 7830479492072657640L;

	private final Board m_pieceDisplayBoard;

	public Rules m_whiteRules = new Rules(false, false);
	public Rules m_blackRules = new Rules(false, true);

	private Square m_draggedSquare;
	private JPanel m_boardOnePanel = new JPanel();
	private JPanel m_boardTwoPanel = new JPanel();
	private JPanel m_pieceListPanel = new JPanel();
	private Map<String, List<String>> m_promotionMap = Maps.newHashMap();
	private Builder m_builder;
	private List<Piece> m_whiteTeam;
	private List<Piece> m_blackTeam;
	private JButton m_returnToMainButton;
	private JButton m_submitButton;
	private JButton m_changePromotionButton;
	private JList m_pieceTypeList;
	private JFrame m_optionsFrame;
	private JScrollPane m_scrollPane = new JScrollPane();
}
