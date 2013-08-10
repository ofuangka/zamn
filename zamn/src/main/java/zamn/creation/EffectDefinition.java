package zamn.creation;

public class EffectDefinition {
	private String affectedStat;
	private String drivingStat;
	private int max;
	private int min;
	private boolean positive;

	public String getAffectedStat() {
		return affectedStat;
	}

	public String getDrivingStat() {
		return drivingStat;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public boolean isPositive() {
		return positive;
	}

	public void setAffectedStat(String affectedStat) {
		this.affectedStat = affectedStat;
	}

	public void setDrivingStat(String drivingStat) {
		this.drivingStat = drivingStat;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}
}
