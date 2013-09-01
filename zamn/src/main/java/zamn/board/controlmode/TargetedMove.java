package zamn.board.controlmode;

import zamn.board.tilecollector.ITileCollector;
import zamn.board.tilecollector.TileListFilter;

/**
 * actions are all made up of a targeting range, an area of effect, and the
 * effect itself
 * 
 * @author ofuangka
 * 
 */
public class TargetedMove {
	private final TileListFilter actualRangeFilter;
	private final ITileCollector areaOfEffect;
	private final AbstractEffect effect;
	private final int mpCost;
	private final String name;
	private final ITileCollector targetingRange;

	public TargetedMove(String name, ITileCollector targetingRange,
			TileListFilter actualRangeFilter, ITileCollector areaOfEffect,
			AbstractEffect effect, int mpCost) {
		this.name = name;
		this.targetingRange = targetingRange;
		this.actualRangeFilter = actualRangeFilter;
		this.areaOfEffect = areaOfEffect;
		this.effect = effect;
		this.mpCost = mpCost;
	}

	public TileListFilter getActualRangeFilter() {
		return actualRangeFilter;
	}

	public ITileCollector getAreaOfEffect() {
		return areaOfEffect;
	}

	public AbstractEffect getEffect() {
		return effect;
	}
	
	public int getMpCost() {
		return mpCost;
	}

	public String getName() {
		return name;
	}

	public ITileCollector getTargetingRange() {
		return targetingRange;
	}
}
