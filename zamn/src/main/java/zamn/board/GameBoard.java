package zamn.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import zamn.board.controlmode.AbstractGameBoardControlMode;
import zamn.board.controlmode.Action;
import zamn.board.controlmode.AdventureMode;
import zamn.board.controlmode.CombatMovementMode;
import zamn.board.controlmode.TargetedMove;
import zamn.board.controlmode.TargetingMode;
import zamn.board.piece.Critter;
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
	private List<Critter> critterSequence = new ArrayList<Critter>();
	private List<Tile> tilesInTargetingRange = new ArrayList<Tile>();

	public GameBoard(IEventContext eventContext) {
		this.eventContext = eventContext;
		eventContext.onAll(this);
	}

	public void addExit(int x, int y, Action dir, String[] boardIdAndEntrance) {
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
		eventContext.fire(
				GameEventContext.GameEventType.CRITTER_ASSIGNED_CONTROL,
				critter);
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
		critterSequence.addAll(critters);
		Collections.sort(critterSequence, Critter.SPEED_COMPARATOR);
		Collections.reverse(critterSequence);
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
		critterSequence.remove(deadCritter);
	}

	protected void doOnCritterTargetedActionRequest(EventMenuItem menuItem) {
		pushMode(new TargetingMode(this, getEventContext(),
				(TargetedMove) menuItem.getArg()));
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
		critterSequence.clear();
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
	
	public List<Critter> getCritterSequence() {
		return critterSequence;
	}

	/**
	 * The Board returns whichever current control mode
	 */
	@Override
	public IKeySink getCurrentKeySink() {
		return getCurrentControlMode();
	}

	public AbstractGameBoardControlMode getCurrentControlMode() {
		return modeHistory.get(modeHistory.size() - 1);
	}

	public List<Tile> getCloserTiles(int x1, int y1, int x2, int y2) {
		List<Tile> ret = new ArrayList<Tile>();
		Random random = getEventContext().getRandom();
		if (random.nextBoolean()) {
			if (x1 < x2) {
				ret.add(getTile(x1 + 1, y1));
			} else if (x2 < x1) {
				ret.add(getTile(x1 - 1, y1));
			}
			if (y1 < y2) {
				ret.add(getTile(x1, y1 + 1));
			} else if (y2 < y1) {
				ret.add(getTile(x1, y1 - 1));
			}
		} else {
			if (y1 < y2) {
				ret.add(getTile(x1, y1 + 1));
			} else if (y2 < y1) {
				ret.add(getTile(x1, y1 - 1));
			}
			if (x1 < x2) {
				ret.add(getTile(x1 + 1, y1));
			} else if (x2 < x1) {
				ret.add(getTile(x1 - 1, y1));
			}
		}
		return ret;
	}

	public Action getDir(int x1, int y1, int x2, int y2) {
		if (x1 < x2) {
			return Action.RIGHT;
		} else if (x2 < x1) {
			return Action.LEFT;
		} else if (y1 < y2) {
			return Action.DOWN;
		} else {
			return Action.UP;
		}
	}

	public IEventContext getEventContext() {
		return eventContext;
	}

	public String getExitKey(int x, int y, Action dir) {
		return x + "," + y + "," + dir.toString();
	}

	protected Critter getInitialHero() {
		return critterFactory.get(INITIAL_HERO_ID);
	}

	@Override
	public boolean handleEvent(Event event, Object arg) {
		switch ((GameEventContext.GameEventType) event.getType()) {
		case NEXT_TURN_REQUEST: {
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
			break;
		}
		case LOCATION_CHANGE: {
			doOnLocationChange((String[]) arg);
			break;
		}
		default: {
			break;
		}
		}
		return true;
	}

	private void doOnLocationChange(String[] exit) {
		load(exit[0], Integer.valueOf(exit[1]));
		nextTurn();
	}

	private void executeAi() {
		Critter nearestOpponent = getNearestOpponent(controllingCritter);
		if (nearestOpponent != null) {
			eventContext.fire(
					GameEventContext.GameEventType.TRIGGER_ACTIONS_REQUEST,
					getBestPath(controllingCritter.getX(),
							controllingCritter.getY(), nearestOpponent.getX(),
							nearestOpponent.getY()));
			eventContext.fire(
					GameEventContext.GameEventType.TRIGGER_ACTIONS_REQUEST,
					getBestMove(controllingCritter, nearestOpponent));
		} else {
			throw new IllegalStateException("No nearest opponent found");
		}
	}

	public List<Action> getBestMove(Critter from, Critter to) {
		List<Action> ret = new ArrayList<Action>();
		if (isAdjacent(from.getX(), from.getY(), to.getX(), to.getY())) {
			ret.add(Action.ENTER);
			ret.add(Action.ENTER);
		} else {

			// wait
			ret.add(Action.UP);
			ret.add(Action.ENTER);
		}
		return ret;
	}

	public Critter getNearestOpponent(Critter from) {
		Critter ret = null;
		int minDistance = -1;
		int bufDistance = -1;
		for (int i = 0; i < critters.size(); i++) {
			Critter critter = critters.get(i);
			bufDistance = getDistance(from.getX(), from.getY(), critter.getX(),
					critter.getY());
			if ((minDistance == -1 || bufDistance < minDistance)
					&& (from.isHostile() != critter.isHostile())) {
				minDistance = bufDistance;
				ret = critter;
			}
		}
		return ret;
	}

	protected int getDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	public List<Action> getBestPath(int fromX, int fromY, int toX, int toY) {
		List<Action> ret = new ArrayList<Action>();
		getBestPathHelper(fromX, fromY, toX, toY, ret);
		ret.add(Action.ENTER);
		return ret;
	}

	protected void getBestPathHelper(int fromX, int fromY, int toX, int toY,
			List<Action> acc) {
		if (!isAdjacent(fromX, fromY, toX, toY)) {
			List<Tile> closerTiles = getCloserTiles(fromX, fromY, toX, toY);
			for (Tile tile : closerTiles) {

				if (tile.isEnabled() && tile.isWalkable() && !tile.isOccupied()) {
					acc.add(getDir(fromX, fromY, tile.getX(), tile.getY()));
					getBestPathHelper(tile.getX(), tile.getY(), toX, toY, acc);
					break;
				}
			}
		}
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

			if (critterSequence.isEmpty()) {
				LOG.debug("Critter sequence empty, starting new round...");
				createCritterSequence();
				eventContext.fire(GameEventContext.GameEventType.BEGIN_ROUND);
			}
			

			// figure out who the next controlling piece is
			assignControl(critterSequence.remove(0));

			int beforeMp = controllingCritter.getStat(Critter.Stat.MP);
			if (controllingCritter.getStat(Critter.Stat.MAXMP) > beforeMp) {
				controllingCritter.setStat(Critter.Stat.MP, beforeMp + 1);
			}

			AbstractGameBoardControlMode firstMode;

			if (isInCombat()) {

				// detect and set up the first control mode
				firstMode = new CombatMovementMode(this, getEventContext());

				pushMode(firstMode);

				if (controllingCritter.isHostile()) {

					executeAi();

				} else {
					// wait for user input
				}
			} else {

				firstMode = new AdventureMode(this, getEventContext());

				pushMode(firstMode);

			}
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

	@Override
	public boolean tryMove(AbstractBoardPiece piece, Action dir) {
		if (!isInCombat()) {
			String exitKey = getExitKey(piece.getX(), piece.getY(), dir);
			if (exits.containsKey(exitKey)) {
				String[] exit = exits.get(exitKey);
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
	public boolean tryMove(Action dir) {
		return tryMove(controllingCritter, dir);
	}

	@Override
	public boolean isListening() {
		return controllingCritter == null || !controllingCritter.isHostile();
	}
}
