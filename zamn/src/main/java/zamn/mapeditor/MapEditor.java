package zamn.mapeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import zamn.board.controlmode.Action;

public class MapEditor extends JFrame {

	private static final Logger LOG = Logger.getLogger(MapEditor.class);

	private static final long serialVersionUID = -907502337742442255L;

	private MapEditorScreen mapEditorScreen;
	private MapEditorBoard board;
	private JFileChooser fileChooser;
	private JMenuItem saveMenuItem;

	public void bootstrap() {
		LOG.debug("Bootstrapping...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createAndAddMenuBar();
		disableSaveMenuItem();
		createFileChooser();
		configureAndAddMapEditorScreen();
		setResizable(false);
	}

	private void configureAndAddMapEditorScreen() {
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
				board.enter();
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
				board.space();
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
		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			openFile(fileChooser.getSelectedFile());
		} else {
			LOG.debug("Open operation canceled by user");
		}
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

	private void saveFile(File selectedFile) {

	}

	@Required
	public void setBoard(MapEditorBoard board) {
		this.board = board;
	}

	@Required
	public void setMapEditorScreen(MapEditorScreen mapEditorScreen) {
		this.mapEditorScreen = mapEditorScreen;
	}

	public void showAndCenter() {
		LOG.debug("Showing and centering mapEditor...");
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
		mapEditorScreen.requestFocus();
	}
}
