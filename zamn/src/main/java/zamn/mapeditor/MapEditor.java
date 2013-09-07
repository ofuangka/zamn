package zamn.mapeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import zamn.board.Tile;
import zamn.board.controlmode.Action;
import zamn.creation.BoardSerializer;

public class MapEditor extends JFrame {

	private static final Logger LOG = Logger.getLogger(MapEditor.class);

	private static final long serialVersionUID = -907502337742442255L;

	private MapEditorBoard board;
	private BoardSerializer boardSerializer;
	private JFileChooser fileChooser;
	private JPanel interfaze;
	private JSplitPane mapEditorScreen;
	private Map<String, MapEditorPalette> paletteMap;
	private JMenuItem saveMenuItem;
	private JLabel spacer;
	private JTextField tileInspector;
	private Dimension paletteSize;

	public void bootstrap() {
		LOG.debug("Bootstrapping...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createAndAddMenuBar();
		disableSaveMenuItem();
		createFileChooser();
		createMapEditorInterface();
		createAndAddMapEditorScreen();
		populateMapEditorInterface();
		setResizable(false);
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
				board.up();
			}

		});
		actionMap.put(Action.RIGHT, new AbstractAction() {

			private static final long serialVersionUID = 407938813491787305L;

			@Override
			public void actionPerformed(ActionEvent e) {
				board.right();
			}

		});
		actionMap.put(Action.DOWN, new AbstractAction() {

			private static final long serialVersionUID = 4993801021049655318L;

			@Override
			public void actionPerformed(ActionEvent e) {
				board.down();
			}

		});
		actionMap.put(Action.LEFT, new AbstractAction() {

			private static final long serialVersionUID = 6666716265151808233L;

			@Override
			public void actionPerformed(ActionEvent e) {
				board.left();
			}

		});
		actionMap.put(Action.ENTER, new AbstractAction() {

			private static final long serialVersionUID = -2253852508738647202L;

			@Override
			public void actionPerformed(ActionEvent e) {
			}

		});
		actionMap.put(Action.ESCAPE, new AbstractAction() {

			private static final long serialVersionUID = 5827916790133403430L;

			@Override
			public void actionPerformed(ActionEvent e) {
				board.esc();
			}

		});
		actionMap.put(Action.BACK_SPACE, new AbstractAction() {

			private static final long serialVersionUID = 6253776701930914853L;

			@Override
			public void actionPerformed(ActionEvent e) {
				board.backspace();
			}

		});
		actionMap.put(Action.SPACE, new AbstractAction() {

			private static final long serialVersionUID = 4561198885927625005L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Tile currentTile = board.getCurrentTile();
				board.applyTerrainToTile(paletteMap.get("tiles")
						.getSelectedSpriteId(), currentTile);

			}

		});
		actionMap.put(Action.X, new AbstractAction() {

			private static final long serialVersionUID = 3176448439753484377L;

			@Override
			public void actionPerformed(ActionEvent e) {
				board.x();
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

	private void enableSaveMenuItem() {
		saveMenuItem.setEnabled(true);
	}

	public void handleExitRequest() {
		LOG.debug("Bye");
		System.exit(0);
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

	public void handleSaveRequest() {
		LOG.debug("Handling save request...");
		int returnVal = fileChooser.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			saveFile(fileChooser.getSelectedFile());
		} else {
			LOG.debug("Save operation canceled by user");
		}
	}

	private void openFile(File selectedFile) throws IOException {
		board.load(selectedFile.toURI());
		showAndCenter();
	}

	private void populateMapEditorInterface() {
		if (paletteMap != null) {
			Collection<MapEditorPalette> palettes = paletteMap.values();
			GridBagConstraints gc;
			int numPalettes = palettes.size();
			Iterator<MapEditorPalette> iter = palettes.iterator();
			int count = 0;
			while (iter.hasNext()) {
				MapEditorPalette palette = iter.next();
				palette.setPreferredSize(paletteSize);
				gc = new GridBagConstraints();
				gc.gridx = 0;
				gc.gridy = count++;
				gc.weightx = 1;
				gc.fill = GridBagConstraints.HORIZONTAL;
				interfaze.add(palette, gc);
			}
			spacer = new JLabel();
			gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = numPalettes + 1;
			gc.weighty = 1;
			interfaze.add(spacer, gc);

			tileInspector = new JTextField();
			gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = numPalettes + 2;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.weightx = 1;
			interfaze.add(tileInspector, gc);

			mapEditorScreen.requestFocus();
		} else {
			throw new IllegalStateException("Palette map cannot be null");
		}
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
	public void setPaletteMap(Map<String, MapEditorPalette> paletteMap) {
		this.paletteMap = paletteMap;
	}

	@Required
	public void setPaletteSize(Dimension paletteSize) {
		this.paletteSize = paletteSize;
	}

	public void showAndCenter() {
		LOG.debug("Showing and centering map editor...");
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
	}
}
