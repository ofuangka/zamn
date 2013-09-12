package zamn.creation;

import java.util.List;
import java.util.Map;

public class CritterDefinition {
	private boolean hostile;
	private String spriteId;
	private Map<String, Integer> stats;
	private List<String> talents;
	private int seedX;
	private int seedY;

	public void setSeedX(int seedX) {
		this.seedX = seedX;
	}

	public void setSeedY(int seedY) {
		this.seedY = seedY;
	}

	public int getSeedX() {
		return seedX;
	}

	public int getSeedY() {
		return seedY;
	}

	public String getSpriteId() {
		return spriteId;
	}

	public Map<String, Integer> getStats() {
		return stats;
	}

	public List<String> getTalents() {
		return talents;
	}

	public boolean isHostile() {
		return hostile;
	}

	public void setHostile(boolean hostile) {
		this.hostile = hostile;
	}

	public void setSpriteId(String spriteId) {
		this.spriteId = spriteId;
	}

	public void setStats(Map<String, Integer> stats) {
		this.stats = stats;
	}

	public void setTalents(List<String> talents) {
		this.talents = talents;
	}
}
