package zamn;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zamn.board.GameBoard;
import zamn.common.Direction;
import zamn.common.ZamnConstants;
import zamn.framework.event.Event;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;
import zamn.framework.event.IEventHandler;
import zamn.ui.IKeySink;
import zamn.ui.InGameMenuLayer;
import zamn.ui.menu.CritterMenuFactory;
import zamn.ui.menu.EventMenuItem;
import zamn.ui.menu.Menu;

public class Zamn implements IEventHandler {

	private static final String APP_CTX_PATH = "spring-context.xml";

	private static final Logger LOG = Logger.getLogger(Zamn.class);
	private static final String ZAMN_BEAN_ID = "zamn";

	public static void main(String[] args) {
		ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(
				APP_CTX_PATH);
		Zamn zamn = (Zamn) appCtx.getBean(ZAMN_BEAN_ID);

		appCtx.close();

		zamn.bootstrap();
	}

	private GameBoard board;
	private CritterMenuFactory critterMenuFactory;
	private IKeySink currentKeySink;
	private IEventContext eventContext;
	private JComponent gameScreen;
	private Menu gameOverMenu;
	private InGameMenuLayer inGameMenuLayer;
	private Menu mainMenu;
	private JPanel screenPanel;
	private List<JComponent> shownScreens = new ArrayList<JComponent>();

	private Menu systemMenu;

	private JFrame window;

	// configuration variables
	private Dimension windowSize;

	private String windowTitle;

	public void bootstrap() {

		// attach to events
		eventContext.onAll(this);
		createWindow();
		createScreenPanel();
		showScreen(mainMenu);
		showAndCenterWindow();

	}

