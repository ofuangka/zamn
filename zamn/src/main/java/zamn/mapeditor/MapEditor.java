package zamn.mapeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

public class MapEditor extends JFrame {

	private static final Logger LOG = Logger.getLogger(MapEditor.class);

	private static final long serialVersionUID = -907502337742442255L;

	private MapEditorBoard board;
	private JFileChooser fileChooser;
	private JMenuItem saveMenuItem;

	public void bootstrap() {
		LOG.debug("Bootstrapping...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createAndAddMenuBar();
		disableSaveMenuItem();
		createFileChooser();
	}

	private void createAndAddMenuBar() {
		LOG.debug("Creating menu bar...");
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleOpenRequest();
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

	public void handleOpenRequest() {
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

	private void openFile(File selectedFile) {
		board.load(selectedFile.toURI());
	}

	private void saveFile(File selectedFile) {

	}
}
