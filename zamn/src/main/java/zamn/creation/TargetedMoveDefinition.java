package zamn.creation;

public class TargetedMoveDefinition {

	private String actualRangeFilter;
	private TileCollectorDefinition areaOfEffect;
	private EffectDefinition effect;
	private String name;
	private TileCollectorDefinition targetingRange;
	private int mpCost;

	public String getActualRangeFilter() {
		return actualRangeFilter;
	}

	public TileCollectorDefinition getAreaOfEffect() {
		return areaOfEffect;
	}

	public EffectDefinition getEffect() {
		return effect;
	}
	
	public int getMpCost() {
		return mpCost;
	}

	public String getName() {
		return name;
	}

	public TileCollectorDefinition getTargetingRange() {
		return targetingRange;
	}

	public void setActualRangeFilter(String actualRangeFilter) {
		this.actualRangeFilter = actualRangeFilter;
	}

	public void setAreaOfEffect(TileCollectorDefinition areaOfEffect) {
		this.areaOfEffect = areaOfEffect;
	}

	public void setEffect(EffectDefinition effect) {
		this.effect = effect;
	}
	
	public void setMpCost(int mpCost) {
		this.mpCost = mpCost;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTargetingRange(TileCollectorDefinition targetedRange) {
		this.targetingRange = targetedRange;
	}
}
