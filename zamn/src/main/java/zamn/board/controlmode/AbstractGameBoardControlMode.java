package zamn.board.controlmode;

import zamn.board.GameBoard;
import zamn.framework.event.GameEventContext;
import zamn.framework.event.IEventContext;
import zamn.ui.IKeySink;

/**
 * This is a special KeySink that has visibility of the Board and the overall
 * EventContext. The Board keeps a stack of these as its key delegates
 * 
 * @author ofuangka
 * 
 */
public abstract class AbstractGameBoardControlMode implements IKeySink {

	private GameBoard board;
	private IEventContext eventContext;

	public AbstractGameBoardControlMode(GameBoard board,
			IEventContext eventContext) {
		this.board = board;
		this.eventContext = eventContext;
	}

	@Override
	public void backspace() {

	}

	/**
	 * This method does all of the enabling and targeting of tiles necessary for
	 * this mode's state. It should clear all previously enabled and targeted
	 * tiles before doing the new ones
	 */
	public abstract void configureTileState();

	@Override
	public void esc() {
		eventContext.fire(GameEventContext.GameEventType.SYSTEM_MENU_REQUEST);
	}

	public IEventContext getEventContext() {
		return eventContext;
	}

	public GameBoard getGameBoard() {
		return board;

	}

	/**
	 * This method should store the mode's state
	 */
	public void readAndStoreState() {

	}

	/**
	 * This method resets the mode without recreating its state
	 */
	public void resetToOriginalState() {

	}

	@Override
	public void space() {

	}

	@Override
	public void x() {

	}

}
