package zamn.board.controlmode;

import zamn.board.Tile;
import zamn.board.piece.Critter;

public abstract class AbstractEffect {

	private Critter source;

	public Critter getSource() {
		return source;
	}

	public abstract void interact(Tile targetTile);

	public void setSource(Critter source) {
		this.source = source;
	}
}
