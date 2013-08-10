package zamn.board.piece;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zamn.board.AbstractBoardPiece;
import zamn.util.NullSafeCompare;

public class Critter extends AbstractBoardPiece {

	public interface IHasDefaultValue {
		public int getDefaultValue();
	}

	public enum Stat implements IHasDefaultValue {
		HP {
			@Override
			public int getDefaultValue() {
				return 5;
			}
		},
		MAXHP {
			@Override
			public int getDefaultValue() {
				return 5;
			}
		},
		SMARTS {
			@Override
			public int getDefaultValue() {
				return 5;
			}
		},
		SPEED {
			@Override
			public int getDefaultValue() {
				return 3;
			}
		},
		STRENGTH {
			@Override
			public int getDefaultValue() {
				return 5;
			}
		}
	}

	private static final String DEFAULT_ATTACK_ID = "default_attack";

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
		setTakingUpSpace(true);
	}

	public String getAttack() {
		return DEFAULT_ATTACK_ID;
	}

	public int getStat(Stat stat) {
		Integer ret = stats.get(stat);
		return (ret == null) ? stat.getDefaultValue() : ret;
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
