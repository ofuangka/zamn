package zamn.creation;

import java.util.List;
import java.util.Map;

public class CritterDefinition extends BoardPieceDefinition {
	private boolean hostile;
	private Map<String, Integer> stats;
	private List<String> talents;

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

	public void setStats(Map<String, Integer> stats) {
		this.stats = stats;
	}

	public void setTalents(List<String> talents) {
		this.talents = talents;
	}
}
