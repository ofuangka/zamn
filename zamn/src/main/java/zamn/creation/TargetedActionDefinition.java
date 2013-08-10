package zamn.creation;

public class TargetedActionDefinition {

	private String actualRangeFilterType;
	private TileCollectorDefinition areaOfEffect;
	private EffectDefinition effect;
	private String name;
	private TileCollectorDefinition targetingRange;

	public String getActualRangeFilterType() {
		return actualRangeFilterType;
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

	public void setActualRangeFilterType(String actualRangeFilterType) {
		this.actualRangeFilterType = actualRangeFilterType;
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
