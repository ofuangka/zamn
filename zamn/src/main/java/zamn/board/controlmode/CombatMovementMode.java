package zamn.board.controlmode;

import java.util.List;

import zamn.board.GameBoard;
import zamn.board.Tile;
import zamn.board.piece.Critter;
import zamn.board.tilecollector.CombatMovementTileCollector;
import zamn.board.tilecollector.TileListFilter;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;

public class CombatMovementMode extends AbstractGameBoardControlMode {

	private int origX;
	private int origY;

	public CombatMovementMode(GameBoard board, IEventContext eventContext) {
		super(board, eventContext);
	}

	@Override
	public void configureTileState() {

		GameBoard board = getGameBoard();

		Critter me = board.getControllingCritter();

		// figure out which tiles to enable and enable them
		board.enableTiles(getMovementRange(me));
	}

	@Override
	public void down() {
		tryMove(Action.DOWN);
	}

	@Override
	public void enter() {
		getEventContext().fire(
				GameEventContext.GameEventType.COMBAT_ACTION_MENU_REQUEST);
	}

	@Override
	public void esc() {
		resetToOriginalState();
	}

	private List<Tile> getMovementRange(Critter critter) {
		CombatMovementTileCollector tileCollector = new CombatMovementTileCollector(
				TileListFilter.NULL_TILE_FILTER, critter);
		return tileCollector.collect(getGameBoard().getTile(origX, origY));
	}

	@Override
	public void left() {
		tryMove(Action.LEFT);
	}

	@Override
	public void readAndStoreState() {
		Critter me = getGameBoard().getControllingCritter();

		// save the initial information
		origX = me.getX();
		origY = me.getY();

	}

	@Override
	public void resetToOriginalState() {

		GameBoard board = getGameBoard();

		Critter me = board.getControllingCritter();

		board.placePiece(me, origX, origY);

		board.alignViewport();
	}

	@Override
	public void right() {
		tryMove(Action.RIGHT);
	}

	protected void tryMove(Action dir) {
		GameBoard board = getGameBoard();

		if (board.tryMove(dir)) {
			board.alignViewport();
		}
	}

	@Override
	public void up() {
		tryMove(Action.UP);
	}

}
