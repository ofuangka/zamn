package zamn.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import zamn.board.controlmode.AbstractGameBoardControlMode;
import zamn.board.controlmode.AdventureMode;
import zamn.board.controlmode.CombatMovementMode;
import zamn.board.controlmode.TargetedAction;
import zamn.board.controlmode.TargetingMode;
import zamn.board.piece.Critter;
import zamn.common.Direction;
import zamn.creation.BoardLoader;
import zamn.creation.CritterFactory;
import zamn.framework.event.Event;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;
import zamn.framework.event.IEventHandler;
import zamn.ui.IKeySink;
import zamn.ui.menu.EventMenuItem;

public class GameBoard extends AbstractViewportBoard implements IEventHandler {

	private static final String INITIAL_BOARD_ID = "goStraight";
	private static final String INITIAL_HERO_ID = "mainCharacter";
	private static final Logger LOG = Logger.getLogger(GameBoard.class);
	private static final long serialVersionUID = -6548852244995136036L;

	private BoardLoader boardLoader;
	private Critter controllingCritter;
	private CritterFactory critterFactory;
	private List<Critter> critters = new ArrayList<Critter>();
	private List<Tile> crosshairTiles = new ArrayList<Tile>();
	private List<Tile> disabledTiles = new ArrayList<Tile>();
	private IEventContext eventContext;
	private Map<String, String[]> exits = new HashMap<String, String[]>();
	private List<AbstractGameBoardControlMode> modeHistory = new ArrayList<AbstractGameBoardControlMode>();
	private List<Critter> sequence = new ArrayList<Critter>();
	private List<Tile> tilesInTargetingRange = new ArrayList<Tile>();

	public GameBoard(IEventContext eventContext) {
		this.eventContext = eventContext;
		eventContext.onAll(this);
	}

	public void addExit(int x, int y, Direction dir, String[] boardIdAndEntrance) {
		exits.put(getExitKey(x, y, dir), boardIdAndEntrance);
	}

	/**
	 * Aligns the viewport to the currently controlling critter
	 */
	public void alignViewport() {
		alignViewport(controllingCritter.getX(), controllingCritter.getY());
	}

	public void assignControl(Critter critter) {
		LOG.debug("Assigning control to Critter " + critter + "...");
		if (controllingCritter != null) {
			controllingCritter.setSelected(false);
		}
		controllingCritter = critter;
		controllingCritter.setSelected(true);

		alignViewport();
		repaint();
	}

	public void clearCrosshairUi() {
		LOG.debug("Removing all crosshair tiles...");
		while (!crosshairTiles.isEmpty()) {
			crosshairTiles.remove(0).setCrosshair(false);
		}
		repaint();
	}

	public void clearDisabledTileUi() {
		LOG.debug("Removing all out of range tiles...");
		while (!disabledTiles.isEmpty()) {
			disabledTiles.remove(0).setEnabled(true);
		}
		repaint();
	}

	public void clearTargetingRangeUi() {
		LOG.debug("Clearing targetable Tile objects...");
		while (!tilesInTargetingRange.isEmpty()) {
			tilesInTargetingRange.remove(0).setInTargetingRange(false);
		}
		repaint();
	}

	public void createCritterSequence() {
		sequence.addAll(critters);
		Collections.sort(sequence, Critter.SPEED_COMPARATOR);
		Collections.reverse(sequence);
	}

