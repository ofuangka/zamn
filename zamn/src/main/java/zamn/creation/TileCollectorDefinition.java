package zamn.creation;

public class TileCollectorDefinition {
	private String filterType;
	private boolean inclusive;
	private int range;
	private String type;

	public String getFilterType() {
		return filterType;
	}

	public int getRange() {
		return range;
	}

	public String getType() {
		return type;
	}

	public boolean isInclusive() {
		return inclusive;
	}

	public void setFilterType(String filter) {
		this.filterType = filter;
	}

	public void setInclusive(boolean inclusive) {
		this.inclusive = inclusive;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public void setType(String type) {
		this.type = type;
	}
}
