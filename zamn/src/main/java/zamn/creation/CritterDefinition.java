package zamn.creation;

import java.util.List;
import java.util.Map;

public class CritterDefinition {
	private boolean hostile;
	private String spriteId;
	private Map<String, Integer> stats;
	private List<String> talents;

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
