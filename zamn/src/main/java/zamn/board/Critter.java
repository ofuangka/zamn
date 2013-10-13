package zamn.board;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zamn.util.NullSafeCompare;

public class Critter extends BoardPiece {
	
	public interface IStat {
		int getDefaultValue();

		Stat getMaxStat();
	}
	public enum Stat implements IStat {
		HP {
			@Override
			public int getDefaultValue() {
				return DEFAULT_STAT_VALUE;
			}

			@Override
			public Stat getMaxStat() {
				return MAXHP;
			}
		},
		MAXHP {
			@Override
			public int getDefaultValue() {
				return DEFAULT_STAT_VALUE;
			}

			@Override
			public Stat getMaxStat() {
				return null;
			}
		},
		MAXMP {
			@Override
			public int getDefaultValue() {
				return DEFAULT_STAT_VALUE;
			}

			@Override
			public Stat getMaxStat() {
				return null;
			}
		},
		MP {
			@Override
			public int getDefaultValue() {
				return DEFAULT_STAT_VALUE;
			}

			@Override
			public Stat getMaxStat() {
				return MAXMP;
			}
		},
		SIGHT {
			@Override
			public int getDefaultValue() {
				return DEFAULT_STAT_VALUE;
			}

			@Override
			public Stat getMaxStat() {
				return null;
			}
		},
		SMARTS {
			@Override
			public int getDefaultValue() {
				return DEFAULT_STAT_VALUE;
			}

			@Override
			public Stat getMaxStat() {
				return null;
			}
		},
		SPEED {
			@Override
			public int getDefaultValue() {
				return DEFAULT_SPEED_VALUE;
			}

			@Override
			public Stat getMaxStat() {
				return null;
			}
		},
		STRENGTH {
			@Override
			public int getDefaultValue() {
				return DEFAULT_STAT_VALUE;
			}

			@Override
			public Stat getMaxStat() {
				return null;
			}
		}
	}

	private static final int CRITTER_Z = 1000;

	private static final String DEFAULT_ATTACK_ID = "default_attack";

	private static final int DEFAULT_SPEED_VALUE = 3;

	private static final int DEFAULT_STAT_VALUE = 5;

	public static final Comparator<Critter> SPEED_COMPARATOR = new Comparator<Critter>() {

		@Override
		public int compare(Critter o1, Critter o2) {
			return NullSafeCompare.compare(o1.getStat(Stat.SPEED),
					o2.getStat(Stat.SPEED));
		}

	};

	private boolean hostile;
	private boolean selected;

	private Map<Stat, Integer> stats = new HashMap<Stat, Integer>();
	private List<String> talents;

	public Critter() {
		setSolid(true);
		setZ(CRITTER_Z);
	}

	public String getAttack() {
		return DEFAULT_ATTACK_ID;
	}

	public int getStat(Stat stat) {
		Integer ret = stats.get(stat);
		return (ret == null) ? stat.getDefaultValue() : ret;
	}

	public Map<Stat, Integer> getStats() {
		return stats;
	}

	public List<String> getTalents() {
		return talents;
	}

	public boolean isHostile() {
		return hostile;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setHostile(boolean hostile) {
		this.hostile = hostile;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setStat(Stat stat, int value) {
		stats.put(stat, value);
	}

	public void setTalents(List<String> talents) {
		this.talents = talents;
	}

}
