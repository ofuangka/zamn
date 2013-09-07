package zamn.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import zamn.creation.GameBoardLoader;
import zamn.framework.event.Event;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;
import zamn.framework.event.IEventHandler;
import zamn.ui.IDelegatingKeySink;
import zamn.ui.IKeySink;
import zamn.ui.menu.EventMenuItem;

public class GameBoard extends AbstractViewportBoard implements IEventHandler,
		IDelegatingKeySink {

	private static final String INITIAL_BOARD_ID = "goStraight";
	private static final String INITIAL_HERO_ID = "mainCharacter";
	private static final Logger LOG = Logger.getLogger(GameBoard.class);
	private static final long serialVersionUID = -6548852244995136036L;

	private BoardLoader boardLoader;
	private IBoardUriResolver boardUriResolver;
	private Critter controllingCritter;
	private CritterFactory critterFactory;
	private List<Critter> critterSequence = new ArrayList<Critter>();
	private List<Tile> crosshairTiles = new ArrayList<Tile>();
	private List<Tile> disabledTiles = new ArrayList<Tile>();
	private IEventContext eventContext;
	private List<AbstractGameBoardControlMode> modeHistory = new ArrayList<AbstractGameBoardControlMode>();
	private List<Tile> tilesInTargetingRange = new ArrayList<Tile>();

	public GameBoard(IEventContext eventContext) {
		this.eventContext = eventContext;
		eventContext.onAll(this);
	}

	public void addBestMove(int fromX, int fromY, Critter opponent,
			List<Action> actions) {
		if (isAdjacent(fromX, fromY, opponent.getX(), opponent.getY())) {
			actions.add(Action.ENTER);
			actions.add(Action.ENTER);
		} else {

			// wait
			actions.add(Action.UP);
			actions.add(Action.ENTER);
		}
	}

	public int[] addBestPath(int fromX, int fromY, int toX, int toY,
			List<Action> acc) {
		int[] ret = addBestPathHelper(fromX, fromY, toX, toY, acc);
		acc.add(Action.ENTER);
		return ret;
	}

	protected int[] addBestPathHelper(int fromX, int fromY, int toX, int toY,
			List<Action> acc) {
		if (!isAdjacent(fromX, fromY, toX, toY)) {
			List<Tile> closerTiles = getCloserTiles(fromX, fromY, toX, toY);
			for (Tile tile : closerTiles) {

				if (tile.isEnabled() && !tile.isSolid() && !tile.isOccupied()) {
					acc.add(getDir(fromX, fromY, tile.getX(), tile.getY()));
					return addBestPathHelper(tile.getX(), tile.getY(), toX,
							toY, acc);
				}
			}
		}
		return new int[] { fromX, fromY };
	}

	@Override
	public void addCritter(Critter critter) {
		super.addCritter(critter);
		eventContext.fire(
				GameEventContext.GameEventType.CRITTER_ADDED_TO_BOARD, critter);
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

	@Override
	public void backspace() {
		getCurrentKeySink().backspace();
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

	@Override
	public void down() {
		getCurrentKeySink().down();
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

	@Override
	public void enter() {
		getCurrentKeySink().enter();
	}

	@Override
	public void esc() {
		getCurrentKeySink().esc();
	}

	private void executeAi() {
		Critter nearestOpponent = getNearestOpponent(controllingCritter);
		if (nearestOpponent != null) {
			List<Action> aiActions = new ArrayList<Action>();
			int[] finalPosition = addBestPath(controllingCritter.getX(),
					controllingCritter.getY(), nearestOpponent.getX(),
					nearestOpponent.getY(), aiActions);
			addBestMove(finalPosition[0], finalPosition[1], nearestOpponent,
					aiActions);
			eventContext.fire(
					GameEventContext.GameEventType.TRIGGER_ACTIONS_REQUEST,
					aiActions);
		} else {
			throw new IllegalStateException("No nearest opponent found");
		}
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

	public Critter getControllingCritter() {
		return controllingCritter;
	}

	public AbstractGameBoardControlMode getCurrentControlMode() {
		return modeHistory.get(modeHistory.size() - 1);
	}

	/**
	 * The Board returns whichever current control mode
	 */
	@Override
	public IKeySink getCurrentKeySink() {
		return getCurrentControlMode();
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

	protected int getDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	public IEventContext getEventContext() {
		return eventContext;
	}

	protected Critter getInitialHero() {
		return critterFactory.get(INITIAL_HERO_ID);
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

	private void handleCoordinatesRequest() {
		LOG.info(controllingCritter.getX() + ", " + controllingCritter.getY());
	}

	protected void handleCritterDeath(Critter deadCritter) {
		removePiece(deadCritter);
		critters.remove(deadCritter);
		critterSequence.remove(deadCritter);
	}

	protected void handleCritterTargetedActionRequest(EventMenuItem menuItem) {
		pushMode(new TargetingMode(this, getEventContext(),
				(TargetedMove) menuItem.getArg()));
	}

	protected void handleEndOfTurn() {
		nextTurn();
	}

	@Override
	public boolean handleEvent(Event event, Object arg) {
		switch ((GameEventContext.GameEventType) event.getType()) {
		case NEXT_TURN_REQUEST: {
			handleEndOfTurn();
			break;
		}
		case NEW_GAME_REQUEST: {
			handleNewGameRequest();
			break;
		}
		case CRITTER_DEATH: {
			handleCritterDeath((Critter) arg);
			break;
		}
		case CRITTER_TARGETED_ACTION_REQUEST: {
			handleCritterTargetedActionRequest((EventMenuItem) arg);
			break;
		}
		case COORDINATES_REQUEST: {
			handleCoordinatesRequest();
			break;
		}
		case LOCATION_CHANGE: {
			handleLocationChange((String[]) arg);
			break;
		}
		default: {
			break;
		}
		}
		return true;
	}

	private void handleLocationChange(String[] exit) {
		load(exit[0], Integer.valueOf(exit[1]));
		nextTurn();
	}

	protected void handleNewGameRequest() {
		forceClearBoardState();
		eventContext.fire(GameEventContext.GameEventType.HERO_JOIN_REQUEST,
				getInitialHero());
		load(INITIAL_BOARD_ID);
		nextTurn();
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

	@Override
	public boolean isListening() {
		return controllingCritter == null || !controllingCritter.isHostile();
	}

	@Override
	protected boolean isTileOpen(Tile tile) {
		return super.isTileOpen(tile) && !tile.isSolid() && tile.isEnabled()
				&& !tile.isOccupied();
	}

	@Override
	public void left() {
		getCurrentKeySink().left();
	}

	public void load(String boardId) {
		load(boardId, 0);
	}

	public void load(String boardId, int entryPoint) {
		LOG.info("Loading board " + boardId + "[" + entryPoint + "]...");
		forceClearBoardState();
		try {
			((GameBoardLoader) boardLoader).load(
					boardUriResolver.resolve(boardId), this, entryPoint);
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
				eventContext.fire(GameEventContext.GameEventType.BEGIN_ROUND,
						critterSequence);
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

	@Override
	public void right() {
		getCurrentKeySink().right();
	}

	@Required
	public void setBoardLoader(BoardLoader boardLoader) {
		this.boardLoader = boardLoader;
	}

	@Required
	public void setBoardUriResolver(IBoardUriResolver boardUriResolver) {
		this.boardUriResolver = boardUriResolver;
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
	public void space() {
		getCurrentKeySink().space();
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
	public void up() {
		getCurrentKeySink().up();
	}

	@Override
	public void x() {
		getCurrentKeySink().x();
	}
}