	/**
	 * This method creates a content pane and configures it to funnel key events
	 * to the current keysink
	 */
	private void createScreenPanel() {
		LOG.debug("Initializing screen panel...");
		screenPanel = new JPanel(new GridBagLayout());

		// create input and action maps
		InputMap inputMap = new InputMap();
		ActionMap actionMap = new ActionMap();

		inputMap.put(KeyStroke.getKeyStroke(Direction.UP.toString()),
				Direction.UP);
		inputMap.put(KeyStroke.getKeyStroke(Direction.RIGHT.toString()),
				Direction.RIGHT);
		inputMap.put(KeyStroke.getKeyStroke(Direction.DOWN.toString()),
				Direction.DOWN);
		inputMap.put(KeyStroke.getKeyStroke(Direction.LEFT.toString()),
				Direction.LEFT);
		inputMap.put(KeyStroke.getKeyStroke(ZamnConstants.ENTER),
				ZamnConstants.ENTER);
		inputMap.put(KeyStroke.getKeyStroke(ZamnConstants.ESCAPE),
				ZamnConstants.ESCAPE);
		inputMap.put(KeyStroke.getKeyStroke(ZamnConstants.BACKSPACE),
				ZamnConstants.BACKSPACE);
		inputMap.put(KeyStroke.getKeyStroke(ZamnConstants.SPACE),
				ZamnConstants.SPACE);
		inputMap.put(KeyStroke.getKeyStroke(ZamnConstants.X), ZamnConstants.X);

		actionMap.put(Direction.UP, new AbstractAction() {

			private static final long serialVersionUID = -4035258231309741970L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentKeySink.up();
			}

		});
		actionMap.put(Direction.RIGHT, new AbstractAction() {

			private static final long serialVersionUID = 407938813491787305L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentKeySink.right();
			}

		});
		actionMap.put(Direction.DOWN, new AbstractAction() {

			private static final long serialVersionUID = 4993801021049655318L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentKeySink.down();
			}

		});
		actionMap.put(Direction.LEFT, new AbstractAction() {

			private static final long serialVersionUID = 6666716265151808233L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentKeySink.left();
			}

		});
		actionMap.put(ZamnConstants.ENTER, new AbstractAction() {

			private static final long serialVersionUID = -2253852508738647202L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentKeySink.enter();
			}

		});
		actionMap.put(ZamnConstants.ESCAPE, new AbstractAction() {

			private static final long serialVersionUID = 5827916790133403430L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentKeySink.esc();
			}

		});
		actionMap.put(ZamnConstants.BACKSPACE, new AbstractAction() {

			private static final long serialVersionUID = 6253776701930914853L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentKeySink.backspace();
			}

		});
		actionMap.put(ZamnConstants.SPACE, new AbstractAction() {

			private static final long serialVersionUID = 4561198885927625005L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentKeySink.space();
			}

		});
		actionMap.put(ZamnConstants.X, new AbstractAction() {

			private static final long serialVersionUID = 3176448439753484377L;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentKeySink.x();
			}

		});

		screenPanel.setInputMap(JComponent.WHEN_FOCUSED, inputMap);
		screenPanel.setActionMap(actionMap);
		window.setContentPane(screenPanel);
	}

	/**
	 * Creates and configures the window
	 */
	private void createWindow() {
		LOG.debug("Initializing window...");
		window = new JFrame(windowTitle);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void doOnCombatActionMenuRequest() {
		inGameMenuLayer.pushMenu(critterMenuFactory.getCombatActionMenu(board
				.getControllingCritter()));
	}

	private void doOnCritterTargetedActionRequest(EventMenuItem menuItem) {
		inGameMenuLayer.setVisible(false);
	}

	private void doOnEndOfTurn() {
		inGameMenuLayer.clearMenus();
		inGameMenuLayer.setVisible(true);
	}

	private void doOnExitRequest() {
		System.exit(0);
	}

	private void doOnLoseCondition() {
		showScreen(gameOverMenu);
	}

	private void doOnMainMenuRequest() {
		showScreen(mainMenu);
	}

	protected void doOnNewGameRequest() {
		showScreen(gameScreen);
	}

	private void doOnPreviousInGameMenuRequest() {
		if (inGameMenuLayer.isVisible()) {
			inGameMenuLayer.popMenu();
		} else {
			inGameMenuLayer.setVisible(true);
		}
	}

	private void doOnPushInGameSubMenu(Menu menu) {
		inGameMenuLayer.pushMenu(menu);
	}

	private void doOnReturnToGameRequest() {
		showScreen(gameScreen);
	}

	private void doOnSystemMenuRequest() {
		showScreen(systemMenu);
	}

	/**
	 * This method gets called before a screen is shown for the first time
	 * 
	 * @param screen
	 */
	private void firstTimeConfigureScreen(JComponent screen) {
		screen.setPreferredSize(windowSize);
	}

	@Override
	public boolean handleEvent(Event event, Object arg) {
		switch ((GameEventContext.GameEventType) event.getType()) {
		case COMBAT_ACTION_MENU_REQUEST: {
			doOnCombatActionMenuRequest();
			break;
		}
		case END_OF_TURN: {
			doOnEndOfTurn();
			break;
		}
		case EXIT_GAME_REQUEST: {
			doOnExitRequest();
			break;
		}
		case NEW_GAME_REQUEST: {
			doOnNewGameRequest();
			break;
		}
		case LOSE_CONDITION: {
			doOnLoseCondition();
			break;
		}
		case PUSH_IN_GAME_SUBMENU_REQUEST: {
			doOnPushInGameSubMenu((Menu) arg);
			break;
		}
		case PREVIOUS_IN_GAME_MENU_REQUEST: {
			doOnPreviousInGameMenuRequest();
			break;
		}
		case RETURN_TO_GAME_REQUEST: {
			doOnReturnToGameRequest();
			break;
		}
		case MAIN_MENU_REQUEST: {
			doOnMainMenuRequest();
			break;
		}
		case SYSTEM_MENU_REQUEST: {
			doOnSystemMenuRequest();
			break;
		}
		case CRITTER_TARGETED_ACTION_REQUEST: {
			doOnCritterTargetedActionRequest((EventMenuItem) arg);
			break;
		}

		default: {
			break;
		}
		}
		return true;
	}

	@Required
	public void setBoard(GameBoard board) {
		this.board = board;
	}

	@Required
	public void setCritterMenuFactory(CritterMenuFactory critterMenuFactory) {
		this.critterMenuFactory = critterMenuFactory;
	}

	@Required
	public void setEventContext(IEventContext eventContext) {
		this.eventContext = eventContext;
	}

	@Required
	public void setGameOverMenu(Menu gameOverMenu) {
		this.gameOverMenu = gameOverMenu;
	}

	public void setGameScreen(JComponent gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Required
	public void setInGameMenuLayer(InGameMenuLayer inGameMenuLayer) {
		this.inGameMenuLayer = inGameMenuLayer;
	}

	@Required
	public void setMainMenu(Menu mainMenu) {
		this.mainMenu = mainMenu;
	}

	@Required
	public void setSystemMenu(Menu systemMenu) {
		this.systemMenu = systemMenu;
	}

	public void setWindowSize(Dimension windowSize) {
		this.windowSize = windowSize;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	/**
	 * Places the window in the center of the screen and sets it visible
	 */
	private void showAndCenterWindow() {
		LOG.debug("Showing and centering window...");
		window.setVisible(true);
		window.pack();
		window.setLocationRelativeTo(null);
		screenPanel.requestFocus();
	}

	/**
	 * Replaces all contents of the window with the argument JComponent, sizing
	 * it to the window size. If the screen is assignable from IKeySink, this
	 * method sets up the user input, setting currentKeySink. If the screen is
	 * not assignable from IKeySink, this method sets the currentKeySink equal
	 * to the preconfigured fallbackKeySink.
	 * 
	 * @param screen
	 */
	private void showScreen(JComponent screen) {
		LOG.info("Showing screen " + screen + "...");

		// check if the screen has already been added once
		if (!shownScreens.contains(screen)) {
			firstTimeConfigureScreen(screen);
			shownScreens.add(screen);
		}
		if (IKeySink.class.isAssignableFrom(screen.getClass())) {
			currentKeySink = (IKeySink) screen;
		} else {
			throw new IllegalArgumentException("Screen must implement IKeySink");
		}
		if (Menu.class.isAssignableFrom(screen.getClass())) {
			((Menu) screen).reset();
		}
		screenPanel.removeAll();
		screenPanel.add(screen, new GridBagConstraints());
		screenPanel.revalidate();
		screenPanel.repaint();
	}
}