	public void disableAllTiles() {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				tiles[x][y].setEnabled(false);
				disabledTiles.add(tiles[x][y]);
			}
		}
		repaint();
	}

	private void doOnCoordinatesRequest() {
		LOG.info(controllingCritter.getX() + ", " + controllingCritter.getY());
	}

	private void doOnCritterAddedToBoard(Critter newCritter) {
		critters.add(newCritter);
	}

	protected void doOnCritterDeath(Critter deadCritter) {
		removePiece(deadCritter);
		critters.remove(deadCritter);
		sequence.remove(deadCritter);
	}

	protected void doOnCritterTargetedActionRequest(EventMenuItem menuItem) {
		pushMode(new TargetingMode(this, getEventContext(),
				(TargetedAction) menuItem.getArg()));
	}

	protected void doOnEndOfTurn() {
		nextTurn();
	}

	protected void doOnNewGameRequest() {
		forceClearBoardState();
		eventContext.fire(GameEventContext.GameEventType.HERO_JOIN_REQUEST,
				getInitialHero());
		load(INITIAL_BOARD_ID);
		nextTurn();
	}

	public void enableTiles(List<Tile> tilesToEnable) {
		disableAllTiles();
		if (tilesToEnable != null) {
			for (Tile tile : tilesToEnable) {
				tile.setEnabled(true);
				disabledTiles.remove(tile);
			}
		}
		repaint();
	}

	/**
	 * Clears the board state without clearing the game state, ignoring the UI
	 * state
	 */
	protected void forceClearBoardState() {
		critters.clear();
		sequence.clear();
		disabledTiles.clear();
		tilesInTargetingRange.clear();
		modeHistory.clear();
		exits.clear();
		tiles = null;
		controllingCritter = null;
	}

	public Critter getControllingCritter() {
		return controllingCritter;
	}

	/**
	 * The Board returns whichever current control mode
	 */
	@Override
	public IKeySink getCurrentKeySink() {
		return modeHistory.get(modeHistory.size() - 1);
	}

	public IEventContext getEventContext() {
		return eventContext;
	}

	public String getExitKey(int x, int y, Direction dir) {
		return x + "," + y + "," + dir.toString();
	}

	protected Critter getInitialHero() {
		return critterFactory.get(INITIAL_HERO_ID);
	}

	@Override
	public boolean handleEvent(Event event, Object arg) {
		switch ((GameEventContext.GameEventType) event.getType()) {
		case END_OF_TURN: {
			doOnEndOfTurn();
			break;
		}
		case NEW_GAME_REQUEST: {
			doOnNewGameRequest();
			break;
		}
		case CRITTER_DEATH: {
			doOnCritterDeath((Critter) arg);
			break;
		}
		case CRITTER_TARGETED_ACTION_REQUEST: {
			doOnCritterTargetedActionRequest((EventMenuItem) arg);
			break;
		}
		case CRITTER_ADDED_TO_BOARD: {
			doOnCritterAddedToBoard((Critter) arg);
			break;
		}
		case COORDINATES_REQUEST: {
			doOnCoordinatesRequest();
		}
		default: {
			break;
		}
		}
		return true;
	}

	public boolean isAtLeastOneHeroOnBoard() {
		boolean ret = false;
		for (Critter critter : critters) {
			if (!critter.isHostile()) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public boolean isInCombat() {
		boolean ret = false;
		for (Critter critter : critters) {
			if (critter.isHostile()) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public void load(String boardId) {
		load(boardId, 0);
	}

	public void load(String boardId, int entryPoint) {
		LOG.info("Loading board " + boardId + "[" + entryPoint + "]...");
		forceClearBoardState();
		try {
			boardLoader.load(boardId, entryPoint, this);
		} catch (IOException e) {
			LOG.error("Could not load board with ID: '" + boardId + "'", e);
		}
	}

	public void nextTurn() {
		LOG.debug("Starting new turn...");

		clearDisabledTileUi();
		clearTargetingRangeUi();
		clearCrosshairUi();

		modeHistory.clear();

		// check end conditions
		if (!isAtLeastOneHeroOnBoard()) {
			getEventContext().fire(
					GameEventContext.GameEventType.LOSE_CONDITION);
		} else {

			if (sequence.isEmpty()) {
				LOG.debug("Critter sequence empty, starting new round...");
				createCritterSequence();
			}

			// figure out who the next controlling piece is
			assignControl(sequence.remove(0));

			AbstractGameBoardControlMode firstMode;

			if (isInCombat()) {

				// detect and set up the first control mode
				firstMode = new CombatMovementMode(this, getEventContext());

				// wait for user input
			} else {

				firstMode = new AdventureMode(this, getEventContext());

			}

			pushMode(firstMode);
		}
	}

	public void popMode() {
		modeHistory.remove(modeHistory.size() - 1);
		refreshMode();
	}

	public void pushMode(AbstractGameBoardControlMode mode) {
		mode.readAndStoreState();
		mode.configureTileState();
		modeHistory.add(mode);
	}

	public void refreshMode() {
		modeHistory.get(modeHistory.size() - 1).configureTileState();
	}

	public void renderCrosshair(List<Tile> tilesInCrosshair) {
		clearCrosshairUi();
		if (tilesInCrosshair != null) {
			for (Tile tile : tilesInCrosshair) {
				tile.setCrosshair(true);
				crosshairTiles.add(tile);
			}
			repaint();
		} else {
			LOG.warn("targetTiles called with null Tile List");
		}
	}

	@Required
	public void setBoardLoader(BoardLoader boardLoader) {
		this.boardLoader = boardLoader;
	}

	@Required
	public void setCritterFactory(CritterFactory critterFactory) {
		this.critterFactory = critterFactory;
	}

	public void setTargetingRange(List<Tile> targetingRange) {
		clearTargetingRangeUi();
		if (targetingRange != null) {
			for (Tile tile : targetingRange) {
				tile.setInTargetingRange(true);
				tilesInTargetingRange.add(tile);
			}
			repaint();
		} else {
			LOG.warn("setTargetable called with null Tile List");
		}
	}

	public void stepNeutralCritters() {

	}

	@Override
	public boolean tryMove(AbstractBoardPiece piece, Direction dir) {
		if (!isInCombat()) {
			String exitKey = getExitKey(piece.getX(), piece.getY(), dir);
			if (exits.containsKey(exitKey)) {
				String[] exit = exits.get(exitKey);
				load(exit[0], Integer.valueOf(exit[1]));
				nextTurn();
				eventContext.fire(
						GameEventContext.GameEventType.LOCATION_CHANGE, exit);
				return false;
			} else {
				return super.tryMove(piece, dir);
			}
		} else {
			return super.tryMove(piece, dir);
		}
	}

	/**
	 * Moves the controlling critter in the direction provided
	 * 
	 * @param dir
	 * @return
	 */
	public boolean tryMove(Direction dir) {
		return tryMove(controllingCritter, dir);
	}
}
