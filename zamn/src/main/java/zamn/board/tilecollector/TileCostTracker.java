package zamn.board.tilecollector;

import zamn.board.Tile;

public class TileCostTracker {
	private int cost;
	private Tile tile;

	public TileCostTracker(Tile tile, int cost) {
		this.tile = tile;
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

	public Tile getTile() {
		return tile;
	}
}
