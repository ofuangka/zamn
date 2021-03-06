package zamn.board.tilecollector;

import zamn.board.Critter;
import zamn.board.Tile;

public class CombatMovementTileCollector extends AbstractDecayingTileCollector {

	private Critter critter;

	public CombatMovementTileCollector(TileListFilter filter, Critter critter) {
		super(filter);
		this.critter = critter;
	}

	@Override
	protected int getCostThreshold() {
		return critter.getStat(Critter.Stat.SPEED);
	}

	@Override
	protected int getTileCost(Tile tile) {
		return tile.getMovementCost();
	}

	protected boolean isInclusive() {
		return true;
	}

	@Override
	protected boolean isTileValid(Tile tile) {
		return tile != null && !tile.isSolid()
				&& (!tile.isOccupied() || tile.getOccupant() == critter);
	}

}
