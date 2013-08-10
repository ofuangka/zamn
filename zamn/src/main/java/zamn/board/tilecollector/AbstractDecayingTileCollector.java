package zamn.board.tilecollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zamn.board.Tile;

/**
 * Uses TileCostTracker objects to implement an algorithm that takes Tile cost
 * into account when collecting Tiles. This algorithm adds the Target tile at no
 * cost and then adds adjacent Tiles, subtracting their cost from the running
 * total. Once the cost threshold is met, Tiles are no longer added to the
 * result List.
 * 
 * @author ofuangka
 * 
 */
public abstract class AbstractDecayingTileCollector extends
		AbstractFilteredTileCollector {

	public AbstractDecayingTileCollector(TileListFilter filter) {
		super(filter);
	}

	@Override
	public List<Tile> doCollect(Tile targetTile) {
		Set<TileCostTracker> open = new HashSet<TileCostTracker>();
		Set<Tile> validTiles = new HashSet<Tile>();
		int costThreshold = getCostThreshold();

		// include the target tile at no cost
		open.add(new TileCostTracker(targetTile, 0));

		TileCostTracker current;
		while (!open.isEmpty()) {
			current = open.iterator().next();

			if (current.getCost() <= costThreshold) {
				validTiles.add(current.getTile());
				Tile[] adjacentTiles = current.getTile().getAdjacentTiles();
				for (Tile tile : adjacentTiles) {
					if (isTileValid(tile)) {
						open.add(new TileCostTracker(tile, getTileCost(tile)
								+ current.getCost()));
					}
				}
			}

			open.remove(current);
		}
		if (!isInclusive()) {
			validTiles.remove(targetTile);
		}

		// sort the tiles
		List<Tile> ret = new ArrayList<Tile>(validTiles);
		Collections.sort(ret, Tile.TOP_BOTTOM_LEFT_RIGHT);
		return ret;
	}

	/**
	 * Implement this method to determine how far the collector is allowed to
	 * decay
	 * 
	 * @return
	 */
	protected abstract int getCostThreshold();

	/**
	 * Implement this method to determine the cost of a given tile
	 * 
	 * @param tile
	 * @return
	 */
	protected abstract int getTileCost(Tile tile);

	/**
	 * Returns true if this TileCollector needs to add itself to the result List
	 * 
	 * @return
	 */
	protected abstract boolean isInclusive();

	/**
	 * Implement this method to determine if a tile within the threshold should
	 * be added to the resulting collection
	 * 
	 * @param tile
	 * @return
	 */
	protected abstract boolean isTileValid(Tile tile);

}
