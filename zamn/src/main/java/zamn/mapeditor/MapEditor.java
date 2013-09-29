package zamn.mapeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import zamn.board.BoardPiece;
import zamn.board.Sprite;
import zamn.board.controlmode.Action;
import zamn.creation.board.BoardSerializer;
import zamn.creation.board.TileFactory;

public class MapEditor extends JFrame {

	private static final Logger LOG = Logger.getLogger(MapEditor.class);

	private static final long serialVersionUID = -907502337742442255L;

	private MapEditorBoard board;
	private BoardSerializer boardSerializer;
	private MapEditorPalette critterPalette;
	private MapEditorPalette decorationPalette;
	private JFileChooser fileChooser;
	private Resource firstBoardResource;
	private JPanel interfaze;
	private JSplitPane mapEditorScreen;
	private Dimension paletteSize;
	private JMenuItem saveMenuItem;
	private TileFactory tileFactory;
	private MapEditorPalette tilePalette;

	private void addCritter() {
		addSpriteAsBoardPiece(critterPalette.getSelectedSprite());
	}

	private void addDecoration() {
		addSpriteAsBoardPiece(decorationPalette.getSelectedSprite());
	}

	protected void addPalette(MapEditorPalette palette) {
		JScrollPane scrollPane = new JScrollPane(palette);
		scrollPane.setPreferredSize(paletteSize);
		GridBagConstraints gc = new GridBagConstraints();
		gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = getNumberOfPalettes();
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		interfaze.add(scrollPane, gc);
	}

	private void addSpriteAsBoardPiece(Sprite sprite) {
		if (sprite != null) {
			board.addBoardPiece((BoardPiece) sprite, board.getCursorX(),
					board.getCursorY());
		}
	}

