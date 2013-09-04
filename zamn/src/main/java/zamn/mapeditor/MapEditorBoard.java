package zamn.mapeditor;

import java.net.URI;

import zamn.board.AbstractBoard;

public class MapEditorBoard extends AbstractBoard {

	private static final long serialVersionUID = 8159431301832393440L;

	@Override
	public void backspace() {
		// remove a piece from the currently selected tile
	}

	@Override
	public void down() {
		// move the cursor down
	}

	@Override
	public void enter() {
		// add the piece to the currently selected tile
	}

	@Override
	public void esc() {
		// do nothing
	}

	@Override
	public boolean isListening() {
		return true;
	}

	@Override
	public void left() {
		// move the cursor left
	}

	@Override
	public void load(URI boardId) {

	}

	@Override
	public void right() {
		// move the cursor right
	}

	@Override
	public void space() {
		// add the piece to the currently selected tile
	}

	@Override
	public void up() {
		// move the cursor up
	}

	@Override
	public void x() {
		// toggle the tile's walkability
	}
}
