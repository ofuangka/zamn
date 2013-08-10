package zamn.board.tilecollector;

import java.util.ArrayList;
import java.util.List;

import zamn.board.Tile;
import zamn.board.piece.Critter;

public class TileListFilter {

	public enum TileListFilterType {
		NULL_TILE_FILTER, OCCUPIED_BY_CRITTER_FILTER
	}

	public static final TileListFilter NULL_TILE_FILTER = new TileListFilter(
			new ITilePredicate() {

				@Override
				public boolean apply(Tile t) {
					return t != null;
				}
			});

	public static final TileListFilter OCCUPIED_BY_CRITTER_FILTER = new TileListFilter(
			new ITilePredicate() {

				@Override
				public boolean apply(Tile t) {
					return t != null
							&& t.isOccupied()
							&& Critter.class.isAssignableFrom(t.getOccupant()
									.getClass());
				}
			});

	private ITilePredicate predicate;

	public TileListFilter(ITilePredicate predicate) {
		this.predicate = predicate;
	}

	public List<Tile> filter(List<Tile> unfiltered) {
		List<Tile> ret = new ArrayList<Tile>();

		if (unfiltered != null) {
			for (Tile tile : unfiltered) {
				if (predicate.apply(tile)) {
					ret.add(tile);
				}
			}
		}

		return ret;
	}
}
