package zamn.board.tilecollector;

import java.util.ArrayList;
import java.util.List;

import zamn.board.Tile;
import zamn.board.piece.Critter;

public enum TileListFilter implements ITileListFilter {

	NULL_TILE_FILTER {
		@Override
		public boolean apply(Tile t) {
			return t != null;
		}
	},
	OCCUPIED_BY_CRITTER_FILTER {
		@Override
		public boolean apply(Tile t) {
			return t != null
					&& t.isOccupied()
					&& Critter.class.isAssignableFrom(t.getOccupant()
							.getClass());
		}
	};

	public List<Tile> filter(List<Tile> unfiltered) {
		List<Tile> ret = new ArrayList<Tile>();

		if (unfiltered != null) {
			for (Tile tile : unfiltered) {
				if (apply(tile)) {
					ret.add(tile);
				}
			}
		}

		return ret;
	}
}
