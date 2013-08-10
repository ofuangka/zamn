package zamn.board.controlmode;

import zamn.board.tilecollector.ITileCollector;
import zamn.board.tilecollector.TileListFilter;

/**
 * CritterActions are all made up of a targeting range, and area of effect, and
 * the effect itself
 * 
 * @author ofuangka
 * 
 */
public class TargetedAction {
	private final TileListFilter actualRangeFilter;
	private final ITileCollector areaOfEffect;
	private final AbstractEffect effect;
	private final String name;
	private final ITileCollector targetingRange;

	public TargetedAction(String name, ITileCollector targetingRange,
			TileListFilter actualRangeFilter, ITileCollector areaOfEffect,
			AbstractEffect effect) {
		this.name = name;
		this.targetingRange = targetingRange;
		this.actualRangeFilter = actualRangeFilter;
		this.areaOfEffect = areaOfEffect;
		this.effect = effect;
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

	public String getName() {
		return name;
	}

	public ITileCollector getTargetingRange() {
		return targetingRange;
	}
}
