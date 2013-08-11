package zamn.board.controlmode;

import zamn.board.GameBoard;
import zamn.common.Direction;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;

public class AdventureMode extends AbstractGameBoardControlMode {

	public AdventureMode(GameBoard board, IEventContext eventContext) {
		super(board, eventContext);
	}

	public void configureTileState() {
		getGameBoard().clearDisabledTileUi();
		getGameBoard().clearCrosshairUi();
	}

	@Override
	public void down() {
		tryMove(Direction.DOWN);
	}

	@Override
	public void enter() {
		// show in game menu
	}

	@Override
	public void left() {
		tryMove(Direction.LEFT);
	}

	@Override
	public void right() {
		tryMove(Direction.RIGHT);
	}

	public void space() {
		getGameBoard().nextTurn();
	}
	
	/**
	 * Convenience method combines delegation call of board.tryMove() with
	 * board.nextTurn() if move was successful
	 * 
	 * @param dir
	 */
	private void tryMove(Direction dir) {
		GameBoard board = getGameBoard();
		if (board.tryMove(dir)) {
			board.alignViewport();
			board.stepNeutralCritters();
			board.nextTurn();
		}
	}

	@Override
	public void up() {
		tryMove(Direction.UP);
	}

	@Override
	public void x() {
		getEventContext().fire(GameEventContext.GameEventType.COORDINATES_REQUEST);
	}

}
