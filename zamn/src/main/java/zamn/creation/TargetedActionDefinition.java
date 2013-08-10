package zamn.creation;

public class TargetedActionDefinition {

	private String actualRangeFilter;
	private TileCollectorDefinition areaOfEffect;
	private EffectDefinition effect;
	private String name;
	private TileCollectorDefinition targetingRange;

	public String getActualRangeFilter() {
		return actualRangeFilter;
	}

	public TileCollectorDefinition getAreaOfEffect() {
		return areaOfEffect;
	}

	public EffectDefinition getEffect() {
		return effect;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setTargetingRange(TileCollectorDefinition targetedRange) {
		this.targetingRange = targetedRange;
	}
}
