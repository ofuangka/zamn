package zamn.board.controlmode;

import zamn.board.Tile;
import zamn.board.piece.Critter;

public abstract class AbstractEffect {

	private Critter critter;

	public Critter getCritter() {
		return critter;
	}

	public abstract void apply(Tile targetTile);

	public void setCritter(Critter critter) {
		this.critter = critter;
	}
}