	public void bootstrap() throws IOException {
		LOG.debug("Bootstrapping...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createAndAddMenuBar();
		disableSaveMenuItem();
		createFileChooser();
		createMapEditorInterface();
		createAndAddMapEditorScreen();
		populateMapEditorInterface();
		setResizable(false);
		board.load(firstBoardResource.getURI());
	}

	private void createAndAddMapEditorScreen() {
		mapEditorScreen = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, board,
				interfaze);
		mapEditorScreen.setDividerSize(0);
		mapEditorScreen.setBorder(BorderFactory.createEmptyBorder());

		// create input and action maps
		InputMap inputMap = new InputMap();
		ActionMap actionMap = new ActionMap();

		inputMap.put(KeyStroke.getKeyStroke(Action.UP.toString()), Action.UP);
		inputMap.put(KeyStroke.getKeyStroke(Action.RIGHT.toString()),
				Action.RIGHT);
		inputMap.put(KeyStroke.getKeyStroke(Action.DOWN.toString()),
				Action.DOWN);
		inputMap.put(KeyStroke.getKeyStroke(Action.LEFT.toString()),
				Action.LEFT);
		inputMap.put(KeyStroke.getKeyStroke(Action.ENTER.toString()),
				Action.ENTER);
		inputMap.put(KeyStroke.getKeyStroke(Action.ESCAPE.toString()),
				Action.ESCAPE);
		inputMap.put(KeyStroke.getKeyStroke(Action.BACK_SPACE.toString()),
				Action.BACK_SPACE);
		inputMap.put(KeyStroke.getKeyStroke(Action.SPACE.toString()),
				Action.SPACE);
		inputMap.put(KeyStroke.getKeyStroke(Action.X.toString()), Action.X);

		actionMap.put(Action.UP, new AbstractAction() {

			private static final long serialVersionUID = -4035258231309741970L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleUp();
			}

		});
		actionMap.put(Action.RIGHT, new AbstractAction() {

			private static final long serialVersionUID = 407938813491787305L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleRight();
			}

		});
		actionMap.put(Action.DOWN, new AbstractAction() {

			private static final long serialVersionUID = 4993801021049655318L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleDown();
			}

		});
		actionMap.put(Action.LEFT, new AbstractAction() {

			private static final long serialVersionUID = 6666716265151808233L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleLeft();
			}

		});
		actionMap.put(Action.ENTER, new AbstractAction() {

			private static final long serialVersionUID = -2253852508738647202L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleEnter();
			}

		});
		actionMap.put(Action.ESCAPE, new AbstractAction() {

			private static final long serialVersionUID = 5827916790133403430L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleEsc();
			}

		});
		actionMap.put(Action.BACK_SPACE, new AbstractAction() {

			private static final long serialVersionUID = 6253776701930914853L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleBackspace();
			}

		});
		actionMap.put(Action.SPACE, new AbstractAction() {

			private static final long serialVersionUID = 4561198885927625005L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSpace();
			}

		});
		actionMap.put(Action.X, new AbstractAction() {

			private static final long serialVersionUID = 3176448439753484377L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleX();
			}

		});
		mapEditorScreen.setInputMap(JComponent.WHEN_FOCUSED, inputMap);
		mapEditorScreen.setActionMap(actionMap);
		setContentPane(mapEditorScreen);
	}

	private void createAndAddMenuBar() {
		LOG.debug("Creating menu bar...");
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					handleOpenRequest();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		});

		saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleSaveRequest();
			}
		});

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleExitRequest();
			}
		});

		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		setJMenuBar(menuBar);
	}

	private void createFileChooser() {
		fileChooser = new JFileChooser();
	}

	private void createMapEditorInterface() {
		interfaze = new JPanel(new GridBagLayout());
	}

	private void disableSaveMenuItem() {
		saveMenuItem.setEnabled(false);
	}

	private void drawTile() {
		if (tilePalette.isCursorEnabled()) {
			tileFactory.drawSprite(tilePalette.getSelectedSpriteId(),
					board.getTile(board.getCursorX(), board.getCursorY()));
		}
	}

	private void enableSaveMenuItem() {
		saveMenuItem.setEnabled(true);
	}

	private int getNumberOfPalettes() {
		return interfaze.getComponents().length;
	}

	public void handleBackspace() {
		board.backspace();
	}

	public void handleDown() {
		board.down();
	}

	public void handleEnter() {
		drawTile();
		addDecoration();
		addCritter();
		board.repaint();
	}

	public void handleEsc() {
		board.esc();
	}

	public void handleExitRequest() {
		LOG.debug("Bye");
		System.exit(0);
	}

	public void handleLeft() {
		board.left();
	}

	public void handleOpenRequest() throws IOException {
		LOG.debug("Handling open request...");
		disableSaveMenuItem();
		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			openFile(fileChooser.getSelectedFile());
		} else {
			LOG.debug("Open operation canceled by user");
		}
		enableSaveMenuItem();
	}

	public void handleRight() {
		board.right();
	}

	public void handleSaveRequest() {
		LOG.debug("Handling save request...");
		int returnVal = fileChooser.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			saveFile(fileChooser.getSelectedFile());
		} else {
			LOG.debug("Save operation canceled by user");
		}
	}

	public void handleSpace() {

	}

	public void handleUp() {
		board.up();
	}

	public void handleX() {
		board.x();
	}

	private void openFile(File selectedFile) throws IOException {
		board.load(selectedFile.toURI());
		showAndCenter();
	}

	private void populateMapEditorInterface() {

		addPalette(tilePalette);
		addPalette(decorationPalette);
		addPalette(critterPalette);

		mapEditorScreen.requestFocus();
	}

	private void saveFile(File selectedFile) {
		try {
			FileWriter fileWriter = new FileWriter(selectedFile);
			fileWriter.write(boardSerializer.serialize(board));
			fileWriter.close();
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	@Required
	public void setBoard(MapEditorBoard board) {
		this.board = board;
	}

	@Required
	public void setBoardSerializer(BoardSerializer boardSerializer) {
		this.boardSerializer = boardSerializer;
	}

	@Required
	public void setCritterPalette(MapEditorPalette critterPalette) {
		this.critterPalette = critterPalette;
	}

	@Required
	public void setDecorationPalette(MapEditorPalette decorationPalette) {
		this.decorationPalette = decorationPalette;
	}

	@Required
	public void setFirstBoardResource(Resource firstBoardResource) {
		this.firstBoardResource = firstBoardResource;
	}

	@Required
	public void setPaletteSize(Dimension paletteSize) {
		this.paletteSize = paletteSize;
	}

	@Required
	public void setTileFactory(TileFactory tileFactory) {
		this.tileFactory = tileFactory;
	}

	@Required
	public void setTilePalette(MapEditorPalette tilePalette) {
		this.tilePalette = tilePalette;
	}

	public void showAndCenter() {
		LOG.debug("Showing and centering map editor...");
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
	}
}
