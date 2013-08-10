package zamn.board.tilecollector;

import java.util.List;

import zamn.board.Tile;

public abstract class AbstractFilteredTileCollector implements ITileCollector {

	private final TileListFilter filter;

	public AbstractFilteredTileCollector(TileListFilter filter) {
		this.filter = filter;
	}

	@Override
	public List<Tile> collect(Tile targetTile) {
		return filter.filter(doCollect(targetTile));
	}

	protected abstract List<Tile> doCollect(Tile targetTile);

}
