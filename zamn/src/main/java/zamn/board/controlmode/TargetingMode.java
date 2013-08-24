package zamn.board.controlmode;

import java.util.List;

import org.apache.log4j.Logger;

import zamn.board.GameBoard;
import zamn.board.Tile;
import zamn.board.piece.Critter;
import zamn.board.tilecollector.ITileCollector;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;

public class TargetingMode extends AbstractGameBoardControlMode {

	private static final Logger LOG = Logger.getLogger(TargetingMode.class);

	public static final int NO_SELECTABLE_TILE_INDEX = -1;

	private final TargetedMove action;
	private List<Tile> actualRange;
	private int currentIndex;

	public TargetingMode(GameBoard board, IEventContext eventContext,
			TargetedMove action) {
		super(board, eventContext);
		this.action = action;
	}

	@Override
	public void configureTileState() {

		// clear the previously selected tile
		currentIndex = NO_SELECTABLE_TILE_INDEX;

		// get the different range collectors
		Critter controllingCritter = getGameBoard().getControllingCritter();
		Tile targetTile = getGameBoard().getTile(controllingCritter.getX(),
				controllingCritter.getY());
		List<Tile> targetingRange = action.getTargetingRange().collect(
				targetTile);
		if (!targetingRange.isEmpty()) {
			getGameBoard().setTargetingRange(targetingRange);
		}
		actualRange = action.getActualRangeFilter().filter(targetingRange);

		// if anything is in targeting range, render the crosshair on the first
		// potential target
		if (!actualRange.isEmpty()) {
			currentIndex = 0;
			getGameBoard().enableTiles(actualRange);
			renderCrosshair();

		} else {
			// otherwise disable all tiles
			getGameBoard().disableAllTiles();

			// error handling
			LOG.warn("No selectable tiles");
		}

	}

	@Override
	public void down() {
		left();
	}

	@Override
	public void enter() {

		Critter controllingCritter = getGameBoard().getControllingCritter();

		if (currentIndex != NO_SELECTABLE_TILE_INDEX) {
			int beforeMp = controllingCritter.getStat(Critter.Stat.MP);
			int mpCost = action.getMpCost();

			if (beforeMp >= mpCost) {

				controllingCritter.setStat(Critter.Stat.MP, beforeMp - mpCost);

				// get the tile collector and effect

				ITileCollector aoe = action.getAreaOfEffect();
				AbstractEffect tileEffect = action.getEffect();

				List<Tile> affectedTiles = aoe.collect(actualRange
						.get(currentIndex));
				for (Tile tile : affectedTiles) {
					tileEffect.apply(tile);
				}
				LOG.debug("Valid tile selection, ending turn");
				getEventContext().fire(
						GameEventContext.GameEventType.NEXT_TURN_REQUEST);
			} else {
				LOG.warn("Not enough MP");
			}
		} else {
			LOG.warn("Invalid tile selection, please try again");
		}
	}

	@Override
	public void esc() {
		getGameBoard().clearDisabledTileUi();
		getGameBoard().clearTargetingRangeUi();
		getGameBoard().clearCrosshairUi();
		getGameBoard().popMode();
		getEventContext().fire(
				GameEventContext.GameEventType.PREVIOUS_IN_GAME_MENU_REQUEST);
	}

	@Override
	public void left() {
		if (currentIndex != NO_SELECTABLE_TILE_INDEX) {
			getGameBoard().clearCrosshairUi();
			actualRange.get(currentIndex).setCrosshair(false);
			currentIndex = (currentIndex == 0) ? actualRange.size() - 1
					: currentIndex - 1;
			renderCrosshair();
		}
	}

	protected void renderCrosshair() {
		ITileCollector aoe = action.getAreaOfEffect();
		Tile targetTile = actualRange.get(currentIndex);
		List<Tile> crosshairTiles = aoe.collect(targetTile);
		GameBoard board = getGameBoard();
		board.renderCrosshair(crosshairTiles);
		board.alignViewport(targetTile.getX(), targetTile.getY());

	}

	@Override
	public void right() {
		if (currentIndex != NO_SELECTABLE_TILE_INDEX) {
			getGameBoard().clearCrosshairUi();
			actualRange.get(currentIndex).setCrosshair(false);
			currentIndex = (currentIndex == actualRange.size() - 1) ? 0
					: currentIndex + 1;
			renderCrosshair();
		}
	}

	@Override
	public void up() {
		right();
	}

}
