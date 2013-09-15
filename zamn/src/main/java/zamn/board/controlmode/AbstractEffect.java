package zamn.board.controlmode;

import zamn.board.Critter;
import zamn.board.Tile;

public abstract class AbstractEffect {

	private Critter critter;

	public abstract void apply(Tile targetTile);

	public Critter getCritter() {
		return critter;
	}

	public void setCritter(Critter critter) {
		this.critter = critter;
	}
}
