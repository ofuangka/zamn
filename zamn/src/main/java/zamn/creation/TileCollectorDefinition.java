package zamn.creation;

public class TileCollectorDefinition {
	private String filter;
	private boolean inclusive;
	private int range;
	private String type;

	public String getFilter() {
		return filter;
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

	public void setFilter(String filter) {
		this.filter = filter;
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
